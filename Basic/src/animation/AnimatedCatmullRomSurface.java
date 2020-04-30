package animation;

import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;

import animation.physics.ForceField;
import dataStructures.CircularGrid;
import model.CatmullRomSurfaces;
import model.RenderableObject;

public class AnimatedCatmullRomSurface{
	private CircularGrid<Vector3f> positions;
	private CircularGrid<Vector3f> velocities;
	
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	private static float naturalInnerRadius = 2;
	private static float naturalOuterRadius = 8;
	private static int toroidPatchCircumference = 20;
	private static int circumferenceInPatches = 10;
	
	private float age;
	
	public AnimatedCatmullRomSurface() {
		positions = CatmullRomSurfaces.crToroidPatchData(toroidPatchCircumference, circumferenceInPatches, naturalOuterRadius, naturalInnerRadius);
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(positions);
		
		SURFACE = RenderableObject.catmullRomSurfaceFactory(patchData);
		POINTS = RenderableObject.setupPointCloud(patchData);
		
		velocities = new CircularGrid<>(toroidPatchCircumference, circumferenceInPatches);
		for (int i = 0; i < velocities.HEIGHT; i++) {
			for (int j = 0; j < velocities.WIDTH; j++) {
				velocities.set(i, j, new Vector3f());
			}
		}
		age = 0;
	}
	
	public void update(List<ForceField> forceFields, float delta) {
		for (Iterator<ForceField> it = forceFields.iterator(); it.hasNext();) {
			ForceField forceField = it.next();
			forceField.applyForceField(positions, velocities, delta);
		}
		applyElasticForces(delta);
		
		updatePositions(delta);
		age += delta;
		
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(positions);
		
		SURFACE.updateVertices(patchData);
		POINTS.updateVertices(patchData);
	}

	private void updatePositions(float delta) {
		float ageFactor = Math.max(0, (20 - age) / 20);
		for(int i = 0; i != positions.HEIGHT; ++i) {
			for (int j = 0; j < velocities.WIDTH; j++) {
				positions.get(i, j).add(new Vector3f(velocities.get(i, j)).mul(delta).mul(ageFactor));
			}
		}
	}

	private void applyElasticForces(float delta) {
		
		for(int i = 0; i != positions.HEIGHT; ++i) {
			Vector3f center = new Vector3f();
			
			for (int j = 0; j != positions.WIDTH; ++j) {
				center.add(positions.get(i, j));
			}
			center.div(positions.WIDTH);
			
			for (int j = 0; j != positions.WIDTH; ++j) {
				//Ring force, which attempts to maintain the tube shape
				Vector3f point = positions.get(i, j);
				Vector3f direction = new Vector3f(center).sub(point).normalize();
				float magnitude = center.distance(point) - naturalInnerRadius;
				Vector3f ringForce = direction.mul(magnitude * delta);
				
				//Skin tension force, which attempts to maintain the toroid shape
				
				velocities.get(i, j).add(ringForce);
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i+1, j), 2.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i, j+1), 2.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i-1, j), 2.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i, j-1), 2.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i-1, j-1), 3.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i+1, j-1), 3.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i+1, j+1), 3.5f, delta));
				velocities.get(i, j).add(tensionWithPoint(point, positions.get(i-1, j+1), 3.5f, delta));
			}
		}
	}
	
	private static Vector3f tensionWithPoint(Vector3f center, Vector3f point, float distance, float delta) {
		float naturalDistance = 3;
		Vector3f tension = new Vector3f(center).sub(point).normalize();
		
		float magnitude = naturalDistance - center.distance(point);
		magnitude *= 0.1f;
		return tension.mul(magnitude* delta);
	}
}

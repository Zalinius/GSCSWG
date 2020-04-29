package animation;

import java.util.List;

import org.joml.Vector3f;

import animation.physics.ForceField;
import dataStructures.CircularGrid;
import model.CatmullRomSurfaces;
import model.RenderableObject;

public class AnimatedCatmullRomSurface{
	private CircularGrid<Vector3f> pointGrid;
	
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	private static float naturalInnerRadius = 2;
	
	public AnimatedCatmullRomSurface() {
		pointGrid = CatmullRomSurfaces.crToroidPatchData(10, 4, 8, naturalInnerRadius);
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(pointGrid);
		
		SURFACE = RenderableObject.catmullRomSurfaceFactory(patchData);
		POINTS = RenderableObject.setupPointCloud(patchData);
	}
	
	public void update(ForceField forceField, float delta) {
		forceField.applyForceField(pointGrid, delta);
		
		applyElasticForces(delta);
		
		
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(pointGrid);
		
		SURFACE.updateVertices(patchData);
		POINTS.updateVertices(patchData);
	}

	private void applyElasticForces(float delta) {
		
		for(int i = 0; i != pointGrid.HEIGHT; ++i) {
			Vector3f center = new Vector3f();
			
			for (int j = 0; j != pointGrid.WIDTH; ++j) {
				center.add(pointGrid.get(i, j));
			}
			center.div(pointGrid.WIDTH);
			
			for (int j = 0; j != pointGrid.WIDTH; ++j) {
				Vector3f point = pointGrid.get(i, j);
				Vector3f direction = new Vector3f(center).sub(point).normalize();
				float magnitude = center.distance(point) - naturalInnerRadius;
				
				Vector3f force = direction.mul(magnitude * delta);
				
				point.add(force);
			}
		}
	}
}

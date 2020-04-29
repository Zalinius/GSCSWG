package animation;

import java.util.List;

import org.joml.Vector3f;

import animation.physics.ForceField;
import model.CatmullRomSurfaces;
import model.RenderableObject;

public class AnimatedCatmullRomSurface{
	private Vector3f[][] pointGrid;
	
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	public AnimatedCatmullRomSurface() {
		pointGrid = CatmullRomSurfaces.crToroidPatchData(10, 4, 8, 2);
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(pointGrid);
		
		SURFACE = RenderableObject.catmullRomSurfaceFactory(patchData);
		POINTS = RenderableObject.setupPointCloud(patchData);
	}
	
	public void update(ForceField forceField, float delta) {
		forceField.applyForceField(pointGrid, delta);
		
		List<Vector3f> patchData = CatmullRomSurfaces.generateCyclicCatmullRomPatches(pointGrid);
		
		SURFACE.updateVertices(patchData);
		POINTS.updateVertices(patchData);
	}
}

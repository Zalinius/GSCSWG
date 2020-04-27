package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class SplineSurface {
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	public SplineSurface(RenderableObject surface, RenderableObject points) {
		this.SURFACE = surface;
		this.POINTS = points;
	}
	
	
	public static SplineSurface crSurface() {
		return new SplineSurface(RenderableObject.CATMULL_ROM_SURFACE, RenderableObject.CATMULL_ROM_POINTS);
	}
	
	private static List<Vector3f> crCubeFacePatchData() {
		List<Vector3f> patches = new ArrayList<>();
		
		Vector3f p0 = new Vector3f(0,1,0);
		Vector3f p1 = new Vector3f(1,1,0);
		Vector3f p2 = new Vector3f(0,1,1);
		Vector3f p3 = new Vector3f(1,1,1);
		Vector3f p4 = new Vector3f(0,0,0);
		Vector3f p5 = new Vector3f(1,0,0);
		Vector3f p6 = new Vector3f(0,0,1);
		Vector3f p7 = new Vector3f(1,0,1);
		
		patches.add(new Vector3f(p5));
		patches.add(new Vector3f(p5));
		patches.add(new Vector3f(p4));
		patches.add(new Vector3f(p4));

		patches.add(new Vector3f(p5));
		patches.add(new Vector3f(p2));
		patches.add(new Vector3f(p3));
		patches.add(new Vector3f(p4));

		patches.add(new Vector3f(p1));
		patches.add(new Vector3f(p6));
		patches.add(new Vector3f(p7));
		patches.add(new Vector3f(p0));
		
		patches.add(new Vector3f(p1));
		patches.add(new Vector3f(p1));
		patches.add(new Vector3f(p0));
		patches.add(new Vector3f(p0));
		
		
		patches.add(new Vector3f(p7));
		patches.add(new Vector3f(p7));
		patches.add(new Vector3f(p5));
		patches.add(new Vector3f(p5));

		patches.add(new Vector3f(p7));
		patches.add(new Vector3f(p0));
		patches.add(new Vector3f(p2));
		patches.add(new Vector3f(p5));

		patches.add(new Vector3f(p3));
		patches.add(new Vector3f(p4));
		patches.add(new Vector3f(p6));
		patches.add(new Vector3f(p1));

		patches.add(new Vector3f(p3));
		patches.add(new Vector3f(p3));
		patches.add(new Vector3f(p1));
		patches.add(new Vector3f(p1));

		
		return patches;
	}
	
	public static SplineSurface crCubeFacePatch() {
		List<Vector3f> patchData = crCubeFacePatchData();
		RenderableObject surface = RenderableObject.catmullRomSurfaceFactory(patchData);
		RenderableObject points = RenderableObject.setupPointCloud(patchData);
		
		return new SplineSurface(surface, points);
	}

}

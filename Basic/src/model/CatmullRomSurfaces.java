package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import dataStructures.CircularGrid;

public class CatmullRomSurfaces {
	public static SplineSurface crSurface() {
		return new SplineSurface(RenderableObject.CATMULL_ROM_SURFACE, RenderableObject.CATMULL_ROM_POINTS);
	}
	
	public static SplineSurface crCubeFacePatch() {
		List<Vector3f> patchData = crCubeFacePatchData();
		RenderableObject surface = RenderableObject.catmullRomSurfaceFactory(patchData);
		RenderableObject points = RenderableObject.setupPointCloud(patchData);
		
		return new SplineSurface(surface, points);
	}
	public static SplineSurface crToroidSurface() {
		List<Vector3f> patchData = generateCyclicCatmullRomPatches(crToroidPatchData(3,3,10,1));
		RenderableObject surface = RenderableObject.catmullRomSurfaceFactory(patchData);
		RenderableObject points = RenderableObject.setupPointCloud(patchData);
		
		return new SplineSurface(surface, points);
	}
	
	public static CircularGrid<Vector3f> crToroidPatchData(int length, int width, float toroidRadius, float cylinderRadius){
		CircularGrid<Vector3f> toroidGrid = new CircularGrid<>(length, width);
		
		for(int i = 0; i != length; ++i) {
			float phi = (float) (2*Math.PI * i) / length;
			Vector3f segmentCenter = new Vector3f((float)Math.sin(phi), 0, (float)Math.cos(phi));

			segmentCenter.mul(toroidRadius);
			
			for(int j = 0; j != width; ++j) {
				float theta = (float) (2*Math.PI * j) / width;
				
				Vector3f up = new Vector3f(0,cylinderRadius,0).mul((float) Math.cos(theta));
				Vector3f right = new Vector3f(segmentCenter).normalize().mul(cylinderRadius * (float) Math.sin(theta));
				Vector3f toroidPoint = new Vector3f(segmentCenter).add(up).add(right);
				
				toroidGrid.set(i, j, toroidPoint);
			}
		}
		
		return toroidGrid;
	}
	
	public static List<Vector3f> generateNonCyclicCatmullRomPatches(Vector3f[][] surfacePoints) {
		List<Vector3f> patches = new ArrayList<>();
		//TODO

		for(int iPatch = 0; iPatch != 7; ++iPatch) {
			for(int jPatch = 0; jPatch != 7; ++jPatch) {
				for(int i = 0; i != 4; ++i) {
					for(int j = 0; j != 4; ++j) {
						Vector3f point = surfacePoints[iPatch + i][jPatch + j];
						patches.add(point);
					}
				}
			}
		}
		
		return patches;
	}
	
	public static List<Vector3f> generateCyclicCatmullRomPatches(CircularGrid<Vector3f> surfacePoints) {
		List<Vector3f> patches = new ArrayList<>();
		int gridLength = surfacePoints.HEIGHT;
		int gridWidth = surfacePoints.WIDTH;

		for(int iPatch = 0; iPatch != gridLength; ++iPatch) {
			for(int jPatch = 0; jPatch != gridWidth; ++jPatch) {
				for(int i = 0; i != 4; ++i) {
					for(int j = 0; j != 4; ++j) {
						int gridCoordinateI = (iPatch + i) % gridLength;
						int gridCoordinateJ = (jPatch + j) % gridWidth;
						Vector3f point = surfacePoints.get(gridCoordinateI, gridCoordinateJ);
						patches.add(point);
					}
				}
			}
		}
		
		return patches;
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
	

}

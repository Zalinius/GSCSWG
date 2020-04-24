package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class BezierCylinder {
	
	private Vector3f[] points;
	private Vector3f[] tangents;

	public BezierCylinder() {
		points = new Vector3f[2];
		tangents = new Vector3f[4];
		
		points[0] = new Vector3f( 0, 1, 0);
		points[1] = new Vector3f( 0,-1, 0);
		
		float meow = (float) Math.sqrt(2);
		tangents[0] = new Vector3f(-meow, 1, 0);
		tangents[1] = new Vector3f(-meow,-1, 0);
		tangents[2] = new Vector3f( meow,-1, 0);
		tangents[3] = new Vector3f( meow, 1, 0);

	}
	
	public List<Vector3f> getPatchData() {
		List<Vector3f> patches = new ArrayList<>();
		
		for(int i = 0; i != 4; ++i) {
			double phi = i * Math.PI/6.0;
		}
		
		
		
		//2 patches
		for(int i = 0; i != 2; ++i) {
			int ip1 = (i+1)%2;
			
			//16 points per patch
			for(int depth = 0; depth != 4; ++depth) {
				Vector3f depthVec = new Vector3f(0,0,depth);
				patches.add(new Vector3f(points[i]).add(depthVec));
				patches.add(new Vector3f(tangents[2*i ]).add(depthVec));
				patches.add(new Vector3f(tangents[2*i + 1]).add(depthVec));
				patches.add(new Vector3f(points[ip1]).add(depthVec));
			}
		}
		
		return patches;

	}
	
	public RenderableObject getModel() {
		return RenderableObject.bezierCompositeSurface(getPatchData());
	}
	
	public RenderableObject getPoints() {
		return RenderableObject.setupPointCloud(getPatchData());
	}
	
	

}

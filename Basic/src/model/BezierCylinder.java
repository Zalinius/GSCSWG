package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import shader.CurveShader;

public class BezierCylinder {
	
	private Vector3f[] points;
	private Vector3f[] tangents;

	public BezierCylinder() {
		points = new Vector3f[4];
		tangents = new Vector3f[4];
		
		points[0] = new Vector3f( 1, 1, 0);
		points[1] = new Vector3f(-1, 1, 0);
		points[2] = new Vector3f(-1,-1, 0);
		points[3] = new Vector3f( 1,-1, 0);
		
		tangents[0] = new Vector3f( 0, 2, 0);
		tangents[1] = new Vector3f(-2, 0, 0);
		tangents[2] = new Vector3f( 0,-2, 0);
		tangents[3] = new Vector3f( 2, 0, 0);	
	}
	
	public RenderableObject getModel() {
		List<Vector3f> patches = new ArrayList<>();
		
		//4 patches
		for(int i = 0; i != 4; ++i) {
			int ip1 = (i+3)%4;
			
			//16 points per patch
			for(int depth = 0; depth != 4; ++depth) {
				Vector3f depthVec = new Vector3f(0,0,depth);
				patches.add(new Vector3f(points[i]).add(depthVec));
				patches.add(new Vector3f(tangents[ip1]).add(depthVec));
				patches.add(new Vector3f(tangents[ip1]).add(depthVec));
				patches.add(new Vector3f(points[ip1]).add(depthVec));
			}
		}
		
		return RenderableObject.bezierCompositeSurface(patches);

	}
	
	

}

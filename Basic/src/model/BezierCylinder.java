package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BezierCylinder {
	
	private Vector3f[] points;
	private Vector3f[] tangents;
	
	private RenderableObject model;
	private RenderableObject pointsModel;

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

		model = RenderableObject.bezierCompositeSurface(getPatchData());
		pointsModel = RenderableObject.setupPointCloud(getPatchData());
		age = 0;
	}
	
	/** 
	 * A Bezier cylinder controlled by a catmull-rom spline
	 * @param p0 control
	 * @param p1 point
	 * @param p2 point
	 * @param p3 control
	 */
	public BezierCylinder(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		Vector3f knot1, knot2, ctrl1, ctrl2;
		knot1 = p1;
		knot2 = p2;
		
		float t1 = 1.0f/3.0f;
		float t2 = 2.0f/3.0f;
		CatmullRomSpline cr = new CatmullRomSpline(p0, p1, p2, p3);
		ctrl1 = cr.position(t1);
		ctrl2 = cr.position(t2);
		
		BezierCircle c0, c1, c2, c3;
		
		c0 = new BezierCircle(knot1, cr.tangent(0), cr.unitBinormalVector(0));
		c1 = new BezierCircle(ctrl1, cr.tangent(t1), cr.unitBinormalVector(t1));
		c2 = new BezierCircle(ctrl2, cr.tangent(t2), cr.unitBinormalVector(t2));
		c3 = new BezierCircle(knot2, cr.tangent(1), cr.unitBinormalVector(1));
		
		//TODO Now, get the patch data		
	}

	
	private float age;
	public void update(float delta) {
		age += delta;
		float change = (float) Math.sin(age) * delta;
		
		points[0].add(0, change, 0);
		tangents[0].add(0, change, 0);
		tangents[3].add(0, change, 0);
		
		List<Vector3f> newPatchData = getPatchData(age);
		model.updateVertices(newPatchData);
		pointsModel.updateVertices(newPatchData);
		
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
	
	public List<Vector3f> getPatchData(float age) {
		List<Vector3f> patches = new ArrayList<>();
		
		for(int i = 0; i != 4; ++i) {
			double phi = i * Math.PI/6.0;
		}
		
		
		
		//2 patches
		for(int i = 0; i != 2; ++i) {
			int ip1 = (i+1)%2;
			
			//16 points per patch
			for(int depth = 0; depth != 4; ++depth) {
				float change = (float) Math.sin(age + depth);
				Vector3f depthVec = new Vector3f(0,change,depth);
				
				patches.add(new Vector3f(points[i]).add(depthVec));
				patches.add(new Vector3f(tangents[2*i ]).add(depthVec));
				patches.add(new Vector3f(tangents[2*i + 1]).add(depthVec));
				patches.add(new Vector3f(points[ip1]).add(depthVec));
			}
		}
		return patches;
	}
	
	public RenderableObject getModel() {
		return model;
	}
	
	public RenderableObject getPoints() {
		return pointsModel;
	}
	
	public static class BezierCircle{
		private Vector3f center, top, bottom;
		private Vector3f tTop, tBottom;
		private float tT0, tT1, tB0, tB1;
		
		private final float RADIUS = 1;
		
		public BezierCircle(Vector3f center, Vector3f normal, Vector3f right) {
			this.center = center;
			Vector3f up = new Vector3f().add(right).cross(normal).normalize();
			
			top = new Vector3f(up).mul(RADIUS).add(center);
			bottom = new Vector3f(up).mul(-RADIUS).add(center);
			
			tTop = new Vector3f(right).normalize();
			tBottom = new Vector3f(right).mul(-1).normalize();
			
			tT0 = 1;			
			tT1 = 1;			
			
			tB0 = 1;			
			tB1 = 1;			
		}
		
	}

}

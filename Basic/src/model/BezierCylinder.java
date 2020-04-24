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
		
		Matrix4x3f pMatrix = new Matrix4x3f(p0, p1, p2, p3);
		Matrix4f crMatrix = new Matrix4f(0, -1, 2, -1, 2, 0, -5, 3, 0, 1, 4, -3, 0, 0, -1, 1);
		
		Vector4f tVector = Utils.cubicVector(t1);
		tVector.mul(crMatrix);
		tVector.mul(pMatrix);
		ctrl1 = new Vector3f(tVector.x(),tVector.y(),tVector.z());
		
		tVector = Utils.cubicVector(t2);
		tVector.mul(crMatrix);
		tVector.mul(pMatrix);
		ctrl1 = new Vector3f(tVector.x(),tVector.y(),tVector.z());
		
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
	
	

}

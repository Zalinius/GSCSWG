package model;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CatmullRomSpline {
	
	private Vector3f p0, p1, p2, p3;

	public CatmullRomSpline(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Vector3f evaluate(float t) {
		Matrix4x3f pMatrix = new Matrix4x3f(p0, p1, p2, p3);
		Matrix4f crMatrix = new Matrix4f(0, 2, 0, 0, 
										-1, 0, 1, 0, 
										 2,-5, 4,-1,
										-1, 3,-3, 1);
		Vector4f tVector = Utils.cubicVector(t).mul(0.5f);
		
		tVector.mul(crMatrix);
		pMatrix.transform(tVector);
		Vector3f result = new Vector3f(tVector.x(),tVector.y(),tVector.z());
	
		return result;
	}
	
}

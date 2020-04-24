package model;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CatmullRomSpline {
	
	Matrix4x3f positionMatrix;
	
	public CatmullRomSpline(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {	
		positionMatrix = new Matrix4x3f(p0, p1, p2, p3);
	}
	public static final Matrix4f CR_MATRIX = new Matrix4f(
			 0, 2, 0, 0, 
			-1, 0, 1, 0, 
			 2,-5, 4,-1,
			-1, 3,-3, 1);
	
	private Vector3f evaluate(Vector4f parameter) {
		parameter.mul(CR_MATRIX);
		positionMatrix.transform(parameter);
		Vector3f result = new Vector3f(parameter.x(),parameter.y(),parameter.z());
		result.mul(0.5f);
	
		return result;
	}
	
	public Vector3f position(float t) {
		Vector4f tVector = Utils.cubicVector(t);
		return evaluate(tVector);
	}
	
	public Vector3f velocity(float t) {
		Vector4f tVector = Utils.cubicVectorD1Dt(t);
		return evaluate(tVector);
	}	
	public Vector3f tangent(float t) {
		return velocity(t).normalize();
	}
	
	public Vector3f acceleration(float t) {
		Vector4f tVector = Utils.cubicVectorD2Dt(t);
		return evaluate(tVector);
	}
	
	public Vector3f unitBinormalVector(float t) {
		return velocity(t).cross(acceleration(t)).normalize();
	}
	
}

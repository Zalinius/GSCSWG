package model;

import org.joml.Vector4f;

public class Utils {

	public static Vector4f cubicVector(float t) {
		return new Vector4f(1, t, t*t, t*t*t);
	}
	
	public static Vector4f cubicVectorD1Dt(float t) {
		return new Vector4f(0, 1, 2*t, 3*t*t);
	}
	public static Vector4f cubicVectorD2Dt(float t) {
		return new Vector4f(0, 0, 2, 6*t);
	}
	
	
}

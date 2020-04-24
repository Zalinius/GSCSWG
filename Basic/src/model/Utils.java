package model;

import org.joml.Vector4f;

public class Utils {

	public static Vector4f cubicVector(float t) {
		return new Vector4f(1, t, t*t, t*t*t);
	}
}

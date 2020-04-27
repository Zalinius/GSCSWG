package model;

import org.joml.Vector3f;

public class BezierCircle {
	private Vector3f center, top, bottom;
	private Vector3f tTop, tBottom;
	private float tT0, tT1, tB0, tB1;
	
	private final float RADIUS = 1;
	
	public BezierCircle(Vector3f center, Vector3f tangent, Vector3f up) {
		
		System.out.println(center);
		System.out.println(tangent);
		System.out.println(up);
		System.out.println();
		
		this.center = center;
		Vector3f right = new Vector3f().add(tangent).cross(up).normalize();
		
		top = new Vector3f(up).mul(RADIUS).add(center);
		bottom = new Vector3f(up).mul(-RADIUS).add(center);
		
		tTop = new Vector3f(right).normalize();
		tBottom = new Vector3f(right).mul(-1).normalize();
		
		float intensity = (float) Math.sqrt(RADIUS*2);
		
		tT0 = -intensity;
		tT1 = intensity;			
		
		tB0 = intensity;			
		tB1 = -intensity;			
	}
	
	public Vector3f top() {
		return top;
	}
	
	public Vector3f bottom() {
		return bottom;
	}
	
	public Vector3f topLeftTangent() {
		return new Vector3f(tTop).mul(tT0).add(top);
	}
	public Vector3f topRightTangent() {
		return new Vector3f(tTop).mul(tT1).add(top);
	}
	public Vector3f botLeftangent() {
		return new Vector3f(tBottom).mul(tB0).add(bottom);
	}
	public Vector3f botRightTangent(){
		return new Vector3f(tBottom).mul(tB1).add(bottom);
	}
	
}

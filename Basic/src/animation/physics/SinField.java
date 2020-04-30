package animation.physics;

import org.joml.Vector3f;

public class SinField extends ForceField{
	@Override
	public Vector3f accelerate(float delta, Vector3f point) {
		float theta = (float) ((2 * Math.PI * point.x + 2) / 7 );
		float phi = (float) ((2 * Math.PI * point.z +5) / 10 );
		
		Vector3f force = new Vector3f((float)Math.sin(theta), 0, (float) Math.cos(phi)).mul(delta * 0.03f);
		return force;
	}
	
}

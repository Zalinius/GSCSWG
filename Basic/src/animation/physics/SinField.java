package animation.physics;

import org.joml.Vector3f;

public class SinField extends ForceField{

	@Override
	public void accelerate(float delta, Vector3f point) {
		accelerate(delta, point, 1);
	}

	@Override
	public void accelerate(float delta, Vector3f point, float mass) {
		float theta = (float) ((2 * Math.PI * point.x) / 5 );
		float phi = (float) ((2 * Math.PI * point.z) / 5 );
		
		Vector3f force = new Vector3f((float)Math.sin(theta), 0, (float) Math.cos(phi)).mul(delta * 0.1f);
		point.add(force);
	}
	
}

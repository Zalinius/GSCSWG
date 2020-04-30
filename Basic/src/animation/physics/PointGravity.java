package animation.physics;

import org.joml.Vector3f;

public class PointGravity extends ForceField {
	
	private Vector3f center;
	private float mass;
	
	public PointGravity(Vector3f center, float mass) {
		this.center = center;
		this.mass = mass;
	}

	@Override
	public Vector3f accelerate(float delta, Vector3f point) {
		Vector3f force = new Vector3f(center).sub(point).normalize();
		float distance = Math.max(1f, center.distance(point));
		force.mul(this.mass * delta).div((float) Math.pow(distance, 1.4));
		return force;
	}

}

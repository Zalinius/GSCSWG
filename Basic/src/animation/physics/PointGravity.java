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
	public void accelerate(float delta, Vector3f point) {
		accelerate(delta, point, 1f);
	}
	@Override
	public void accelerate(float delta, Vector3f point, float mass) {
		Vector3f force = new Vector3f(center).sub(point).normalize();
		float distance = Math.max(1f, center.distanceSquared(point));
		force.mul(this.mass * delta).div(distance);
		point.add(force);
	}

}

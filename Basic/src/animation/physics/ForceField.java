package animation.physics;

import java.util.Iterator;

import org.joml.Vector3f;

public abstract class ForceField {
	/**
	 * @param delta
	 * @param point
	 * @param mass
	 * @return The change in velocity
	 */
	public abstract Vector3f accelerate(float delta, Vector3f point);
	
	public void applyForceField(Iterable<Vector3f> points, Iterable<Vector3f> velocities, float delta) {
		Iterator<Vector3f> velIt = velocities.iterator();
		for (Iterator<Vector3f> it = points.iterator(); it.hasNext();) {
			Vector3f point = it.next();
			Vector3f velocity = velIt.next();
			velocity.add(accelerate(delta, point));
		}
	}
}

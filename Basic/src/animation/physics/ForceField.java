package animation.physics;

import java.util.Iterator;

import org.joml.Vector3f;

public abstract class ForceField {
	public abstract void accelerate(float delta, Vector3f point);
	public abstract void accelerate(float delta, Vector3f point, float mass);
	
	public void applyForceField(Iterable<Vector3f> points, float delta) {
		for (Iterator<Vector3f> it = points.iterator(); it.hasNext();) {
			Vector3f point = it.next();
			accelerate(delta, point);
		}
	}
}

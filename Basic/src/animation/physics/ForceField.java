package animation.physics;

import org.joml.Vector3f;

public abstract class ForceField {
	public abstract void accelerate(float delta, Vector3f point);
	public abstract void accelerate(float delta, Vector3f point, float mass);
	
	public void applyForceField(Vector3f[][] points, float delta) {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				accelerate(delta, points[i][j]);
				if(i == 0 && j == 0) {
					System.out.println(points[i][j]);
				}
			}
		}
	}
}

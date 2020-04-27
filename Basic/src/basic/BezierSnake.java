package basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;

import model.BezierCylinder2;

public class BezierSnake extends BezierSurface{
	List<BezierCylinder2> snake;
	
	
	public BezierSnake() {
	}
	
	public List<Vector3f> initializePatchData(){
		snake = new ArrayList<>();

		List<Vector3f> snakePoints = new ArrayList<>();
		snakePoints.add(new Vector3f(8, 0, 8));
		snakePoints.add(new Vector3f(10, 0, 0));
		snakePoints.add(new Vector3f(8, 0,-8));
		snakePoints.add(new Vector3f(0, 0, -10));
		snakePoints.add(new Vector3f(-8, 0, -8));
		snakePoints.add(new Vector3f(-10, 0, 0));
		snakePoints.add(new Vector3f(-8, 0, 8));
		snakePoints.add(new Vector3f(0, 0, 10));
		
		
		for(int i = 0; i != snakePoints.size(); ++i) {
			Vector3f p0 = snakePoints.get((i) % snakePoints.size());
			Vector3f p1 = snakePoints.get((i+1) % snakePoints.size());
			Vector3f p2 = snakePoints.get((i+2) % snakePoints.size());
			Vector3f p3 = snakePoints.get((i+3) % snakePoints.size());
			snake.add(new BezierCylinder2(p0, p1, p2, p3));
		}
		
		return patchData();

	}

	@Override
	public List<Vector3f> patchData() {
		List<Vector3f> patchData = new ArrayList<>();
		for (Iterator<BezierCylinder2> it = snake.iterator(); it.hasNext();) {
			BezierCylinder2 bezierCylinder2 = it.next();
			patchData.addAll(bezierCylinder2.patchData());
		}
		
		return patchData;
	}
	
}

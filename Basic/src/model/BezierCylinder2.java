package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class BezierCylinder2 {

	
	private RenderableObject model;
	private RenderableObject pointsModel;
	
	private BezierCircle c0, c1, c2, c3;

	
	/** 
	 * A Bezier cylinder controlled by a catmull-rom spline
	 * @param p0 control
	 * @param p1 point
	 * @param p2 point
	 * @param p3 control
	 */
	public BezierCylinder2(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		Vector3f knot1, knot2, ctrl1, ctrl2;
		knot1 = p1;
		knot2 = p2;
		
		float t1 = 1.0f/3.0f;
		float t2 = 2.0f/3.0f;
		CatmullRomSpline cr = new CatmullRomSpline(p0, p1, p2, p3);
		ctrl1 = cr.position(t1);
		ctrl2 = cr.position(t2);
		
		Vector3f up = new Vector3f(0,1,0);

		c0 = new BezierCircle(knot1, cr.tangent(0), up);
		c1 = new BezierCircle(ctrl1, cr.tangent(t1),up);
		c2 = new BezierCircle(ctrl2, cr.tangent(t2),up);
		c3 = new BezierCircle(knot2, cr.tangent(1), up);
		

		model = RenderableObject.bezierCompositeSurface(patchData());
		pointsModel = RenderableObject.setupPointCloud(patchData());		

	}
	
	public List<Vector3f> patchData(){
		List<Vector3f> patchData = new ArrayList<>();
		
		patchData.add(c0.top());
		patchData.add(c1.top());
		patchData.add(c2.top());
		patchData.add(c3.top());
		patchData.add(c0.topLeftTangent());
		patchData.add(c1.topLeftTangent());
		patchData.add(c2.topLeftTangent());
		patchData.add(c3.topLeftTangent());
		patchData.add(c0.botLeftangent());
		patchData.add(c1.botLeftangent());
		patchData.add(c2.botLeftangent());
		patchData.add(c3.botLeftangent());
		patchData.add(c0.bottom());
		patchData.add(c1.bottom());
		patchData.add(c2.bottom());
		patchData.add(c3.bottom());
		
		patchData.add(c0.bottom());
		patchData.add(c1.bottom());
		patchData.add(c2.bottom());
		patchData.add(c3.bottom());
		patchData.add(c0.botRightTangent());
		patchData.add(c1.botRightTangent());
		patchData.add(c2.botRightTangent());
		patchData.add(c3.botRightTangent());
		patchData.add(c0.topRightTangent());
		patchData.add(c1.topRightTangent());
		patchData.add(c2.topRightTangent());
		patchData.add(c3.topRightTangent());
		patchData.add(c0.top());
		patchData.add(c1.top());
		patchData.add(c2.top());
		patchData.add(c3.top());
		
		return patchData;
	}
	
	
	public RenderableObject getModel() {
		return model;
	}
	
	public RenderableObject getPoints() {
		return pointsModel;
	}

	
	public static BezierCylinder2 prototype() {
		return new BezierCylinder2(new Vector3f(10,0,15),new Vector3f(-5,0,5), new Vector3f(5,0,-5), new Vector3f(-10,0,-15));
	}
}

package basic;

import java.util.List;

import org.joml.Vector3f;

import model.RenderableObject;

public abstract class BezierSurface {
	
	private RenderableObject model;
	private RenderableObject pointsModel;
	
	public BezierSurface() {
		model = RenderableObject.bezierCompositeSurface(initializePatchData());
		pointsModel = RenderableObject.setupPointCloud(patchData());	
	}

	public RenderableObject getModel() {
		return model;
	}
	
	public RenderableObject getPoints() {
		return pointsModel;
	}

	public abstract List<Vector3f> initializePatchData();
	public abstract List<Vector3f> patchData();
}

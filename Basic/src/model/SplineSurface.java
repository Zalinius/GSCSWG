package model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class SplineSurface {
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	public SplineSurface(RenderableObject surface, RenderableObject points) {
		this.SURFACE = surface;
		this.POINTS = points;
	}

}

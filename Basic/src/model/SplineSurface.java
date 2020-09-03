package model;

public class SplineSurface {
	public final RenderableObject SURFACE;
	public final RenderableObject POINTS;
	
	public SplineSurface(RenderableObject surface, RenderableObject points) {
		this.SURFACE = surface;
		this.POINTS = points;
	}

}

package basic;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class BicubicBezierSurface{
	
	private Vector3f[][] points;
	private final int WIDTH, HEIGHT;
	private final static int DEGREE = 3;
	private final static int PATCH_SIZE = (DEGREE + 1) * (DEGREE + 1);
	
	public BicubicBezierSurface(int width, int height) {
		if(!checkSizeIsValid(width)) {
			throw new RuntimeException("Bicubic Bezier surface requires width satisfy 3n+1, but width was: " + width);
		}
		else if(!checkSizeIsValid(height)) {
			throw new RuntimeException("Bicubic Bezier surface requires height satisfy 3n+1, but height was: " + width);
		}
		
		this.WIDTH = width;
		this.HEIGHT = height;
		
		points = new Vector3f[width][];
		for(int i = 0; i != width; ++i) {
			points[i] = new Vector3f[height];
			
			for(int j = 0; j != height; ++j) {
				float depth = (float) Math.log(i+j);
				points[i][j] = new Vector3f(i,j,depth);
			}
		}
	}
	
	
	/**
	 * Creates a 1D array, such that every 16 points corresponds to 1 patch.
	 * @return
	 */
	public Vector3f[] collapsePointsIntoPatches() {
		int numberOfPatches = ((WIDTH - 1) / 3) * ((HEIGHT - 1) / 3);
		Vector3f[] pointsAsPatches = new Vector3f[numberOfPatches * PATCH_SIZE];
		
		for(int t = 0; t != pointsAsPatches.length; ++t) {
			int patchNumber = t / PATCH_SIZE;        //Patch number, within [0, numberOfPatches]
			int relativePointIndex = t % PATCH_SIZE; //Position within the patch, within [0,15]		
			
			Vector2i relativePosition = new Vector2i(relativePointIndex / 4, relativePointIndex % 4); //Coordinate within a patch
			int pHeight = HEIGHT / DEGREE; // the width and height of the patch matrix
			int pWidth = WIDTH / DEGREE;
			Vector2i patchPosition = new Vector2i(patchNumber / pHeight, patchNumber % pHeight);   //Coordinates of a patch, relative to all other patches. Each unit is 1 patch wide
			
			int i = relativePosition.x + patchPosition.x * (4*4)*pHeight;
			int j = relativePosition.y + patchPosition.y * (4);
			
			System.out.println("i: " + i + ", j: " + j + " gives t: " + t);
			System.out.println(patchPosition);
			pointsAsPatches[t] = points[i][j];
		}
		
		return pointsAsPatches;
	}
	
	
	private boolean checkSizeIsValid(int size) {
		return (size % DEGREE == 1);
	}
}

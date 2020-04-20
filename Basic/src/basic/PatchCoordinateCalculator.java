package basic;

import org.joml.Vector2i;

public class PatchCoordinateCalculator {
	private final int DEGREE;  // The degree of the polynomial. A patch will have (DEGREE+1)^2 points
	private final int WIDTH;
	private final int HEIGHT;
	
	public PatchCoordinateCalculator(int degree, int width, int height) {
		this.DEGREE = degree;
		
		if(!checkSizeIsValid(width)) {
			throw new RuntimeException("Bicubic Bezier surface requires width satisfy 3n+1, but width was: " + width);
		}
		else if(!checkSizeIsValid(height)) {
			throw new RuntimeException("Bicubic Bezier surface requires height satisfy 3n+1, but height was: " + width);
		}
		
		this.WIDTH = width;
		this.HEIGHT = height;		
	}
	
	private boolean checkSizeIsValid(int size) {
		return (size % DEGREE == 1);
	}
	
	//Whether the collapsed index has a corresponding point, give this calculator's parameters
	public boolean isInRange(int t) {
		return t >= 0 && t < size();		
	}
	public boolean isInRange(int i, int j) {
		return (i >= 0 && i < WIDTH)  &&  (j >= 0 && j < HEIGHT);
	}
	
	public int getIndexFromCoordinate(Vector2i coordinate) {
		return 0;
	}
	
	public Vector2i getCoordinateFromIndex(int index) {
		return null;
	}
	
	
	
	public int size() {
		return WIDTH * HEIGHT;
	}
}


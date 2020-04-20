package dataStructures;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import basic.PatchCoordinateCalculator;

public class PatchCordinateCalculatorTest {

	@Test
	void calculator_allCoordinatesUpToSize_areInRange() {
		PatchCoordinateCalculator calc = bicubicBezierCalculator10x10();
		
		for(int i = 0; i != calc.size(); ++i) {
			boolean result = calc.isInRange(i);
			assertTrue(result);
		}
	}
	
	@Test
	void calculator_coordinatesBeforeAndAfterRange_areOutOfRange() {
		PatchCoordinateCalculator calc = bicubicBezierCalculator10x10();
		
		boolean negativeOneResult = calc.isInRange(-1);
		boolean outOfRangeResult = calc.isInRange(calc.size()+1);
		
		assertFalse(negativeOneResult);
		assertFalse(outOfRangeResult);
	}
	
	
	public PatchCoordinateCalculator bicubicBezierCalculator10x10() {
		return new PatchCoordinateCalculator(3, 10, 10);
	}

}

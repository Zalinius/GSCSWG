import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import basic.Grid;

public class GridTest {

	@Test
	void freshGrid_isEmpty_true() {
		Grid<Integer> grid = new Grid<>();
		
		boolean result = grid.isEmpty();
		
		assertTrue(result);
	}

	@Test
	void freshGrid_size_0() {
		Grid<Integer> grid = new Grid<>();
		
		int result = grid.size();
		
		assertEquals(0, result);
	}
	

	@Test
	void gridWith5Elements_size_5() {
		Grid<Integer> grid = new Grid<>();
		
		grid.put(0, 0, 0);
		grid.put(1, 1, 1);
		grid.put(2, 2, 2);
		grid.put(3, 3, 3);
		grid.put(4, 4, 4);
		int result = grid.size();
		
		assertEquals(5, result);
	}
	
}

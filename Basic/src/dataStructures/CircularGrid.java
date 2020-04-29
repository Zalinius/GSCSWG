package dataStructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularGrid<E> implements Iterable<E> {
	public final int HEIGHT;
	public final int WIDTH;
	
	private Object[][] grid;
	
	public CircularGrid(int width, int height) {
		HEIGHT = width;
		WIDTH = height;
		
		grid = new Object[width][];
		
		for (int i = 0; i < grid.length; i++) {
			grid[i] = new Object[height];
		}
	}
	
	
	public E get(int i, int j) {
		i = modulo(i, HEIGHT);
		j = modulo(j, WIDTH);
		
		return (E) grid[i][j];
	}
	
	public void set(int i, int j, E e) {
		i = modulo(i, HEIGHT);
		j = modulo(j, WIDTH);
		
		grid[i][j] = e;
	}
	
	public static int modulo(int dividend, int divisor) {
		int modulo = dividend % divisor;
		if( dividend >=0) {
			return modulo;
		}
		else {
			if(modulo < 0) {
			return modulo + divisor;
			}
			else {
				return modulo;
			}
		}
	}


	@Override
	public Iterator<E> iterator() {
		return new GridIterator();
	}
	
	private class GridIterator implements Iterator<E>{
		
		private int i, j;
		
		public GridIterator() {
			this.i = 0;
			this.j = 0;
		}

		@Override
		public boolean hasNext() {
			return j != WIDTH;
		}

		@Override
		public E next() {
			
			if(j == WIDTH) {
				throw new NoSuchElementException();
			}
			
			E value = get(i, j);
					
			++i;
			if(i == HEIGHT) {
				i = 0;
				++j;
			}

			return value;
		}
		
	}
	
}

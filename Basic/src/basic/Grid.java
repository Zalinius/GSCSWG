package basic;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Vector2i;

/**
 * An infinite 2D grid, which extends in every direction. At any point in the grid, a single object can be stored.
 * @author Zach
 *
 * @param <E> The type to be stored, on which there is no restriction..
 */
public class Grid<E> {
	Map<Vector2i, E> grid;

	public Grid() {
		grid = new HashMap<>();
	}
	
	/**
	 * Inserts an element into the grid, at (i,j)
	 * @param i 
	 * @param j
	 * @param element
	 * @return The element previously at (i,j), or null if the point was empty.
	 */
	public E put(int i, int j, E element) {
		E oldValue = grid.put(new Vector2i(i, j), element);
		return oldValue;
	}
	
	public int size() {
		return grid.size();
	}
	
	public E get(int i, int j) {
		return grid.get(new Vector2i(i, j));
	}
	
	
	public void clear() {
		grid.clear();
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}

	public Iterator<E> elements() {
		return grid.values().iterator();
	}
	
	public Iterator<Vector2i> points() {
		return grid.keySet().iterator();
	}
	
	public Iterator<Entry<Vector2i,E>> tiles(){
		return grid.entrySet().iterator();
	}

}

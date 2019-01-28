package maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
	// Mode of generation
	public static final int GEN_RANDOM = 0, GEN_LONG = 1;
	private final int MODE;

	// Size
	private final int size;
	// Maze generated
	private final Tile[][] tiles;
	// Random
	private final Random rand = new Random();

	// Size constructor
	public MazeGenerator(int size, int mode) {
		// Initialize vars
		this.size = size;
		this.MODE = mode;
		tiles = new Tile[size][size];
		// Iterate until there are no more empty coordinates
		ArrayList<Point> emptyLocs = new ArrayList<Point>();
		do {
			// Refresh all empty points
			emptyLocs.clear();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (tiles[i][j] == null) {
						emptyLocs.add(new Point(i, j));
					}
				}
			}
			if (!emptyLocs.isEmpty()) {
				// Iterate depth first pathing on random empty location
				depthPath(emptyLocs.get(rand.nextInt(emptyLocs.size())));
				// TODO: Maybe only need to call above on a random location. Find out.
			}

		} while (!emptyLocs.isEmpty());

		if (MODE == GEN_RANDOM) {
			// Set 2 random as start and end
			Tile selected = tiles[rand.nextInt(size)][rand.nextInt(size)];
			selected.setStartTile();
			do {
				selected = tiles[rand.nextInt(size)][rand.nextInt(size)];
			} while (selected.getStartTile());
			selected.setEndTile();
		}
	}

	private void depthPath(Point startloc) {
		// Initiate location if null
		if (tiles[startloc.x][startloc.y] == null) {
			tiles[startloc.x][startloc.y] = new Tile(startloc);
		}
		// Place starting
		if (MODE == GEN_LONG) {
			tiles[startloc.x][startloc.y].setStartTile();
		}
		// Keep track of endings in GEN_LONG mode
		HashMap<Integer, Point> endings = new HashMap<Integer, Point>();
		// Create stack for depth
		Stack<Point> locs = new Stack<Point>();
		locs.push(startloc);
		// Loop until iterated
		while (!locs.isEmpty()) {
			// Get current point
			Point cur = locs.peek();
			Point neighbor = getValidNeighbor(cur);
			if (neighbor == null) {
				// None found. Iterate backwards
				locs.pop();
				// Add ending if in long mode
				if (MODE == GEN_LONG) {
					endings.put(locs.size(), cur);
				}
			} else {
				// Found one. Add point to stack, create a tile, and connect previous
				locs.push(neighbor);
				tiles[neighbor.x][neighbor.y] = new Tile(neighbor, cur);
				tiles[cur.x][cur.y].addLink(neighbor);
			}
		}
		// Place ending
		if (MODE == GEN_LONG) {
			// Get longest
			int curMax = -1;
			Point curFarthest = null;
			// Check list for longest
			Iterator<Entry<Integer, Point>> it = endings.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Point> pair = it.next();
				// Check if longest
				if (pair.getKey() > curMax) {
					curMax = pair.getKey();
					curFarthest = pair.getValue();
				}
			}
			// End at point
			tiles[curFarthest.x][curFarthest.y].setEndTile();
		}
	}

	private Point getValidNeighbor(Point loc) {
		// Make a list of neighbors
		ArrayList<Point> neighbors = new ArrayList<Point>();
		// Check spots
		if (loc.x > 0 && tiles[loc.x - 1][loc.y] == null) {
			neighbors.add(new Point(loc.x - 1, loc.y));
		}
		if (loc.y > 0 && tiles[loc.x][loc.y - 1] == null) {
			neighbors.add(new Point(loc.x, loc.y - 1));
		}
		if (loc.x < size - 1 && tiles[loc.x + 1][loc.y] == null) {
			neighbors.add(new Point(loc.x + 1, loc.y));
		}
		if (loc.y < size - 1 && tiles[loc.x][loc.y + 1] == null) {
			neighbors.add(new Point(loc.x, loc.y + 1));
		}
		// Return null if none found
		if (neighbors.size() == 0) {
			return null;
		}
		// Return random one
		return neighbors.get(rand.nextInt(neighbors.size()));
	}

	// Get data generated
	public Tile[][] getTilemap() {
		return tiles;
	}
}

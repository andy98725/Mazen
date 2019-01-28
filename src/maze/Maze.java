package maze;

import java.awt.Graphics2D;
import java.awt.Point;

public class Maze {

	// Wid and hei of maze
	private int size;

	// Tile array
	private Tile[][] map;
	
	// Navigation has started
	private boolean hasStarted = false;
	private Point curLoc;

	// Construct by size
	public Maze(int wid) {
		size = wid;
		// Make array
		map = new Tile[size][size];
		// Make maze generator
		MazeGenerator gen = new MazeGenerator(size);
		map = gen.getTilemap();
	}

	// Draw current maze on scale
	public void draw(Graphics2D g) {
		// Scale to maze size
		double scale = 1.0 / (Tile.pxSize * size);
		g.scale(scale, scale);

		// Draw maze
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				map[i][j].draw(g);
			}
		}
	}
}

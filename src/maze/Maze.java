package maze;

import java.awt.Graphics2D;
import java.awt.Point;

import main.App;

public class Maze {

	// Iteration sleep time between jumps (in seconds)
	private static final double sleepTime = 0.05;
	// Wid and hei of maze
	private int size;

	// Tile array
	private Tile[][] map;

	// Navigation has started
	private boolean hasStarted = false;
	// Navication location
	private Point curLoc;
	// Tile to display choice (volatile so it updates instantly)
	volatile private Tile choiceTile;

	// Construct by size
	public Maze(int wid) {
		size = wid;
		// Make array
		map = new Tile[size][size];
		// Make maze generator
		MazeGenerator gen = new MazeGenerator(size, MazeGenerator.GEN_LONG);
		map = gen.getTilemap();
	}

	// Start navigating
	public void start() {
		if (hasStarted) {
			return;
		}
		hasStarted = true;
		// Determine current loc by starting tile
		Point startLoc = getStartingLoc();
		// Check if corner
//		if (map[startLoc.x][startLoc.y].getCorner()) {
		// Start iterating right away
		iterate(startLoc, startLoc);
//		} else {
//			// Present choice initially
//			setChoice(startLoc, map[startLoc.x][startLoc.y]);
//		}
	}

	private void iterate(Point loc, Point prevloc) {
		// Set no tile
		choiceTile = null;
		// Set location
		curLoc = loc;
		// Get current tile
		Tile tloc = map[loc.x][loc.y];
		// Handle visitation (prevloc usage)
		boolean visitState = tloc.getVisited();
		map[prevloc.x][prevloc.y].setVisited(!visitState);
		tloc.setVisited(true);
		// Check intersection
		if (tloc.getIntersection()) {
			// Set choice and end
			setChoice(map[loc.x][loc.y]);
			return;
		} else {
			// Pathway. First determine the 2 possible directions from the tile
			int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
			if (tloc.getOpenUp()) {
				x1 = 0;
				y1 = -1;
			}
			if (tloc.getOpenDown()) {
				if (x1 == 0 && y1 == 0) {
					x1 = 0;
					y1 = 1;
				} else {
					x2 = 0;
					y2 = 1;
				}
			}
			if (tloc.getOpenLeft()) {
				if (x1 == 0 && y1 == 0) {
					x1 = -1;
					y1 = 0;
				} else {
					x2 = -1;
					y2 = 0;
				}
			}
			if (tloc.getOpenRight()) {
				if (x1 == 0 && y1 == 0) {
					x1 = 1;
					y1 = 0;
				} else {
					x2 = 1;
					y2 = 0;
				}
			}
			// Make absolute
			x1 += loc.x;
			x2 += loc.x;
			y1 += loc.y;
			y2 += loc.y;

			// Then check that each direction has a different visit value. If equal, set
			// choice.
			if (map[x1][y1].getVisited() == map[x2][y2].getVisited()) {
				setChoice(tloc);
				return;
			}
			// Redraw
			App.disp.repaint();
			// Iterate in direction matching visit state
			if (map[x1][y1].getVisited() == visitState) {
				sleepIter(new Point(x1, y1), loc);
			} else {
				sleepIter(new Point(x2, y2), loc);
			}
		}
	}

	// Sleep iterate
	private void sleepIter(Point newpoint, Point oldpoint) {
		// Make sleep thread and run it
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Sleep current sleeptime in ms
				try {
					Thread.sleep((long) (1000 * sleepTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Iterate then
				iterate(newpoint, oldpoint);
			}
		}).start();
	}

	private void setChoice(Tile choice) {
		// Set choice tile
		choiceTile = choice;
		// Redraw
		App.disp.repaint();
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
		// Only draw if running:
		if (hasStarted) {
			// Draw choice
			if (choiceTile != null) {
				choiceTile.drawChoices(g, map);
			}
			// Draw player
			if (curLoc != null) {
				map[curLoc.x][curLoc.y].drawPlayer(g);
			}
		}
	}

	// Input direction
	public void inputDirection(int x, int y) {
		// Only do if a choice tile
		if (choiceTile == null) {
			return;
		}
		// Only do if a valid direction
		if (x < 0 && !choiceTile.getOpenLeft()) {
			return;
		}

		if (x > 0 && !choiceTile.getOpenRight()) {
			return;
		}
		if (y < 0 && !choiceTile.getOpenUp()) {
			return;
		}

		if (y > 0 && !choiceTile.getOpenDown()) {
			return;
		}
		// Okay, it's a valid direction. Iterate on it.
		iterate(new Point(choiceTile.getX() + x, choiceTile.getY() + y),
				new Point(choiceTile.getX(), choiceTile.getY()));
	}

	// Utilities
	private Point getStartingLoc() {
		// Loop through tiles
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (map[i][j].getStartTile()) {
					return new Point(i, j);
				}
			}
		}
		// Not found :/
		return null;
	}
}

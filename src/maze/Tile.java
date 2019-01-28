package maze;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Tile {
	// bitwise identities
	private static final int ID_UP = 1, ID_LEFT = 2, ID_RIGHT = 4, ID_DOWN = 8;

	// Size in pixels
	public static final int pxSize = 16;
	// Size of walls in pixels
	private static final int wallSize = 2;

	// Identity
	private int ID;
	// Location
	private int x, y;

	// Other properties
	// Is corner?
	private boolean isCorner = false;
	// Is intersection?
	private boolean isIntersection = false;
	// Start tile?
	private boolean startTile = false;
	// End tile?
	private boolean endTile = false;

	// Drawing colors
	private static final Color startcol = new Color(40, 215, 40);
	private static final Color endcol = new Color(215, 40, 40);
	private static final Color bgcol = new Color(40, 40, 40);
	private static final Color wallcol = new Color(215, 215, 215);

	// Empty tile constructor
	public Tile(Point loc) {
		this.ID = 0;
		this.x = loc.x;
		this.y = loc.y;
	}

	// Constructor from points
	public Tile(Point loc, Point from) {
		// Do empty constructor
		this(loc);
		// Add link
		addLink(from);
	}

	// Add link to location
	public void addLink(Point loc) {
		// Simple up check
		if (loc.y == y - 1) {
			// Add and return
			ID |= ID_UP;
			updateIntersection();
			return;
		}
		// Simple down check
		if (loc.y == y + 1) {
			// Add and return
			ID |= ID_DOWN;
			updateIntersection();
			return;
		}
		// Simple left check
		if (loc.x == x - 1) {
			// Add and return
			ID |= ID_LEFT;
			updateIntersection();
			return;
		}
		// Simple right check
		if (loc.x == x + 1) {
			// Add and return
			ID |= ID_RIGHT;
			updateIntersection();
			return;
		}
	}

	private void updateIntersection() {
		// Count amount of paths out of tile
		int pathCount = 0;
		if ((ID & ID_UP) != 0) {
			pathCount++;
		}
		if ((ID & ID_DOWN) != 0) {
			pathCount++;
		}
		if ((ID & ID_LEFT) != 0) {
			pathCount++;
		}
		if ((ID & ID_RIGHT) != 0) {
			pathCount++;
		}
		// Set states
		isCorner = pathCount < 2;
		isIntersection = pathCount > 2;
	}

	// Draw tile
	public void draw(Graphics2D g) {
		// Locations
		int xx = x * pxSize, yy = y * pxSize, ww = pxSize;
		int x2 = xx + ww - wallSize, y2 = yy + ww - wallSize;
		// Draw bg first
		g.setColor(bgcol);
		g.fillRect(xx, yy, ww, ww);
		// Draw start or end shape
		if (startTile) {
			g.setColor(startcol);
			g.fillRect(xx + wallSize, yy + wallSize, ww - 2 * wallSize, ww - 2 * wallSize);
		}
		if (endTile) {
			g.setColor(endcol);
			g.fillRect(xx + wallSize, yy + wallSize, ww - 2 * wallSize, ww - 2 * wallSize);
		}
		// Draw walls
		g.setColor(wallcol);
		// Draw corners
		g.fillRect(xx, yy, wallSize, wallSize);
		g.fillRect(x2, yy, wallSize, wallSize);
		g.fillRect(xx, y2, wallSize, wallSize);
		g.fillRect(x2, y2, wallSize, wallSize);
		// Draw any walls
		if ((ID & ID_UP) == 0) {
			g.fillRect(xx, yy, ww, wallSize);
		}
		if ((ID & ID_LEFT) == 0) {
			g.fillRect(xx, yy, wallSize, ww);
		}
		if ((ID & ID_DOWN) == 0) {
			g.fillRect(xx, y2, ww, wallSize);
		}
		if ((ID & ID_RIGHT) == 0) {
			g.fillRect(x2, yy, wallSize, ww);
		}
	}

	// Basic gets/sets
	public void setStartTile() {
		// Don't do if end tile
		if (endTile) {
			return;
		}
		startTile = true;
	}

	public boolean getStartTile() {
		return startTile;
	}

	public void setEndTile() {
		// Don't do if start tile
		if (startTile) {
			return;
		}
		endTile = true;
	}

	public boolean getEndTile() {
		return endTile;
	}

}

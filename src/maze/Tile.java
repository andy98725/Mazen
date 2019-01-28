package maze;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

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
	// Is visited?
	private boolean isVisited = false;
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
	private static final Color playcol = new Color(175, 175, 40); // TODO Tweak this color to match
	private static final Color arrowcol = new Color(40, 40, 215);
	private static final Color bgcol = new Color(40, 40, 40);
	private static final Color visitcol = new Color(127, 127, 127);
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
		// Draw background
		g.setColor(isVisited ? visitcol : bgcol);
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

	// Right arrow shape
	private static final Shape arrowR = new Polygon(
			new int[] { pxSize - pxSize / 4, pxSize - pxSize / 4, pxSize + pxSize / 4 },
			new int[] { -pxSize / 4, pxSize / 4, 0 }, 3);
	// Up arrow shape
	private static final Shape arrowD = AffineTransform.getRotateInstance(Math.PI / 2).createTransformedShape(arrowR);
	// Left arrow shape
	private static final Shape arrowL = AffineTransform.getRotateInstance(Math.PI / 2).createTransformedShape(arrowD);
	// Down arrow shape
	private static final Shape arrowU = AffineTransform.getRotateInstance(Math.PI / 2).createTransformedShape(arrowL);

	// Draw choices
	public void drawChoices(Graphics2D g, Tile[][] map) {
		// Save previous transform
		AffineTransform oldT = g.getTransform();
		// Transform to center
		g.transform(AffineTransform.getTranslateInstance(x * pxSize + pxSize/2, y * pxSize + pxSize/2));

		if (getOpenUp()) {
			Color borcol = map[x][y - 1].getVisited() ? bgcol : visitcol;
			g.setColor(arrowcol);
			g.fill(arrowU);
			g.setColor(borcol);
			g.draw(arrowU);
		}
		if (getOpenDown()) {
			Color borcol = map[x][y + 1].getVisited() ? bgcol : visitcol;
			g.setColor(arrowcol);
			g.fill(arrowD);
			g.setColor(borcol);
			g.draw(arrowD);
		}
		if (getOpenLeft()) {
			Color borcol = map[x - 1][y].getVisited() ? bgcol : visitcol;
			g.setColor(arrowcol);
			g.fill(arrowL);
			g.setColor(borcol);
			g.draw(arrowL);
		}
		if (getOpenRight()) {
			Color borcol = map[x + 1][y].getVisited() ? bgcol : visitcol;
			g.setColor(arrowcol);
			g.fill(arrowR);
			g.setColor(borcol);
			g.draw(arrowR);
		}
		// Restore transform
		g.setTransform(oldT);
	}

	// Draw player
	public void drawPlayer(Graphics2D g) {
		// Locations
		int xx = x * pxSize, yy = y * pxSize, ww = pxSize;
		// Draw player shape
		g.setColor(playcol);
		g.fillRect(xx + wallSize, yy + wallSize, ww - 2 * wallSize, ww - 2 * wallSize);

	}

	// Basic gets/sets

	public boolean getIntersection() {
		// Explanation: Still prompt user for corner.
		return isIntersection || isCorner;
	}

	public boolean getOpenUp() {
		return (ID & ID_UP) != 0;
	}

	public boolean getOpenLeft() {
		return (ID & ID_LEFT) != 0;
	}

	public boolean getOpenDown() {
		return (ID & ID_DOWN) != 0;
	}

	public boolean getOpenRight() {
		return (ID & ID_RIGHT) != 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getVisited() {
		return isVisited;
	}

	public void setVisited(boolean set) {
		isVisited = set;
	}

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

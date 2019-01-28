package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JComponent;

import maze.Maze;

public class MazeDisplay extends JComponent {
	private static final long serialVersionUID = 1L;
	// Component preferred dimensions
	public static final int prefWid = 512, prefHei = 512;
	// Component dimensions
	private final int wid, hei;
	// Maze dimensions
	private static final int minWid = 8, maxWid = 32;

	// Random generator
	private Random rand = new Random();
	// Current maze
	private Maze maze;
	// Current mode
	private static final int M_GEN = 0, M_PLAY = 1;
	private int MODE = M_GEN;

	// Initiate at dimensions
	public MazeDisplay(int wid, int hei) {
		// Set size
		this.wid = wid;
		this.hei = hei;
		setPreferredSize(new Dimension(wid, hei));
		setBounds(0, 0, wid, hei);
		// Make maze
		makeMaze();
	}

	// Gen a random maze
	private void makeMaze() {
		// Only generate in correct mode
		if (MODE == M_GEN) {
			// Random size
			int size = minWid + (int) Math.round(rand.nextDouble() * (maxWid - minWid));
			// Generate maze
			maze = new Maze(size);
			// Draw
			repaint();
		}
	}

	// Initiate play mode
	private void startPlay() {

	}

	// Paint the maze
	@Override
	public void paint(Graphics graphics) {
		// Get graphics2d
		Graphics2D g = (Graphics2D) graphics;
		// Anti aliasing
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Scale to 0-1
		g.scale(wid, hei);
		// Draw maze
		maze.draw(g);
	}

	// Use key
	public void keyPressed(int keycode) {
		// Generation mode key inputs
		if (MODE == M_GEN) {
			switch (keycode) {
			default: // None
				break;
			case KeyEvent.VK_SPACE:
				// Remake
				makeMaze();
				break;
			case KeyEvent.VK_ENTER:
				// Start play mode
				startPlay();
				break;
			}
		}
		// Play mode key inputs
		else if (MODE == M_PLAY) {
			switch (keycode) {
			default: // None
				break;
			// TODO
			}
		}
	}
}

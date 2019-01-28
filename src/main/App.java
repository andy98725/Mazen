package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import util.KeyInput;

public class App {

	// Frame for application
	private static JFrame frame;

	// Maze display active
	public static MazeDisplay disp;

	// Main method
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Make frame
				genFrame();
			}
		});
	}

	private static void genFrame() {
		// Make
		frame = new JFrame();
		// Make maze display and add
		final int wid = MazeDisplay.prefWid;
		final int hei = MazeDisplay.prefHei;
		disp = new MazeDisplay(wid, hei);
		frame.add(disp);
		// Let user select (to get key inputs)
		frame.setFocusable(true);
		// Add inputs (keys only)
		frame.addKeyListener(new KeyInput());
//		MouseInput mouse = new MouseInput();
//		frame.addMouseListener(mouse);
//		frame.addMouseMotionListener(mouse);
		// Resizable
		frame.setResizable(false);
		// Window header
		frame.setTitle("Mazen");
		// Close app on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Pack dimensions
		frame.pack();
		// Center
		frame.setLocationRelativeTo(null);
		// Set icon
		frame.setIconImage(makeIconImage());
		// Display
		frame.setVisible(true);
	}

	// Hardcoded icon
	private static BufferedImage makeIconImage() {
		BufferedImage ret = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		// Draw
		Graphics2D g = (Graphics2D) ret.getGraphics();
		// Filler image lol
		g.setColor(Color.GRAY);
		g.fillRect(16, 16, 32, 32);
		g.setColor(Color.WHITE);
		g.fillRect(24, 24, 16, 16);

		// Dispose and return
		g.dispose();
		return ret;
	}

}

package main;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import game.Game;
import game.GameStateListenerImpl;
import ui.UI;

public class Main {
	public static boolean DEBUG = false;

	public static Game GAME;

	public static final int PORT = 8723;
	public static final KeyAdapter KEY_ADAPTER = new KeyAdapter();

	public static void main(String args[]) throws InterruptedException {
		GAME = new Game(new GameStateListenerImpl());

		JFrame frame = new JFrame("Spacetime");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UI ui = new UI();
		ui.changeFrame(GAME);
		ui.setPreferredSize(new Dimension(UI.FIELD_WIDTH + 2 * UI.SIDE_BUFFER,
				UI.FIELD_HEIGHT + 2 * UI.SIDE_BUFFER + UI.TOP_BANNER));

		frame.add(ui);
		frame.pack();

		frame.setVisible(true);

		frame.addKeyListener(KEY_ADAPTER);

		boolean debugStep = true;
		while (true) {
			long start = System.currentTimeMillis();
			if (DEBUG) {
				if (KEY_ADAPTER.isKeyPressed(KeyEvent.VK_V)) {
					if (debugStep) {
						debugStep = false;
						GAME.update();
					}
				} else {
					debugStep = true;
				}
			} else {
				GAME.update();
			}
			ui.repaint();

			long timeElapsed = System.currentTimeMillis() - start;
//			System.out.printf("Debug: %d ms elapsed%n", timeElapsed);
			Thread.sleep(Math.max(0, 16 - timeElapsed));
		}
	}
}

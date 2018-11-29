package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import game.Game;
import ui.UI;

public class Main {
	public static final int PORT = 8723;
	public static final KeyAdapter KEY_ADAPTER = new KeyAdapter();

	public static void main(String args[]) throws InterruptedException {
		Game g = new Game();

		JFrame frame = new JFrame("Spacetime");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UI ui = new UI();
		ui.changeFrame(g);
		ui.setPreferredSize(new Dimension(UI.FIELD_WIDTH + 2 * UI.SIDE_BUFFER,
				UI.FIELD_HEIGHT + 2 * UI.SIDE_BUFFER + UI.TOP_BANNER));

		frame.add(ui);
		frame.pack();

		frame.setVisible(true);

		frame.addKeyListener(KEY_ADAPTER);

		while (true) {
			g.update();
			ui.repaint();

			Thread.sleep(16, 666);
		}
	}
}

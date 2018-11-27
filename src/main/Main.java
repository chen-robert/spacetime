package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import game.Game;
import ui.UI;

public class Main {
	public static void main(String args[]) {
		Game g = new Game();

		JFrame frame = new JFrame("Spacetime");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UI ui = new UI();
		ui.changeFrame(g);
		ui.setPreferredSize(new Dimension(480+2*UI.SIDE_BUFFER, 360+2*UI.SIDE_BUFFER+UI.TOP_BANNER));

		frame.add(ui);
		frame.pack();

		frame.setVisible(true);

		while (true) {
			g.update();
			ui.repaint();
		}
	}
}

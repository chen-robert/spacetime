package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

import javax.swing.JPanel;

import game.Game;

public class UI extends JPanel {
	private static final long serialVersionUID = 3279859667848651348L;

	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final int SIDE_BUFFER = 20;
	public static final int TOP_BANNER = 100;

	private Game currentFrame;

	public void changeFrame(Game g) {
		currentFrame = g;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (currentFrame == null)
			return;

		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		Collection<Renderable> items = currentFrame.getRenderables();
		for (Renderable item : items) {
			g.drawImage(item.getImg(), item.getX(), item.getY(), null);
		}
	}
}

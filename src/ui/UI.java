package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.PriorityQueue;

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
		
		Graphics2D g2d = (Graphics2D)g;//just for safety and more usable features

		g2d.setColor(BACKGROUND_COLOR);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		PriorityQueue<Renderable> items = new PriorityQueue<>((a, b) -> a.getRenderPriority() - b.getRenderPriority());
		items.addAll(currentFrame.getRenderables());
		for (Renderable item : items) {
			Image sprite = item.getImg();
			
			//the following code should deal with rotated sprites.
			g2d.translate(item.getX(), item.getY());
			g2d.rotate(-item.getDirectionRadians());
			g2d.drawImage(sprite, 0 - sprite.getWidth(null) / 2, 0 - sprite.getHeight(null) / 2,
					null);
			g2d.setTransform(new AffineTransform());
		}
	}
}

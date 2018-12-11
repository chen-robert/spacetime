package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import game.Game;

public class UI extends JPanel {
	private static final long serialVersionUID = 3279859667848651348L;

	public static final Color BACKGROUND_COLOR = new Color(210, 210, 240);
	public static final int SIDE_BUFFER = 20;
	public static final int TOP_BANNER = 100;
	public static final int FIELD_WIDTH = 480;
	public static final int FIELD_HEIGHT = 360;

	private Game currentFrame;

	public void changeFrame(Game g) {
		currentFrame = g;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (currentFrame == null) return;

		Graphics2D g2d = (Graphics2D) g;// just for safety and more usable
										// features

		g2d.setColor(BACKGROUND_COLOR);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		g2d.setColor(Color.WHITE);
		g2d.fillRect(UI.SIDE_BUFFER, UI.SIDE_BUFFER + UI.TOP_BANNER, UI.FIELD_WIDTH, UI.FIELD_HEIGHT);

		ArrayList<Renderable> items = new ArrayList<>(currentFrame.getRenderables());
		Collections.sort(items, (a, b) -> a.getRenderPriority() - b.getRenderPriority());

		for (Renderable item : items) {
			Image sprite = item.getImg();

			if (item.getDirectionRadians() == 0) {
				g2d.drawImage(sprite, SIDE_BUFFER + item.getRenderX() - sprite.getWidth(null) / 2,
						SIDE_BUFFER + TOP_BANNER + item.getRenderY() - sprite.getHeight(null) / 2, null);
			}

			else {
				// the following code should deal with rotated sprites.
				g2d.translate(SIDE_BUFFER + item.getRenderX(), SIDE_BUFFER + TOP_BANNER + item.getRenderY());
				g2d.rotate(-item.getDirectionRadians());
				// IMPORTANT: All sprites that are rotated MUST be square
				// Otherwise, we get some problems with alignment
				g2d.drawImage(sprite, 0 - sprite.getWidth(null) / 2, 0 - sprite.getHeight(null) / 2, null);
				g2d.setTransform(new AffineTransform());
			}
		}
	}
}

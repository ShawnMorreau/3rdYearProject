package View;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

/**
 * A node shape which is used to illustrate a named router from the simulation
 * on the view panel.
 * 
 * @author Aaron Bungay
 */
public class Node extends Ellipse2D.Float implements TopologyShape {

	private static final long serialVersionUID = -2541168903366055477L;
	public static final float DEFAULT_DIAMETER = 60f;

	private final Color FILL_COLOR = Color.decode("#87CEEB");
	private final Color STROKE_COLOR = Color.BLACK;
	private final Color STROKE_SELECTED_COLOR = Color.GREEN;
	private final Color TEXT_COLOR = Color.BLACK;
	private final Font NAME_FONT = new Font("Helvetica", Font.PLAIN, 20);

	private List<Message> messages;
	private String name;
	private float diameter;

	/**
	 * Constructor for the node shape(router)
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Node(float x, float y) {
		this(x, y, DEFAULT_DIAMETER);
	}

	/**
	 * Constructor for the node shape(router) with a diameter
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param diameter the custom diameter of the Node
	 */
	public Node(float x, float y, float diameter) {
		super(x, y, diameter, diameter);
		this.diameter = diameter;
		messages = new ArrayList<>();
	}

	/**
	 * Sets the name of the node
	 * @param name the name of the node to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the diameter of the node
	 */
	float getDiameter(){
		return diameter;
	}

	/**
	 * Sets the list of messages currently at the node
	 * @param messages the updated list of messages on this node
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Draws the node at its location on the screen and colours
	 * its outline if it is the currently selected shape.
	 */
	@Override
	public void draw(Graphics2D g2D, TopologyShape selected) {
		g2D.setColor(FILL_COLOR);
		g2D.fill(this);
		if (this.equals(selected)) {
			g2D.setColor(STROKE_SELECTED_COLOR);
		} else {
			g2D.setColor(STROKE_COLOR);
		}
		g2D.draw(this);
		g2D.setColor(TEXT_COLOR);
		drawName(g2D);
		drawMessages(g2D);
	}

	/**
	 * Displays the name of the node
	 * @param g2D the drawn node itself
	 */
	private void drawName(Graphics2D g2D) {
		if (name != null) {
			Font originalFont = g2D.getFont();

			g2D.setFont(NAME_FONT);
			FontMetrics metrics = g2D.getFontMetrics(NAME_FONT);
			float newX = x + (diameter - metrics.stringWidth(name)) / 2;
			float newY = y + (diameter - metrics.getHeight()) / 2 + metrics.getAscent();
			g2D.drawString(name, newX, newY);

			g2D.setFont(originalFont);
		}
	}

	/**
	 * Displays the messages currently held by the node
	 * @param g2D the drawn node itself
	 */
	private void drawMessages(Graphics2D g2D) {
		Stroke originalStroke = g2D.getStroke();

		g2D.setStroke(new BasicStroke(1));

		if (messages.isEmpty()) {
			g2D.setColor(Color.decode("#FFFFCC"));
			Rectangle2D.Float msgBox = new Rectangle2D.Float(x - 13f, y - 25f, 90f, 20f);
			g2D.fill(msgBox);
			g2D.setColor(Color.BLACK);
			g2D.draw(msgBox);
			g2D.drawString("No messages", x - 8f, y - 10f);
		} else {
			float msgXDist = 80f;
			float msgYDist = 10f;
			float boxXDist = 85f;
			float boxYDist = 25f;
			float boxHeight = 20f;
			float boxWidth = 230f;
			for (Message m : messages) {
				if (m.getDestination().getName().equals(name)) {
					g2D.setColor(Color.decode("#98FB98"));
				} else if (m.getSteps() == 0) {
					g2D.setColor(Color.decode("#FFCCFF"));
				} else {
					g2D.setColor(Color.decode("#FFCC99"));
				}
				Rectangle2D.Float msgBox = new Rectangle2D.Float(x - boxXDist, y - boxYDist, boxWidth, boxHeight);
				g2D.fill(msgBox);
				g2D.setColor(Color.BLACK);
				g2D.draw(msgBox);
				g2D.drawString("MSG " + m.getID() + ", SRC: "
						+ m.getSource().getName() + ", DEST: "
						+ m.getDestination().getName() + ", HOPS: "
						+ m.getSteps(), x - msgXDist, y - msgYDist);
				boxYDist += boxHeight;
				msgYDist += 20f;
			}
		}
		g2D.setStroke(originalStroke);
	}

	/**
	 * Equality is measured by the name of the nodes
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof Node)) {
			return false;
		}

		Node n = (Node) o;

		return getName().equals(n.getName());
	}
}
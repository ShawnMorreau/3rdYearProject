package View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * A connection shape which is used on the view panel to illustrate the neighbour
 * relationships of nodes.
 * 
 * @author Aaron Bungay, Jaspreet Sanghra
 */
public class Connection extends Line2D.Float implements TopologyShape {
	private static final long serialVersionUID = -4175152776834589880L;

	private final Color FILL_COLOR = Color.BLACK;
	private final Color FILL_SELECTED_COLOR = Color.GREEN;
	private Node node1;
	private Node node2;

	/**
	 * Constructor for the connection object
	 * @param node1 the first node to connect to
	 * @param node2 the second node to connect to
	 */
	public Connection(Node node1, Node node2) {
		super();
		this.node1 = node1;
		this.node2 = node2;
	}

	/**
	 * @return the first node in the connection
	 */
	public Node getNode1() {
		return node1;
	}

	/**
	 * @return the second node in the connection
	 */
	public Node getNode2() {
		return node2;
	}

	/**
	 * Draws a connection at its location on the screen and
	 * colours its outline if it is the currently selected
	 * shape.
	 */
	@Override
	public void draw(Graphics2D g2D, TopologyShape selected)
	{
		x1 = node1.x + node1.getDiameter()/2;
		y1 = node1.y + node1.getDiameter()/2;
		x2 = node2.x + node2.getDiameter()/2;
		y2 = node2.y + node2.getDiameter()/2;
		if (this.equals(selected)) {
			g2D.setColor(FILL_SELECTED_COLOR);
		} else {
			g2D.setColor(FILL_COLOR);
		}
		g2D.draw(this);
	}

	/**
	 * Equality for connections is determined by if the end nodes are
	 * the same, where node1 and node2 are interchangeable
	 */
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof Connection)) {
			return false;
		}

		Connection c = (Connection) o;

		return getNode1().equals(c.getNode1()) && getNode2().equals(c.getNode2()) || 
				getNode1().equals(c.getNode2()) && getNode2().equals(c.getNode1());
	}
}
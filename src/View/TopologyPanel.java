package View;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Model.NetworkTopology;
import Model.Router;
import Simulator.NetworkRoutingSimulatorController;

/**
 * The main jpanel of the network topology view which displays a canvas that allows the user to view
 * the routers and the connections (neighbours) between routers, and allows the user to move around the
 * routers to different positions on the screen
 * 
 * @author Aaron Bungay, Zaidoon Abd Al Hadi, Jaspreet Sanghra, Shawn Morreau
 */
public class TopologyPanel extends JPanel implements MouseListener, MouseMotionListener  {

	private static final String NETWORK_BACKGROUND_CLOUDS_RELATIVE_PATH = "/network-background-clouds.jpg";

	private static final long serialVersionUID = -7406613340247204821L;

	private NetworkRoutingSimulatorController controller;
	private TopologyShape selected;
	private List<Node> nodes;
	private List<Connection> connections;

	private List<Node> newNodes;
	private List<Connection> newConnections;

	private Node newConnectionNode1;
	private boolean isAddingConnection;
	private BufferedImage background; 

	/**
	 * Constructor of the Topology view
	 */
	public TopologyPanel() {
		nodes = new ArrayList<>();
		connections = new ArrayList<>();
		newNodes = new ArrayList<>();
		newConnections = new ArrayList<>();
		isAddingConnection = false;
		addMouseMotionListener(this);
		try {
			URL backgroundURL = getClass().getResource(NETWORK_BACKGROUND_CLOUDS_RELATIVE_PATH);
			background = ImageIO.read(backgroundURL);
			Dimension preferredSize = new Dimension(background.getWidth(), background.getHeight());
			this.setPreferredSize(preferredSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add new node to the list of nodes displayed on the topology view
	 * @param node the node to be added
	 */
	public void enqueueNewNode(Node node) {
		newNodes.add(node);
	}

	/**
	 * Add new connection to the list of connections displayed on the topology view
	 * @param connection the connection to be added
	 */
	private void enqueueNewConnection(Connection connection) {
		newConnections.add(connection);
	}

	/**
	 * @return returns the currently selected topology shape
	 */
	public TopologyShape getSelected() {
		return selected;
	}

	/**
	 * Clears the selected object
	 */
	public void clearSelected() {
		selected = null;
	}

	/**
	 * Updates the display to show the currently selected Connection clearly
	 * @param e the mouse event
	 * @return true if the connection display was updated
	 */
	private boolean connectionSelectedUpdate(MouseEvent e) {
		for (Connection c : connections) {
			if (c.ptSegDist(e.getPoint()) < 5) {
				System.out.println("**Connection " + c.getNode1().getName() + "<->" + c.getNode2().getName() + " was clicked on!");
				selected = c;
				repaint();
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the display to show the currently selected Node clearly
	 * @param e the mouse event
	 * @return true if the node display was updated
	 */
	private boolean nodeSelectedUpdate(MouseEvent e) {
		for (Node n : nodes) {
			if (n.contains(e.getPoint())) {
				System.out.println("**Node " + n.getName() + " was clicked on!");
				selected = n;
				repaint();
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets a flag signaling if a connection is about to be added
	 * @param val the boolean value to be set
	 */
	public void setIsAddingConnection(boolean val) {
		isAddingConnection = val;
	}

	/**
	 * Sets the reference to the controller
	 * @param cont the controller to be stored
	 */
	public void setController(NetworkRoutingSimulatorController cont) {
		if(cont != null) controller = cont;
	}

	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @return the connections
	 */
	public List<Connection> getConnections() {
		return connections;
	}

	/**
	 * Updates the view of the topology with only the shapes from
	 * a serialization (called after a 'restore' event from model)
	 * 
	 * @param sc
	 */
	public void update(ShapesContainer sc) {
		// set the nodes & connections to the serialized ones, this includes their saved
		// positions on the screen
		nodes = sc.getNodes();
		connections = sc.getConnections();

		// reset the selected state and queued new shapes for this panel since new shapes
		// were just force-loaded into the view
		clearSelected();
		setIsAddingConnection(false);
		newNodes = new ArrayList<>();
		newConnections = new ArrayList<>();

		repaint();
	}

	/**
	 * Updates the view of the topology normally
	 * 
	 * @param topology - the network
	 */
	public void update(NetworkTopology topology) {
		// update connections from neighbours of topology if they changed
		if (topology.isNeighboursChanged()) {
			Iterator<Connection> newConnectionIt = newConnections.iterator();
			while (newConnectionIt.hasNext()) {
				Connection c = newConnectionIt.next();
				if (topology.getNeighbours(c.getNode1().getName()).contains(topology.getRouter(c.getNode2().getName()))) {
					connections.add(c);
				}
				newConnectionIt.remove();
			}

			Iterator<Connection> connectionIt = connections.iterator();
			while (connectionIt.hasNext()) {
				Connection c = connectionIt.next();
				List<Router> router1Neighbours = topology.getNeighbours(c.getNode1().getName());
				if (router1Neighbours == null || !router1Neighbours.contains(topology.getRouter(c.getNode2().getName()))) {
					connectionIt.remove();
				}
			}
		}

		// update nodes from routers of topology if they changed
		if (topology.isRoutersChanged()) {
			Iterator<Node> newNodeIt = newNodes.iterator();
			while (newNodeIt.hasNext()) {
				Node n = newNodeIt.next();
				if (topology.contains(n.getName())) {
					nodes.add(n);
				}
				newNodeIt.remove();
			}

			Iterator<Node> nodeIt = nodes.iterator();
			while (nodeIt.hasNext()) {
				Node n = nodeIt.next();
				if (!topology.contains(n.getName())) {
					nodeIt.remove();
				}
			}
		}

		// update messages on all nodes from the topology routers
		for (Node n : nodes) {
			n.setMessages(topology.getRouter(n.getName()).getStoredMessages());
		}
		repaint();
	}

	/**
	 * Draws the background image, nodes, and connections to the panel view.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Enable anti-aliasing for smooth edges of drawn shapes and text
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2D.setStroke(new BasicStroke(3));

		if (background != null) {
			g2D.drawImage(background, 0, 0, null);
		}

		for(Connection c : connections) {
			c.draw(g2D, selected);
		}

		for (Node n : nodes) {
			n.draw(g2D, selected);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * Handles the mouse being clicked on the panel. Checks to see if a node
	 * was selected on the click, and updates the state accordingly.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse pressed.");
		if(isAddingConnection) {
			if(nodeSelectedUpdate(e)) {
				newConnectionNode1 = (Node) selected;
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else setIsAddingConnection(false);
		}
		else if(nodeSelectedUpdate(e)) return;
		else if(connectionSelectedUpdate(e)) return;
		else if (selected != null) {
			selected = null;
			repaint();
		}
	}

	/**
	 * Handles the mouse being released on the panel. Checks to see if the user released the mouse
	 * on another shape if a connection was being added, and adds a new connection if that was the case.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(isAddingConnection) {
			if(nodeSelectedUpdate(e)) {
				Node newConnectionNode2 = (Node) selected;
				Connection connection = new Connection(newConnectionNode1, newConnectionNode2);
				if(!connections.contains(connection)) {
					enqueueNewConnection(connection);
					controller.handleAddConnection(newConnectionNode1.getName(), newConnectionNode2.getName());
				}
			}
			setIsAddingConnection(false);
		}
		setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	/**
	 * Handles the mouse being dragged on the panel. If a node is currently selected
	 * then the node is moved around based on the mouse cursor.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!isAddingConnection && selected instanceof Node) {
			Node n = (Node) selected;
			n.x = e.getX() - n.getDiameter()/2;
			n.y = e.getY() - n.getDiameter()/2;
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			repaint();
		}
	}
}
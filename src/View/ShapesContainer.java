package View;

import java.util.List;

/**
 * A shapes container class to simply hold the node and connection
 * shapes for a serialization (XML) so that their positions can be
 * restored on the screen. This simplifies the serialization and
 * allows us to not have to serialize the entire view.
 * 
 * @author aaronbungay
 */
public class ShapesContainer {

	private List<Node> nodes;
	private List<Connection> connections;

	/**
	 * Create a shapes container with the given nodes and connections from the view
	 * 
	 * @param nodes
	 * @param connections
	 */
	public ShapesContainer(List<Node> nodes, List<Connection> connections) {
		this.nodes = nodes;
		this.connections = connections;
	}

	/**
	 * @return the nodes from the view
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @return the connections from the view
	 */
	public List<Connection> getConnections() {
		return connections;
	}

}

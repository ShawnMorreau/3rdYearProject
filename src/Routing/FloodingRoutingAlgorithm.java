package Routing;

import Model.NetworkTopology;
import Model.Router;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A FloodingRoutingAlgorithm is a routing algorithm which directs messages
 * from one router to all its neighboring routers in order to eventually
 * bring the message to its destination. A message will not revisit a router its
 * counter-part message has already visisted. Also, once a message reaches its destination,
 * the counter-part messages will continue to traverse the network until all routers have 
 * been visited. This algorithm sets the routing tables for each router in a network 
 * topology. The routing tables only need to be set once and can be reevaluated if the 
 * topology is altered.
 * 
 * @author Zaydoon
 * @version 1.0
 */
public class FloodingRoutingAlgorithm extends RoutingAlgorithm{

	public static final String NAME = "Flooding Routing Algorithm";

	/**
	 * Sends copy of message to all neighbours
	 */
	public FloodingRoutingAlgorithm() {
		super(true); // regenerate the routing tables every simulation step
	}

	/**
	 * Sets the routing tables for each router in a provided
	 * network topology using the flooding routing algorithm. Needs to be
	 * called only once and any time there is a change to the topology.
	 *
	 * @param topology - the network
	 */
	@Override
	protected void setTables(NetworkTopology topology) {

		Router[] routers = topology.getRouters().toArray(new Router[]{});

		// Set the routing table of each router in network topology
		for (int i = 0; i < routers.length ; i++) {
			HashMap<Router, HashSet<Router>> routingTable = new HashMap<>();
			List<Router> neighbours = topology.getNeighbours(routers[i].getName());
			int neighboursSize = neighbours.size();

			// Build routing table, adding all routers from topology as "To"
			// (message destination), and all neighbour routers as "Next"
			// (next router message should be transferred to)
			//
			for (int j = 0 ; j < routers.length ; j++) {
				if (i == j || neighboursSize == 0) {
					// Current router is the message destination or current
					// router has no neighbours, message does not go anywhere
					//
					routingTable.put(routers[j], null);
				} else {
					routingTable.put(routers[j], new HashSet<>(neighbours));
				}
			}

			// Set the completed routing table to the router
			//
			routers[i].setRoutingTable(routingTable);
		}

	}
}

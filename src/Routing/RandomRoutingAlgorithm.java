package Routing;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import Model.NetworkTopology;
import Model.Router;

/**
 * A RandomRoutingAlgorithm is a routing algorithm which directs messages
 * from one router to a random neighboring router in order to eventually
 * bring the message to its destination. This algorithm will set the routing
 * tables for each router in a network topology. The routing tables must be 
 * set before each simulation step while using this algorithm or else
 * it may be possible for a message to get stuck in a loop and never reach its
 * destination.
 * 
 * @author Aaron Bungay
 * @version 0.2
 */
public class RandomRoutingAlgorithm extends RoutingAlgorithm {

	public static final String NAME = "Random Routing Algorithm";

	/**
	 * Sends message to any random neighbour router
	 */
	public RandomRoutingAlgorithm() {
		super(true); // regenerate the routing tables every simulation step
	}

	/**
	 * Sets the routing tables for each router in a provided
	 * network topology using the random routing algorithm. Must be called
	 * before each simulation step or else a loop in the graph may exist and
	 * it could be possible for a message to never reach its destination.
	 * 
	 * @param topology - the network
	 */
	@Override
	protected void setTables(NetworkTopology topology) {
		// Create a new random number generator with current time as the seed
		Random random = new Random(System.currentTimeMillis());

		Router[] routers = topology.getRouters().toArray(new Router[]{});

		// Set the routing table of each router in network topology
		for (int i = 0; i < routers.length ; i++) {
			HashMap<Router, HashSet<Router>> routingTable = new HashMap<>();
			List<Router> neighbours = topology.getNeighbours(routers[i].getName());
			int neighboursSize = neighbours.size();

			// Build routing table, adding all routers from topology as "To"
			// (message destination), and a random neighbour router as "Next"
			// (next router message should be transferred to)
			for (int j = 0 ; j < routers.length ; j++) {
				if (i == j || neighboursSize == 0) {
					// Current router is the message destination or current
					// router has no neighbours, message does not go anywhere
					routingTable.put(routers[j], null);
				} else {
					// Generate index of a random neighbour
					int rndIndex = random.nextInt(neighboursSize);
					HashSet<Router> randomNeighbourSet = new HashSet<>();
					randomNeighbourSet.add(neighbours.get(rndIndex));
					// Store random neighbour as next router
					routingTable.put(routers[j], randomNeighbourSet);
				}
			}

			// Set the completed routing table to the router
			routers[i].setRoutingTable(routingTable);
		}
	}
}

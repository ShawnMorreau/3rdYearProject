package Routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Model.NetworkTopology;
import Model.Router;

/**
 * A BreadthFirstSearchRoutingAlgorithm is a routing algorithm which directs messages
 * from one router to a neighboring router in order to eventually
 * bring the message to its destination in the shortest amount of jumps. This algorithm sets 
 * the routing tables for each router in a network topology. The routing tables only need to 
 * be set once and can be reevaluated if the topology is altered.
 * 
 * @author Jaspreet Sanghra
 * @version 1.0
 */
public class BreadthFirstSearchRoutingAlgorithm extends RoutingAlgorithm {

	public static final String NAME = "Breadth First Routing Algorithm";
	private NetworkTopology currentTopology;

	/**
	 * key is the node in question, each node is only visited once
	 * value is the parent it came from
	 */
	private HashMap<Router, Router> path;

	/**
	 * AKA Shortest Path
	 */
	public BreadthFirstSearchRoutingAlgorithm() {
		super(false); // generate the routing tables once
	}

	/**
	 * Sets the routing tables for each router in a provided
	 * network topology using the breadth first search routing algorithm. 
	 * Needs to be called only once and any time there is a change to the 
	 * topology.
	 *
	 * @param topology - the network
	 */
	@Override
	protected void setTables(NetworkTopology topology) {
		currentTopology = topology;
		Router[] routers = topology.getRouters().toArray(new Router[]{});

		for (Router router : routers) {
			HashMap<Router, HashSet<Router>> routingTable = new HashMap<>();
			Router fromRouter = router; //the routing table of the router being set

			for (Router router1 : routers) {
				path = new HashMap<Router, Router>();
				Router toRouter = router1; //destination router
				setRoute(fromRouter, toRouter);
				HashSet<Router> set = getNextFromPath(toRouter);
				routingTable.put(toRouter, set);
			}
			fromRouter.setRoutingTable(routingTable);
		}
		topology = null;
	}

	/**
	 * Back tracks the path hashMap from destination as key, to find the
	 * value of the routing table hash map.
	 * @param destination the router trying to be reached via routing table
	 * @return a hashset of the next router to go to under BFS, null if path is empty
	 */
	private HashSet<Router> getNextFromPath(Router destination) {
		if(path.isEmpty())
			return null; //start and destination router are the same

		Router next = destination;
		//back track path
		while(path.containsKey(path.get(next)))
		{
			next = path.get(next);
		}

		HashSet<Router> set = new HashSet<>();
		set.add(next);
		return set;
	}

	/**
	 * Goes through a breadth first search of the topology from
	 * router start to destination while setting the path (hashMap)
	 * along the way, in order to back track it and find the 'next'/
	 * value router in the routing table
	 * @param start the router of which the routing table is being set
	 * @param destination the router trying to be reached
	 */
	private void setRoute(Router start, Router destination) {
		if(start.equals(destination))
			return; //already at destination

		Queue<Router> queue = new LinkedList<Router>();
		queue.add(start);
		while(!queue.isEmpty()) {
			Router parentRouter = queue.remove();
			Router childRouter= null;
			while((childRouter=getUnvisitedNeighbour(parentRouter, start))!=null) {
				// a router can only be visited once, childRouter and its parent
				// are added to the path
				//
				path.put(childRouter, parentRouter);
				queue.add(childRouter);
				if(childRouter == destination)
					return; //don't need to continue traversing the topology
			}
		}
		path = new HashMap<Router, Router>(); //destination was never found, possible disconnected graph
	}

	/**
	 * Returns an unvisited router that is a neighbour of currentRouter
	 * @param currentRouter the router currently being looked at
	 * @param start the router of which the routing table is being set
	 * @return an unvisited router, null otherwise
	 */
	private Router getUnvisitedNeighbour(Router currentRouter, Router start) {
		List<Router> neighbours = currentTopology.getNeighbours(currentRouter.getName());
		for (Router router : neighbours) {
			if(!path.containsKey(router) && router != start)
				return router; //router is unvisited
		}
		return null; //no more unvisited routers
	}
}

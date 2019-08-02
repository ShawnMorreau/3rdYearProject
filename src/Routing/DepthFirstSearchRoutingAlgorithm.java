package Routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import Model.NetworkTopology;
import Model.Router;

/**
 * A DepthFirstSearchRoutingAlgorithm is a routing algorithm which directs messages
 * from one router to a neighboring router in order to eventually
 * bring the message to its destination by using the least amount of memory. This algorithm sets 
 * the routing tables for each router in a network topology. The routing tables only need to 
 * be set once and can be reevaluated if the topology is altered.     
 * 
 * @author Shawn Morreau, Jaspreet
 * @version 1.0001
 */
public class DepthFirstSearchRoutingAlgorithm extends RoutingAlgorithm {

	public static final String NAME = "Depth First Routing Algorithm";
	private NetworkTopology topology;
	private Router source, destination;
	private List<Router> visitedRouters;
	private HashMap<Router, Router> backTrack;

	/**
	 * Default constructor. The topologyTables are only set once.
	 */
	public DepthFirstSearchRoutingAlgorithm() {
		super(false);
		visitedRouters = new ArrayList<Router>();
		backTrack = new HashMap<Router, Router>();
	}

	/**
	 * Sets up the routing tables for each router in the topology
	 * @param topology - the current topology being used
	 */
	@Override
	protected void setTables(NetworkTopology topology) {
		this.topology = topology;
		Router[] routers = topology.getRouters().toArray(new Router[] {});

		HashMap<Router, HashSet<Router>> routingTable;
		List<Router> neighbours;

		for (int i = 0; i < routers.length; i++) {
			routingTable = new HashMap<>();
			neighbours = topology.getNeighbours(routers[i].getName());

			source = routers[i];

			for (int j = 0; j < routers.length; j++) {
				backTrack = new HashMap<Router, Router>();
				if (i == j || (neighbours.size()) == 0) {
					// Current router is the message destination or current
					// router has no neighbours, message does not go anywhere

					routingTable.put(routers[j], null);
				} else {
					destination = routers[j];
					//this is the actual DFS method
					getRoute();
					HashSet<Router> r = goBack();
					// this router should send its message to this one.
					routingTable.put(destination, r);

				}

			}
			source.setRoutingTable(routingTable);
		}
	}

	/**
	 * Props to Jaspreet for helping me with this.
	 * 
	 * @return HashSet containing the router that the source router should forward it's message to.
	 */
	private HashSet<Router> goBack() {
		if(backTrack.isEmpty())
			return null; //start and destination router are the same

		Router next = destination;
		//back track path
		while(backTrack.containsKey(backTrack.get(next)))
		{
			next = backTrack.get(next);
		}

		HashSet<Router> set = new HashSet<>();
		set.add(next);
		return set;

	}

	/**
	 * Goes through the network pushing and popping routers from the stack
	 * The route will then be stored in an ArrayList named visitedRouters 
	 */
	private void getRoute() {
		ArrayList<Router> children = new ArrayList<Router>();
		if (source.equals(destination))
			return;

		Stack<Router> routerStack = new Stack<Router>();
		visitedRouters = new ArrayList<Router>();
		routerStack.add(source);
		while (!routerStack.isEmpty()) {
			Router current = routerStack.pop();
			if(current.equals(destination)){
				visitedRouters.add(current);
				return;

			}
			if (!visitedRouters.contains(current)) {
				visitedRouters.add(current);
				children = getChildren(current);
				if(children.contains(destination)) {
					visitedRouters.add(destination);
					backTrack.put(destination, current);
					return;
				}
				routerStack.addAll(children);
				for(Router r : children) {
					backTrack.put(r, current);
				}

			}
		}
		backTrack = new HashMap<Router, Router>();
	}

	/**
	 * Gets a list of the neighbours of the given router that have not been visited yet
	 * @param current : the router to look at
	 * @return a list of children that have not been visited
	 */
	private ArrayList<Router> getChildren(Router current) {
		List<Router> children = topology.getNeighbours(current.getName());
		ArrayList<Router> unidentifiedChildren = new ArrayList<Router>();
		for (Router router : children) {
			if (!visitedRouters.contains(router)) {
				unidentifiedChildren.add(router);
			}
		}
		return unidentifiedChildren;
	}

}

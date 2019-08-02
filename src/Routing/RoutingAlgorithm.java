package Routing;
import java.util.HashMap;
import java.util.Map;

import Model.NetworkTopology;

/**
 * A RoutingAlgorithm is used to determine the path a message must take to
 * reach its destination by setting the routing tables for each router in
 * a network topology.
 * 
 * @author Aaron Bungay, Jaspreet Sanghra
 * @version 0.2
 */
public abstract class RoutingAlgorithm {

	public static final Map<String, RoutingAlgorithm> INSTANCES;

	static {
		INSTANCES = new HashMap<>();
		INSTANCES.put(RandomRoutingAlgorithm.NAME, new RandomRoutingAlgorithm());
		INSTANCES.put(FloodingRoutingAlgorithm.NAME, new FloodingRoutingAlgorithm());
		INSTANCES.put(BreadthFirstSearchRoutingAlgorithm.NAME, new BreadthFirstSearchRoutingAlgorithm());
		INSTANCES.put(DepthFirstSearchRoutingAlgorithm.NAME , new DepthFirstSearchRoutingAlgorithm());
	}

	private boolean didSet;
	private boolean isResettable;

	/**
	 * Default constructor. The algorithm will only allow the
	 * routing tables of a topology to be set once, additional
	 * calls to setRoutingTables() will be ignored.
	 */
	public RoutingAlgorithm() {
		this(false);
	}

	/**
	 * If true is given as isResettable, this allows the routing
	 * algorithm to set the routing table of a topology multiple times.
	 * This is required for the RandomRoutingAlgorithm which requires
	 * the setting of routing tables before every simulation step, while
	 * the other routing algorithms only need to set the tables once.
	 * 
	 * @param isResettable - a boolean stating if routing tables can be overwritten
	 */
	public RoutingAlgorithm(boolean isResettable) {
		this.isResettable = isResettable;
		this.didSet = false;
	}

	/**
	 * Sets the routing table under the appropriate conditions
	 * @param topology - the network
	 */
	public void setRoutingTables(NetworkTopology topology) {
		// only set the routing tables to be set if this is a
		// resettable algorithm or this is the first time setting
		// the routing tables.
		if (!didSet || isResettable) {
			setTables(topology);
			didSet = true;
		}
	}

	/**
	 * Force the routing tables to be set, ignoring the fact that the tables
	 * may have already been set once before.
	 * @param topology - the network
	 */
	public void forceSetRoutingTables(NetworkTopology topology) {
		didSet = false;
		setRoutingTables(topology);
	}

	/**
	 * Sets the routing tables for each router in a provided
	 * network topology using a specific algorithm.
	 * @param topology - the network
	 */
	protected abstract void setTables(NetworkTopology topology);
}

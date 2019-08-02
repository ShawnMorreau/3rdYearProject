package Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Network Topology is an arrangement of routers (nodes) and the connections
 * between them, to other routers.
 * 
 * @author Shawn Morreau, Aaron Bungay, Zaidoon Abd Al Hadi, Jaspreet Sanghra
 * @version 0.3
 */
public class NetworkTopology implements Serializable{

	private static final long serialVersionUID = 241921490675238039L;
	private static final String LAMBDA = " -> ";
	private static final String NEW_LINE = "\n";
	private static final String COMMA_SPACE = ", ";
	private Map<Router, List<Router>> routerNeighbours;
	private boolean isNeighboursChanged;
	private boolean isRoutersChanged;

	// variables used for metrics (total packets transmitted and average hops)
	private int totalPacketsTransmittedMetric;
	private int numMessagesReachedDest;
	private float averageHopsMetric;

	/**
	 * Constructor for NetworkTopology
	 */
	public NetworkTopology() {
		routerNeighbours = new HashMap<Router, List<Router>>();
		isNeighboursChanged = false;
		isRoutersChanged = false;
		totalPacketsTransmittedMetric = 0;
		numMessagesReachedDest = 0;
		averageHopsMetric = 0;
	}

	/**
	 * Add a router to the topology
	 * @param router - the router to be added
	 */
	public void addRouter(Router router) {
		routerNeighbours.put(router, new ArrayList<Router>());
		isRoutersChanged = true;
	}

	/**
	 * Removes the router from the topology, if it exists. Also removes
	 * 	the reference of the router from the list of neighbors
	 * @param name the name of the router to remove
	 */
	public void removeRouter(String name) {
		Router target = getRouter(name);
		if (target != null) {
			routerNeighbours.remove(target);
			isRoutersChanged = true;
			for (Router router : routerNeighbours.keySet()) {
				List<Router> neighbours = routerNeighbours.get(router);
				if (neighbours.contains(target)) {
					neighbours.remove(target);
					isNeighboursChanged = true;
				}
			}
		}
	}

	/**
	 * Returns the router with 'name' if it exists
	 * @param name - the name of the router
	 * @return the router with the same name, otherwise null
	 */
	public Router getRouter(String name){
		for (Router router : routerNeighbours.keySet()){
			if (router.getName().equals(name)) return router;
		}
		return null;
	}

	/**
	 * Get the set of all unique routers that are in the topology
	 * @return all unique routers in topology
	 */
	public Set<Router> getRouters() {
		return routerNeighbours.keySet();
	}

	/**
	 * Returns a list of neighbours of the router with the given name
	 * @param name the name of the router to look at in the topology
	 * @return a list of neighbours if the router exists, otherwise null
	 */
	public List<Router> getNeighbours(String name){
		Router target = getRouter(name);
		if (target != null){
			return routerNeighbours.get(target);
		}
		return null;
	}

	/**
	 * Checks if the neighbors map has been updated and resets the flag to false
	 * @return true if the map of neighbors has changed, otherwise false
	 */
	public boolean isNeighboursChanged() {
		boolean isNeighboursChangedPrev = isNeighboursChanged;
		isNeighboursChanged = false;
		return isNeighboursChangedPrev;
	}

	/**
	 * Checks if the map of routers has been updated and resets the flag to false
	 * @return true if the map of routers has changed, otherwise false
	 */
	public boolean isRoutersChanged() {
		boolean isRoutersChangedPrev = isRoutersChanged;
		isRoutersChanged = false;
		return isRoutersChangedPrev;
	}

	/**
	 * Adds a neighbour to a router from the given router names
	 * @param routerName the name of the router to add a neighbour to
	 * @param routerNeighbourName the name of the neighbour to be added
	 * @return true if the neighbour is added, otherwise false
	 */
	public boolean setRouterNeighbour(String routerName, String routerNeighbourName) {
		Router router = getRouter(routerName);
		Router neighbour = getRouter(routerNeighbourName);
		return setRouterNeighbour(router, neighbour);
	}

	/**
	 * Sets a neighbour to the router
	 * @param router - the router to gain a neighbour
	 * @param neighbour - the neighbour to be added
	 * @return true if the neighbour is added successfully, otherwise false
	 */
	private boolean setRouterNeighbour(Router router, Router neighbour) {
		if(router != null && neighbour != null) {
			if(routerNeighbours.containsKey(router)) {
				List<Router> routers = routerNeighbours.get(router);
				if(!routers.contains(neighbour)) {
					routerNeighbours.get(router).add(neighbour);
					isNeighboursChanged = true;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the neighbor from the router's set of neighbors if it was found
	 * @param routerName the name of the router to lose a neighbor
	 * @param routerNeighbourName the name of the neighbor to be removed
	 * @return true if the neighbor is removed, otherwise false
	 */
	public boolean removeNeighbour(String routerName, String routerNeighbourName){
		Router router = getRouter(routerName);
		Router neighbour = getRouter(routerNeighbourName);
		if (router != null && neighbour != null && routerNeighbours.get(router).contains(neighbour)){
			routerNeighbours.get(router).remove(neighbour);
			isNeighboursChanged = true;
			return true;
		}
		return false;
	}

	/**
	 * Returns a comma separated string of the give list of routers
	 * 	ie. "a, b, c, d"
	 * @param neighbours - a list of routers
	 * @return string of the list of routers
	 */
	private String getNeighoursString(List<Router> neighbours) {
		StringBuilder sb = new StringBuilder();
		//test if neighbors is null
		for (Router routerNeighbour : neighbours) {
			if(sb.length() > 0){
				//append comma if not the first iteration
				sb.append(COMMA_SPACE);
			}
			sb.append(routerNeighbour.toString());
		}
		return sb.toString();
	}

	/**
	 * Removes all the messages from all the routers
	 */
	public void clearMessages(){
		for (Router router : routerNeighbours.keySet()) {
			router.removeAllMessages();
		}
	}

	/**
	 * Checks to see if the given router name already exists in the topology
	 * @param routerName the name of the router to be checked
	 * @return true if the router exists
	 */
	public boolean contains(String routerName) {
		Router target = getRouter(routerName);
		return getRouters().contains(target);
	}

	/**
	 * @return the number of routers in the topology
	 */
	public int numberOfRouters() {
		return routerNeighbours.keySet().size();
	}

	/**
	 * Returns the string of the complete topology
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Router, List<Router>> routerSet : routerNeighbours.entrySet()) {
			sb.append(routerSet.getKey().toString());
			sb.append(LAMBDA);
			sb.append(getNeighoursString(routerSet.getValue()));
			sb.append(NEW_LINE);
		}
		return sb.toString();
	}

	/**
	 * @return the current total packets transmitted metric
	 */
	public int getTotalPacketsTransmittedMetric() {
		return totalPacketsTransmittedMetric;
	}

	/**
	 * Increments the total packets transmitted metric
	 */
	public void incrementTotalPacketsTransmittedMetric() {
		totalPacketsTransmittedMetric++;
	}

	/**
	 * Increments the total packets that have reached their destination counter.
	 * This is used for the average hops metric.
	 */
	public void incrementNumMessagesReachedDest() {
		numMessagesReachedDest++;
	}

	/**
	 * Resets the variables used for metrics (total packets and average hops)
	 */
	public void resetMetrics() {
		totalPacketsTransmittedMetric = 0;
		numMessagesReachedDest = 0;
		averageHopsMetric = 0;
	}

	/**
	 * Calculates the average hops metric and stores it in averageHopsMetric field
	 */
	public void calculateAverageHopsMetric() {
		if (numMessagesReachedDest == 0) {
			averageHopsMetric = 0;
		} else {
			averageHopsMetric = (float) totalPacketsTransmittedMetric / (float) numMessagesReachedDest;
		}
	}

	/**
	 * @return the averageHopsMetric
	 */
	public float getAverageHopsMetric() {
		return averageHopsMetric;
	}

	/**
	 * Sets both the routers and neighbours changed flags to true
	 */
	public void setFlags()
	{
		isNeighboursChanged = true;
		isRoutersChanged = true;
	}
}

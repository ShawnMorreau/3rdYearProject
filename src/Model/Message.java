package Model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A Message is an object that is transferred between routers in a network topology. It has a
 * 	source router, a destination router it tries to reach, and an Id. It keeps metrics about
 * 	the number of hops it had to take to reach its destination router.
 *  
 * @author Zaidoon Abd Al Hadi, Aaron Bungay, Jaspreet Sanghra
 * @version 0.3
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370506275597815419L;
	private int id;
	private int steps;
	private Router source;
	private Router destination;
	private Router previousDestination;
	private HashSet<Router> routersPassedThrough;

	/**
	 * Constructor for a Message
	 * @param id the ID of the message
	 * @param source the router the message starts at
	 * @param destination the router the message is trying to get to
	 */
	public Message(int id, Router source, Router destination) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		steps = 0;
		previousDestination = null;
		routersPassedThrough = new HashSet<Router>();
	}

	/**
	 * Get the original starting location of the message
	 * @return the router the message started at
	 */
	public Router getSource() {
		return source;
	}

	/**
	 * Gets the previous Router the message was located at
	 * @return the previous location of the message
	 */
	public Router getPreviousDestination(){
		return previousDestination;
	}

	/**
	 * Sets the previous location of the message
	 * @param router the previous router the message was at
	 */
	public void setPreviousDestination(Router router){
		previousDestination = router;
	}

	/**
	 * Get the destination router
	 * @return the router the message is trying to reach
	 */
	public Router getDestination() {
		return destination;
	}

	/**
	 * @return the Id of the message
	 */
	public int getID(){
		return id;
	}

	/**
	 * @return the number of jumps the message has taken so far
	 */
	public int getSteps(){
		return steps;
	}

	/**
	 * Increments the value of the number of jumps the message has taken
	 */
	public void incrementSteps()
	{
		steps++;
	}

	/**
	 * Adds a router to the set of routers the message has passed through
	 * @param router the router to add to the set
	 */
	public void addRouter(Router router) {
		if (router != null ) routersPassedThrough.add(router);
	}

	/**
	 * The set of routers the message has already passed through
	 * @return a HashSet of routers
	 */
	public HashSet<Router> getRoutersPassedThrough() {
		return routersPassedThrough;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;

		return id == message.id;

	}

	@Override
	public String toString(){
		return String.valueOf(id);
	}
}

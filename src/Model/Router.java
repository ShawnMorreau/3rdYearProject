package Model;
import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *  A Router is a node in a network topology which stores messages, has neighbors,
 *  and has a routing table.
 *  
 * @author Zaidoon Abd Al Hadi, Aaron Bungay, Shawn Morreau, Jaspreet Sanghra
 * @version 0.2
 */
public class Router implements Serializable{

	private static final long serialVersionUID = 6288937073120421539L;
	private HashMap<Router, HashSet<Router>> routingTable;
	private ArrayList<Message> messages;
	private String name;

	/**
	 * Constructor for the Router
	 * @param name the name of the new router
	 */
	public Router(String name) {
		messages = new ArrayList<Message>();
		this.name = name;
	}

	/**
	 * Get the name of the Router
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a message in the router
	 * @param message - the message to be stored
	 */
	public void storeMessage(Message message) {
		if (message != null) {
			messages.add(message);
		}
	}

	/**
	 * Returns all the messages in the router
	 * @return	a list of the messages in the router
	 */
	public ArrayList<Message> getStoredMessages() {
		return messages;
	}

	/**
	 * Removes a message from the router with the sane Id
	 * @param message - the message to be removed
	 */
	public void removeMessage(Message message) {
		if(message != null) {
			for (int i =0; i < messages.size();i++){
				if (messages.get(i).getID() == message.getID()) messages.remove(i);
			}
		}
	}

	/**
	 * Removes all the messages currently in the router
	 */
	public void removeAllMessages(){
		messages = new ArrayList<Message>();
	}

	/**
	 * Returns the routing table of this router
	 * @return - the routing table
	 */
	public HashMap<Router, HashSet<Router>> getRoutingTable() {
		return routingTable;
	}

	/**
	 * Sets the routing table of this router
	 * @param routingTable - the routingTable to be set
	 */
	public void setRoutingTable(HashMap<Router, HashSet<Router>> routingTable) {
		this.routingTable = routingTable;
	}

	/**
	 * Returns the router's name, similar to getName().
	 */
	public String toString(){
		return name;
	}

	/**
	 * Equality for Routers is determined by their names being equivalent.
	 */
	public boolean equals(Object o) {
		if (o == this) return true;

		if (!(o instanceof Router)) {
			return false;
		}

		Router router = (Router) o;
		return this.getName().equals(router.getName());
	}
}

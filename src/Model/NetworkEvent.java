package Model;

import java.util.EventObject;

import View.ShapesContainer;

/**
 * An event object to notify the view of a change in the topology or,
 * if a restore is occurring, the topology panel
 * 
 * @author aaronbungay
 *
 */
public class NetworkEvent extends EventObject {

	private static final long serialVersionUID = -3045014715020094713L;

	private NetworkTopology topology;
	private ShapesContainer shapesContainer;
	private int messageCount;
	private int baseMessageCount;

	private boolean isRestoreEvent;

	/**
	 * Construct a new NetworkEvent for the purpose of only notifying of a topology change
	 * 
	 * @param source
	 */
	public NetworkEvent(Object source, NetworkTopology topology, int messageCount, int baseMessageCount) {
		this(source, topology, null, false, messageCount, baseMessageCount);
	}

	/**
	 * Construct a new NetworkEvent for the purpose of notifying of a topology change and topology panel change.
	 * Used for the purpose of notifying after a serialization (restore) occurs.
	 * 
	 * @param source
	 */
	public NetworkEvent(Object source, NetworkTopology topology, ShapesContainer shapesContainer, boolean isRestoreEvent,
			int messageCount, int baseMessageCount) {
		super(source);
		this.topology = topology;
		this.setShapesContainer(shapesContainer);
		this.isRestoreEvent = isRestoreEvent;
		this.messageCount = messageCount;
		this.baseMessageCount = baseMessageCount;
	}

	/**
	 * returns the number of messages that have been generated in the model
	 * @return
	 */
	public int getMessageCount() {
		return messageCount;
	}

	/**
	 * sets the number of messages that have been generated in the model
	 * @param messageCount
	 */
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	/**
	 * returns the number of messages that have been generated in the model
	 * @return
	 */
	public int getBaseMessageCount() {
		return baseMessageCount;
	}

	/**
	 * sets the number of messages that have been generated in the model
	 * @param messageCount
	 */
	public void setBaseMessageCount(int messageCount) {
		this.baseMessageCount = messageCount;
	}

	/**
	 * @return the topology
	 */
	public NetworkTopology getTopology() {
		return topology;
	}

	/**
	 * @param topology the topology to set
	 */
	public void setTopology(NetworkTopology topology) {
		this.topology = topology;
	}

	/**
	 * @return the isRestoreEvent
	 */
	public boolean isRestoreEvent() {
		return isRestoreEvent;
	}

	/**
	 * @param isRestoreEvent the isRestoreEvent to set
	 */
	public void setRestoreEvent(boolean isRestoreEvent) {
		this.isRestoreEvent = isRestoreEvent;
	}

	/**
	 * @return the shapes container for the view to update using
	 */
	public ShapesContainer getShapesContainer()
	{
		return shapesContainer;
	}

	/**
	 * @param shapesContainer for the view
	 */
	public void setShapesContainer(ShapesContainer shapesContainer)
	{
		this.shapesContainer = shapesContainer;
	}

}

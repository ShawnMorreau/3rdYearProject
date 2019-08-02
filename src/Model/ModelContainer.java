package Model;

import Routing.RoutingAlgorithm;

/**
 * A container class with the relevant fields of a model for serialization (XML)
 * 
 * @author aaronbungay
 */
public class ModelContainer {

	private NetworkTopology topology;
	private RoutingAlgorithm chosenRoutingAlgorithm;
	private float messageGenerationRate;
	private int messageCounter;
	private int stepCounter;

	/**
	 * Create a Model container with all relevant fields from a model for XML
	 * 
	 * @param topology
	 * @param alg
	 * @param msgGenRate
	 * @param msgCount
	 * @param stepCount
	 */
	public ModelContainer(NetworkTopology topology, RoutingAlgorithm alg, float msgGenRate, int msgCount, int stepCount) {
		this.topology = topology;
		this.chosenRoutingAlgorithm = alg;
		this.messageGenerationRate = msgGenRate;
		this.messageCounter = msgCount;
		this.stepCounter = stepCount;
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
	 * @return the chosenRoutingAlgorithm
	 */
	public RoutingAlgorithm getChosenRoutingAlgorithm() {
		return chosenRoutingAlgorithm;
	}

	/**
	 * @param chosenRoutingAlgorithm the chosenRoutingAlgorithm to set
	 */
	public void setChosenRoutingAlgorithm(RoutingAlgorithm chosenRoutingAlgorithm) {
		this.chosenRoutingAlgorithm = chosenRoutingAlgorithm;
	}

	/**
	 * @return the messageGenerationRate
	 */
	public float getMessageGenerationRate() {
		return messageGenerationRate;
	}

	/**
	 * @param messageGenerationRate the messageGenerationRate to set
	 */
	public void setMessageGenerationRate(float messageGenerationRate) {
		this.messageGenerationRate = messageGenerationRate;
	}

	/**
	 * @return the messageCounter
	 */
	public int getMessageCounter() {
		return messageCounter;
	}

	/**
	 * @param messageCounter the messageCounter to set
	 */
	public void setMessageCounter(int messageCounter) {
		this.messageCounter = messageCounter;
	}

	/**
	 * @return the stepCounter
	 */
	public int getStepCounter() {
		return stepCounter;
	}

	/**
	 * @param stepCounter the stepCounter to set
	 */
	public void setStepCounter(int stepCounter) {
		this.stepCounter = stepCounter;
	}
}

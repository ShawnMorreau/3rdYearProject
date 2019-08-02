package Simulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import Model.Message;
import Model.ModelContainer;
import Model.NetworkEvent;
import Model.NetworkTopology;
import Model.Router;
import Routing.FloodingRoutingAlgorithm;
import Routing.RandomRoutingAlgorithm;
import Routing.RoutingAlgorithm;
import View.ShapesContainer;
import XML.XMLState;
import XML.XMLStateSerializer;

/**
 * This class is the Model of the Network Routing Simulator, and it allows the Controller of the Network Routing Simulator
 * to add and remove routers. This class then creates and builds the network topology from the provided data. This class
 * also allows the controller to step through the simulator, create messages, as well as forward messages from one
 * router to another until it reaches its destination.
 *
 * @author Zaidoon Abd Al Hadi, Shawn Morreau, Aaron Bungay, Jaspreet Sanghra
 * @version 0.1
 */
public class NetworkRoutingSimulatorModel extends Observable {

	private NetworkTopology topology;
	private RoutingAlgorithm chosenRoutingAlgorithm;
	private float messageGenerationRate;
	private int messageCounter;
	private int baseMessageCounter;
	private int stepCounter;
	private XMLState xmlState;
	private Stack<ModelContainer> stackOfGoodies;
	private boolean stepBackClicked, somethingRemoved, hasStepped;

	/**
	 * Constructor for the MVC Model
	 */
	public NetworkRoutingSimulatorModel() {
		messageGenerationRate = 1;
		messageCounter = 0;
		stepCounter = 0;
		chosenRoutingAlgorithm = new RandomRoutingAlgorithm(); // initial chosen routing algorithm
		topology = new NetworkTopology();
		xmlState = new XMLState();
		stackOfGoodies = new Stack<ModelContainer>();
		stackOfGoodies.push(new ModelContainer(new NetworkTopology(), chosenRoutingAlgorithm, 
				messageGenerationRate, messageCounter, stepCounter));
	}

	/**
	 * Checks if the given router name already exists in the topology
	 * @param routerName the name of the router to check
	 * @return true if the name already exists, otherwise false
	 */
	public boolean containsRouter(String routerName) {
		return topology.contains(routerName);
	}

	/**
	 * @return the current value of the message generation rate
	 */
	public float getMessageGenerationRate() {
		return messageGenerationRate;
	}

	/**
	 * Sets the new message generation rate if its valid and restarts the counter
	 * @param messageGenerationRate the new rate to be set
	 */
	public void setMessageGenerationRate(float messageGenerationRate) {
		if (messageGenerationRate > 0) {
			this.messageGenerationRate = messageGenerationRate;
			stepCounter = 0;
		}
	}

	/**
	 * Adds a neighbour to a router
	 * @param routerName - the name of the first router to gain a neighbour(neighbourName)
	 * @param neighbourName - the name of the second router to gain a neighbour(routerName)
	 * @return true if the neighbour connection was added successfully, otherwise false
	 */
	public boolean attachNeighbour(String routerName, String neighbourName) {
		if (topology.setRouterNeighbour(routerName, neighbourName) && topology.setRouterNeighbour(neighbourName, routerName)) {
			baseMessageCounter = messageCounter;
			notifyView(topology);
			chosenRoutingAlgorithm.forceSetRoutingTables(topology);
			return true;
		}
		return false;
	}

	/**
	 * Deletes the neighbouring connection between two routers
	 * @param routerName the name of the first router
	 * @param neighbourName the name of the second router
	 * @return true if the connection is deleted successfully, otherwise false
	 */
	public boolean deleteNeighbour(String routerName, String neighbourName){
		if(topology.removeNeighbour(routerName, neighbourName) && topology.removeNeighbour(neighbourName, routerName)) {
			somethingRemoved = true;
			baseMessageCounter = messageCounter;
			notifyView(topology);
			chosenRoutingAlgorithm.forceSetRoutingTables(topology);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds a router to the topology
	 * @param router the router to be added
	 */
	public void addRouter(Router router) {
		if (router != null && !containsRouter(router.getName())){
			topology.addRouter(router);
			baseMessageCounter = messageCounter;
			notifyView(topology);
			chosenRoutingAlgorithm.forceSetRoutingTables(topology);
		} 
	}

	/**
	 * Remove a router from the topology
	 * @param name the name of the router to be removed
	 */
	public void removeRouter(String name) {
		if (name.length() > 0){
			topology.removeRouter(name);
			somethingRemoved = true;
			baseMessageCounter = messageCounter;
			notifyView(topology);
			chosenRoutingAlgorithm.forceSetRoutingTables(topology);
		} 
	}

	/**
	 * Creates a message if the set rate demands it and forwards all the messages to
	 * 	the next router
	 */
	public void step() {
		chosenRoutingAlgorithm.setRoutingTables(topology);
		if(topology.getRouters().size() > 0){
			forwardMessages();
			if(messageGenerationRate != 0 && (stepCounter % messageGenerationRate == 0 || stepCounter == 0) ){
				Message message = createMessage(getRandomRouter(), getRandomRouter());
				injectMessage(message);
			}
		}
		topology.calculateAverageHopsMetric();
		//enable stepback
		hasStepped = true;
		notifyView(topology);
		stepCounter++;
	}

	public int getBaseMessageCounter() {
		return baseMessageCounter;
	}

	/**
	 * Removes all the existing messages in the topology
	 */
	public void deleteAllMessages() {
		topology.clearMessages();
		stackOfGoodies.push(new ModelContainer(new NetworkTopology(), chosenRoutingAlgorithm, 
				messageGenerationRate, messageCounter, stepCounter));
		topology.resetMetrics();
		messageCounter = 0;
		baseMessageCounter = 0;
		stepCounter = 0;

		//disable step-back
		hasStepped = false;
		notifyView(topology);
	}

	/**
	 * @return a random router from the network topology
	 */
	private Router getRandomRouter() {
		Set<Router> routers = topology.getRouters();
		Iterator<Router> routersIterator = routers.iterator();
		int numberOfRouters = routers.size();
		Random randomGenerator = new Random();
		int randomRouterIndex = randomGenerator.nextInt(numberOfRouters); // stores a random index that's within the routers set range.
		for (int i = 0; i < randomRouterIndex; i++) {
			routersIterator.next();
		}
		return routersIterator.next();
	}

	/**
	 * Create a message that will be forwarded to the next router.
	 * @param source - the source router
	 * @param destination - the destination router
	 * @return a message that contains a value, source and destination
	 */
	private Message createMessage(Router source, Router destination) {
		Message message = new Message(messageCounter , source, destination);
		messageCounter++;
		return message;
	}

	/**
	 * Store the provided message in the source router.
	 * @param message - the message to be added to the source router
	 */
	private void injectMessage(Message message) {
		Set<Router> routers = topology.getRouters();

		// Loop through each router, and store the message in the router
		// with the same name as the name of the source router in the message.
		//
		for (Router router: routers) {
			if(message.getSource().getName().equals(router.getName())) {
				router.storeMessage(message);
				message.addRouter(router);
				message.getRoutersPassedThrough().add(router);
			}
		}
	}

	/**
	 * Forward all the messages in each router to the next router using the routing table in
	 * each router.
	 */
	private void forwardMessages() {
		Set<Router> routers = topology.getRouters();
		HashMap<Message,HashSet<Router>> newMessageLocations = new HashMap<Message,HashSet<Router>>();
		HashMap<Message, HashSet<Router>> messagesToBeRemoved = new HashMap<Message, HashSet<Router>>();
		HashSet<Message> incrementedMessages = new HashSet<Message>(); 

		// Loop through each router, and get every message that each router has.
		//
		for (Router router: routers) {

			// remove every message from the current router and store it in
			// the next router.
			//
			ArrayList<Message> messages = router.getStoredMessages();
			for (Message message: messages) {
				Router destination = message.getDestination();

				HashSet<Router> nextRouterSet = router.getRoutingTable().get(destination);
				if (nextRouterSet != null) {
					if (nextRouterSet.isEmpty()) {
						addRouterToMessageHashMap(router, messagesToBeRemoved, message);
						break;
					}
					for (Router nextRouter : nextRouterSet) {
						if (nextRouter != null) {
							if (chosenRoutingAlgorithm instanceof FloodingRoutingAlgorithm) {
								if (!message.getRoutersPassedThrough().contains(nextRouter)) {

									message.getRoutersPassedThrough().add(nextRouter);
								}
								else {
									continue;
								}
							}
							addRouterToMessageHashMap(nextRouter, newMessageLocations, message);
							addRouterToMessageHashMap(router, messagesToBeRemoved, message);
							topology.incrementTotalPacketsTransmittedMetric();
						}
					}
					if (!incrementedMessages.contains(message)) {
						message.incrementSteps();
						incrementedMessages.add(message);
					}
					if (chosenRoutingAlgorithm instanceof  FloodingRoutingAlgorithm && router.getStoredMessages().contains(message)) {
						addRouterToMessageHashMap(router, messagesToBeRemoved, message);
					}
				} else {
					//message is at destination
					topology.incrementNumMessagesReachedDest();
					addRouterToMessageHashMap(router, messagesToBeRemoved, message);
				}
			}
		}
		deleteMessages(messagesToBeRemoved);
		addMessages(newMessageLocations);
	}

	/**
	 * Adds a router to the HashSet of a message, and updates the entry in the HashMap
	 * @param router the router needing to have its message list updated
	 * @param messageHashMap the HashMap to be updated
	 * @param message the message whose location needs to be updated
	 */
	private void addRouterToMessageHashMap(Router router, HashMap<Message, HashSet<Router>> messageHashMap, Message message) {
		HashSet<Router> routerHashSet;
		if (messageHashMap.get(message) == null ){
			routerHashSet = new HashSet<>();
		}
		else {
			routerHashSet = messageHashMap.get(message);
		}
		routerHashSet.add(router);
		messageHashMap.put(message, routerHashSet);
	}

	/**
	 * Deletes the messages from the given routers
	 * @param messagesToBeRemoved - a map of messages and the router they will deleted from
	 */
	private void deleteMessages(HashMap<Message,HashSet<Router>> messagesToBeRemoved) {
		for(Map.Entry<Message,HashSet<Router>> routerSet : messagesToBeRemoved.entrySet()) {
			for (Router router : routerSet.getValue()) {
				router.removeMessage(routerSet.getKey());
			}
		}
	}

	/**
	 * Adds the messages to the given routers
	 * @param newMessageLocations - a map of messages and the router they will be added to
	 */
	private void addMessages(HashMap<Message,HashSet<Router>> newMessageLocations) {
		for (Map.Entry<Message,HashSet<Router>> routerSet : newMessageLocations.entrySet()) {
			for (Router router : routerSet.getValue()) {
				router.storeMessage(routerSet.getKey());
				routerSet.getKey().addRouter(router);
			}
		}
	}

	/**
	 * @return the number of routers in the topology
	 */
	public int numberOfRouters() {
		return topology.numberOfRouters();
	}

	/**
	 * Display the previous topology.
	 */
	public void stepBack(){
		ModelContainer lastState = null;
		setStepBackClicked();
		try{
			stackOfGoodies.pop();
			//makes it so the first message is always available
			if(stackOfGoodies.isEmpty()){
				stackOfGoodies.push(lastState);
			}
			lastState = stackOfGoodies.peek();

			
			//restore everything.
			chosenRoutingAlgorithm = lastState.getChosenRoutingAlgorithm();
			messageCounter = lastState.getMessageCounter();
			messageGenerationRate = lastState.getMessageGenerationRate();
			topology = lastState.getTopology();
			stepCounter = lastState.getStepCounter();
			notifyView(topology);

		}catch(Exception e){

		}

	}

	/**
	 * push the current state of the network onto a stack
	 */
	public void pushStateToStack(){
		if(!stepBackClicked)
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(bos);
				oos.writeObject(topology);
				oos.flush();
				oos.close();
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			byte[] byteData = bos.toByteArray();


			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			NetworkTopology copyTopology = null;
			try {
				copyTopology = (NetworkTopology) new ObjectInputStream(bais).readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




			RoutingAlgorithm copyAlgorithm = chosenRoutingAlgorithm;

			stackOfGoodies.push(new ModelContainer(copyTopology, copyAlgorithm, 
					messageGenerationRate, messageCounter, stepCounter));
		}
		stepBackClicked = false;

	}
	/**
	 * If either of the remove buttons are pressed, a new stack is produced to prevent errors.
	 */
	public void clearStack(){
		while(!stackOfGoodies.isEmpty()){
			stackOfGoodies.pop();
		}
		somethingRemoved = false;
	}

	/**
	 * Notify the view of a network topology change, if the case that something has been removed
	 * the stack is cleared and any previous messages are deleted.
	 * Only when a step has occured can someone step back.
	 * 
	 * @param topology the current network
	 */
	private void notifyView(NetworkTopology topology) {
		if(somethingRemoved) {
			clearStack();
		}
		if(hasStepped){
			pushStateToStack();
		}
		setChanged();
		notifyObservers(new NetworkEvent(this, topology, messageCounter, baseMessageCounter));

	}

	/**
	 * Notify the view of the results of a serialization (restore). This event object
	 * contains the topology panel from the XML file, and the state of the view (topology panel)
	 * from the XML file.
	 * 
	 * @param topology the current network
	 * @param panel the updated topology panel
	 */
	private void notifyView(NetworkTopology topology, ShapesContainer sc) {
		clearStack();
		stackOfGoodies.push(new ModelContainer(new NetworkTopology(), new RandomRoutingAlgorithm(), 
				1, 0, 0));
		stackOfGoodies.push(new ModelContainer(topology, chosenRoutingAlgorithm, 
				messageGenerationRate, messageCounter, stepCounter));
		hasStepped = false;
		setChanged();
		notifyObservers(new NetworkEvent(this, topology, sc, true, messageCounter, baseMessageCounter));
	}

	/**
	 * @param routingAlgorithm the chosen RoutingAlgorithm to be set
	 */
	public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm) {
		chosenRoutingAlgorithm = routingAlgorithm;
		chosenRoutingAlgorithm.forceSetRoutingTables(topology);
	}

	/**
	 * Exports the state of the model and panel into an xml
	 * @param file the location to save the xml to, including file name and extension
	 * @param panel the current panel to be saved
	 */
	public void saveState(File file, ShapesContainer sc) {
		xmlState.setModelContainer(new ModelContainer(topology, chosenRoutingAlgorithm, 
				messageGenerationRate, messageCounter, stepCounter));
		xmlState.setShapesContainer(sc);
		XMLStateSerializer stateSerializer = new XMLStateSerializer();
		stateSerializer.serialize(file, xmlState);
	}

	/**
	 * Imports the xml and sets the state of the model and panel
	 * @param file the location to save the xml to, including file name and extension
	 */
	public void restoreState(File file) {
		XMLStateSerializer stateSerializer = new XMLStateSerializer();
		stateSerializer.deserialize(file, xmlState);
		ModelContainer newModelContainer = xmlState.getModelContainer();
		this.topology = newModelContainer.getTopology();
		this.chosenRoutingAlgorithm = newModelContainer.getChosenRoutingAlgorithm();
		this.messageGenerationRate = newModelContainer.getMessageGenerationRate();
		this.messageCounter = newModelContainer.getMessageCounter();
		this.stepCounter = newModelContainer.getStepCounter();
		this.baseMessageCounter = messageCounter;
		notifyView(this.topology, xmlState.getShapesContainer());
	}

	/**
	 * Sets the boolean that states that the step back was clicked
	 */
	protected void setStepBackClicked(){
		stepBackClicked = true;
	}

	/**
	 * @return the number of messages in the topology
	 */
	public int getMessageCount(){
		return messageCounter;
	}	
}
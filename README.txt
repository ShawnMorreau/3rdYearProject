NETWORK ROUTING SIMULATOR MILESTONE 4

1. Deliverables:
	1. 'Network-Routing-Simulator.jar' is the executable application with provided source files included
	2. 'Final UML.png' is the UML class diagram of the Network-Routing-Simulator
	3. Generated JAVADOC files can be found in the 'javadoc' folder
	4. 'User_manual.html' > See updated version through GUI Menu (Help -> User manual) for usage instructions

2. Authors: Zaidoon Abd Al Hadi, Jaspreet Sanghra, Aaron Bungay, Shawn Morreau

3. Changes since last milestone: Implemented XML serialization/deserialization (save/restore functionality) using x-stream library. We decided to go with this library since it has better support
		for maps (HashMaps, etc) and cyclic references (Router A references Router B and router B references router A). We tried using the JAXB library for XML marshalling/unmarshalling at first but
		due to these issues with maps and references we ran into a lot of problems unmarshalling (deserializing) the file so this was our deciding factor for looking for alternatives and choosing x-stream.
		We introduced container classes to store only what is needed for the XML from our objects instead of the entire objects to save a lot of space and ensure no other unexpected errors would occur while
		serializing. We also introduce the capability to undo (step back) in the simulation, which moves the messages back to their previous router. We decided to implement this undo capability using a stack as
		the data structure since it makes retrieving previous states/models easier than any other data structure.

4. Future Plans: 
	1. None at the moment

5. Source Files: 
	1. NetworkTopology.java
	2. NetworkRoutingSimulator.java
	3. RoutingAlgorithm.java 
	4. BreadthFirstSearchRoutingAlgorithm.java
	5. DepthFirstSearchRoutingAlgorithm.java
	6. FloodingRoutingAlgorithm.java
	7. RandomRoutingAlgorithm.java
	8. Message.java
	9. Router.java
	10. Connection.java
	11. Node.java
	12. TopologyPanel.java
	13. TopologyShape.java
	14. AllTests.java
	15. MessageTest.java
	16. NetworkRoutingSimulatorModelTest.java
	17. NetworkTopologyTest.java
	18. RouterTest.java
	19. ModelContainer.java
	20. NetworkEvent.java
	21. XMLTest.java
	22. ShapesContainer.java
	23. XMLState.java
	24. XMLStateSerializer.java

6. Known issues: None that we know of

7. Design Decisions:
	We decided to use the MVC pattern because it allowed to decouple the view from the controller and model thus making our application robust and easier to maintain.
	We decided to create the classes mentioned in the source files to increase cohesion and decrease coupling as much as possible. 
    We used HashMaps to create the linkages between the routers and the network topology to increase the efficiency of our routing algorithm.
	We decided to use the Strategy pattern by having an abstract RoutingAlgorithm class to specifiy default behaviour for routing algorithms and to extend the class and call its
	setRoutingTables() method in order to set the routing tables of our network topology.
	
8. Sources used:
  1. [x-stream](http://x-stream.github.io/) - a simple library for XML serialization & deserialization
  2. [clouds background image](http://www.publicdomainpictures.net/view-image.php?image=35818&picture=fluffy-clouds) - public domain image, modified to increase brightness/reduce opacity
  3. [network image for window icon](https://commons.wikimedia.org/wiki/File:FullMeshNetwork.svg) - public domain image, cropped and resized
  4. [typebase](http://devinhunt.github.io/typebase.css/) - minimal css stylesheet to make text look prettier for user manual/about webpages

# 😍Network-Routing-Simulator😍

SYSC 3110
---
Group: teamgoodwithanything 

Authors: Zaidoon Abd Al Hadi, Aaron Bungay, Jaspreet Sanghra, Shawn Morreau

Version: 3.0

Lab Section: L1
  
---
### Styling:
  Remember not to compromise the code trying to follow styling, self-documentation, and code quality comes first!

  1. Method names, and non-final fields should be in camelCase.
  2. Final fields should be static, and UPPERCASE.
  3. When making comments for methods, fill out the javadoc generated ones.
  4. Class Member ordering is: Fields, Constructors, Methods. Put the meat of the methods at the top, helpers at the bottom.
  5. Try to avoid returns in the middle of methods.

---
### Milestone 4 [Complete by 02/12/16] - Tasks to Do
We will be implementing serialization (save/restore functionality) using XML with XStream library, and the ability to step back (like undo).


  1. [Zaidoon], changes:
    1. Add save functionality (implement using XStream library, make 'Save' menu item on GUI working)
    2. Test cases
    3. Update user manual
  2. [Aaron], changes:
    1. Add restore functionality (implement using XStream library, make 'Restore' menu item on GUI working)
    2. Test cases
    3. Update readme
  3. [Shawn], changes:
    1. Collaborate for Step back functionality (backend on model (store network topology for each step on a stack?) and on GUI add a new menu item under 'Simulation' for 'Undo')
    2. Test cases
    3. Update uml class diagram
  4. [Jaspreet], changes:
    1. Collaborate for Step back functionality (backend on model (store network topology for each step on a stack?) and on GUI add a new menu item under 'Simulation' for 'Undo')
    2. Collaborate for save and restore functionality
    2. Test cases
    3. Update javadocs


### ~~Milestone 3 [Complete by 21/11/16] - Tasks to Do~~
We will be implementing all remaining routing algorithms along with all metrics.

  1. [Zaidoon], changes:
    1. Implement the FloodingRoutingAlgorithm
    2. Test cases for the flooding algorithm
    3. Update readme/javadocs: document changes made since last iteration and reasons for those changes, refer to <b>Notes</b> below
  2. [Aaron], changes:
    1. Implement both metrics properly. Rename 'Steps' on GUI to 'Hops.
      * Total number of packets transmitted (every copy of a message forwarded from one router to its
neighbor counts as one packet)
      * Average number of hops each message goes through before reaching its destination
    2. Allow selection of routing algorithm through GUI -> 'Simulation' menu -> 'Routing algorithm' (opens popup with dropdown list)
    3. Resolve github issues from previous milestones
  3. [Shawn], changes:
    1. Implement DepthFirstSearchRoutingAlgorithm
    2. Test cases for the depth first search algorithm
    3. Update user manual, generate runnable jar
  4. [Jaspreet], changes:
    1. Implement BreadthFirstSearchRoutingAlgorithm
    2. Test cases for the breadth first search algorithm
    3. Update UML class diagram from previous milestone

### ~~Milestone 2 [Complete by 07/11/16] - Tasks to Do~~
We will be implemention a GUI using MVC and we will have a new test class with test cases. Please refer to the mock-gui image. This will require the following tasks to be completed:

  1. [Zaidoon], changes:
    1. Create Model from existing console based system
      * implement removeRouter/removeConnection methods
      * ensure addRouter/addConnection are in methods
    2. Create Controller
      * view should use lambdas to call controller methods directly
    3. Some test cases
  2. [Aaron], changes:
    1. Topology displaying in main view with circles as routers and lines as connections
      * show messages on each router for each step
    2. Allow selection of a router/connection
    3. Allow dragging of a router in the view (extra, nice to have)
    4. Help others once done 
    5. Some test cases
  3. [Shawn], changes:
    1. Implement Router "Add" button on GUI
      * after clicking add button, user can click on main topology view to add a new router and its gui circle will be displayed and stay on the topology
      * only after adding - give a name: basic option: display a dialog box asking user for router name after they add a router, most elegant option: display a textarea on the router (circle on gui topology)
      that the user can type the name onto, when they hit enter it saves the name
    2. Implement Router "Remove button on GUI (deletes the routers and associated connections)
      * basic option: display a dialog with dropdown list of router names so user can select
      a router to delete
      * most elegant option: allow user to select 1 (or multiple, extra elegant) router(s) at a time, clicking the remove button will remove selected router(s)
    3. Implement 'File' (Import & Export unclickable) and 'Help' menu bar items
    4. Some test cases
  4. [Jaspreet], changes:
    1. Implement Connection "Add" button on GUI
      * click on a router and drag mouse to another router after clicking add button
    2. Implement Connection "Remove" button on GUI
      * basic option: show dialog box with dropdown boxes of routers for to and from of the connection
      * elegant: click line connection (or extra elegant, multiple) clicking remove button will remove selected connection(s)
    3. Implement 'Simulation' menu bar items
    4. Some test cases
    
### ~~Milestone 1 [Complete by 21/10/16] - Tasks to Do~~
  1. [Shawn], changes:
    1. Implement console prompts
      1. Ask user for routers (names not number of routers) (nodes)
      2. Create routers with the names provided and add them to topology
      3. Ask user for neighbouring routers (nodes) that belong to each router.
      4. Update topology with neighbouring routers
      5. Ask user for message generation rate (number of simulation steps to generate message)
      6. Inform the user each time that they have three options:
        1. The user can step through the simulator by pressing the "Space" key
        2. The user can exit the program by pressing the "Q" key
        3. The user can display the topology by pressing the "D" key
    
  2. [Aaron], changes:
    1. Implement random routing algorithm
    
  3. [Zaidoon], changes:
    1. Implement the simulation step process
      1. Generate a new message using a random source and a destination and inject it in the network. This will not happen during every           step, but only according to a user-settable rate
      2. The message value will be "message: n" where n is the message number.
      3. forward all messages in the network to the next router(s) toward their destination
      4. Call the metric method
    
  4. [Jaspreet], changes:
    1. Implement the total number of packets transmitted metric
      1. Count the total number of packets transmitted (every copy of a message forwarded from one router to its neighbor counts as one          packet)
      2. Implement a method that prints the metrics that can be called by the simulation step
      3. Implement a method in the topology class to print the topology as a string using the following format:
        
         a -> b, c
         
         b -> c	
         
         where a is connected to both b and c, and b is connected to c.

---
### Notes: 

  1. We must include a README. The README must include:
    1. Name and description of deliverables
    2. Authors
    3. Changes made to each deliverable since previous Milestone
    4. known issues
    5. Roadmap (future plans)
    6. Provided source code file names, and diagram file names

  2. We must include update to date UML diagrams (Class and Sequence)

  3. We must include a complete user manual detailing setup instructions and usage

  4. We must include a detailed description of design decisions 

  5. We must include a JAVADOC documentation 

  6. We must include all unchanged deliverables from previous milestones with each milestone submission

### Reminders: 

  1. Each contribution (source code, documentation, etc.) must contain the name of its author

  2. We should report time consuming issues or improvements using github's built in Issue Tracker

  3. We must submit source code and deliverables to cuLearn

---
### Sources used:

  1. [x-stream](http://x-stream.github.io/) - a simple library for XML serialization & deserialization
  
  2. [clouds background image](http://www.publicdomainpictures.net/view-image.php?image=35818&picture=fluffy-clouds) - public domain image, modified to increase brightness/reduce opacity
  
  3. [network image for window icon](https://commons.wikimedia.org/wiki/File:FullMeshNetwork.svg) - public domain image, cropped and resized
  
  4. [typebase](http://devinhunt.github.io/typebase.css/) - minimal css stylesheet to make text look prettier for user manual/about webpages

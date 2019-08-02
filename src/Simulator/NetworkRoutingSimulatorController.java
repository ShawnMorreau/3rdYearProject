package Simulator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Router;
import Routing.RandomRoutingAlgorithm;
import Routing.RoutingAlgorithm;
import View.Connection;
import View.Node;
import View.ShapesContainer;
import View.TopologyShape;

/**
 * This class is the Controller of the Network Routing Simulator.
 *
 * @author Zaidoon Abd Al Hadi, Jaspreet Sanghra, Shawn Morreau
 * @version 0.1
 */
public class NetworkRoutingSimulatorController implements ActionListener {

	private static final String SELECT_ROUTING_ALGORITHM_PROMPT_TITLE = "Routing Algorithm Selection";
	private static final String SELECT_ROUTING_ALGORITHM_PROMPT = "Select a routing algorithm to use:";
	private static final String MESSAGE_GENERATION_RATE_PROMPT = "Enter the desired Message Generation Rate. (create new message every 'x' steps)\n";
	private static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
	private static final FileNameExtensionFilter XML_FILENAME_FILTER = new FileNameExtensionFilter("eXtensible Markup Language file (*.xml)", "xml");

	private NetworkRoutingSimulatorModel model;
	private NetworkRoutingSimulatorView view;
	private JPanel viewPanel;
	private String name;
	private JButton buttonPressed;

	/**
	 * Constructor for the MVC controller
	 * 
	 * @param model the MVC model
	 */
	public NetworkRoutingSimulatorController(NetworkRoutingSimulatorModel model){
		if (model != null){
			this.model = model;
			buttonPressed = null;
		}
	}

	/**
	 * Adds a view to the controller
	 * 
	 * @param view
	 */
	public void addView(NetworkRoutingSimulatorView view) {
		if (view != null) {
			this.view = view;
			this.view.addAddRouterListener(new AddRouterListener());
		}
	}

	/**
	 * Adds a connection to the topology
	 * @param nodeName1 the name of first existing router
	 * @param nodeName2 the name of the second existing router
	 * @return true if the connection has added successfully, otherwise false
	 */
	public boolean handleAddConnection(String nodeName1, String nodeName2){
		if(model.attachNeighbour(nodeName1, nodeName2)) {
			System.out.println("Connection Added: " + nodeName1 + "<->" + nodeName2);
			return true;
		}
		else {
			System.out.println("Connection Add Failed of " + nodeName1 + "<->" + nodeName2);
			return false;
		}
	}

	/**
	 * Deletes a connection from the topology be removing the neighbour connection
	 * @param connection the topology shape from the view holds properties of its end nodes
	 */
	private void handleDeleteConnections(Connection connection) {
		model.deleteNeighbour(connection.getNode1().getName(), connection.getNode2().getName());
	}

	/**
	 * Deletes a router from the topology, if it exists
	 * @param name the name of the router to be removed
	 */
	public void handleRemoveRouter(String name) {
		model.removeRouter(name);
	}

	/**
	 * Simulates the topology to move/add messages
	 */
	private void simulateStep() {
		if(model.getMessageCount()== model.getBaseMessageCounter()){
			model.pushStateToStack();
		}
		model.step();
	}

	/**
	 * Sets the message generation rate
	 * @param rate the rate to be set
	 */
	private void setMessageGenerationRate(float rate) {
		model.setMessageGenerationRate(rate);
	}

	/**
	 * Sets the chosen routing algorithm to be used to create the routing tables
	 * @param routingAlgorithm the routing algorithm chosen by the user
	 */
	private void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm) {
		model.setRoutingAlgorithm(routingAlgorithm);
	}

	/**
	 * Deletes all existing messages
	 */
	private void resetMessages() {
		model.clearStack();
		model.deleteAllMessages();
	}

	/**
	 * Calls the undo function in the model
	 */
	private void stepBack(){
		model.stepBack();
	}

	/**
	 * Handles action events from buttons and menus of the view.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals(NetworkRoutingSimulatorView.UNDO_NAME)){
			stepBack();
		}
		if (e.getActionCommand().equals(NetworkRoutingSimulatorView.STEP_MENU_NAME)) {
			// When user clicks on 'Step' button, run a simulation step in the model
			simulateStep();
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.RESET_MENU_NAME)) {
			// When user clicks on 'Rest Messages' button, clear all messages and metrics in model
			resetMessages();
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.MESSAGE_GENERATION_NAME)) {
			System.out.println("Setting Message Generation Rate");
			float rate = -1;
			while(rate == -1) {
				String rateString = JOptionPane.showInputDialog(MESSAGE_GENERATION_RATE_PROMPT);

				if(rateString == null || rateString.isEmpty()) return; // user clicks cancel or doesn't input anything


				try {
					rate = Float.parseFloat(rateString);
				} catch(Exception ex) {
					System.out.println("Invalid Rate, try again.");
					rate = -1;
				}
			}
			setMessageGenerationRate(rate);
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.ROUTING_ALGORITHM_MESSAGE_NAME)) {
			// Open an algorithm selection dialog when user clicks the 'Routing Algorithm' menu
			String chosenAlgorithmName = (String) JOptionPane.showInputDialog(null, SELECT_ROUTING_ALGORITHM_PROMPT, SELECT_ROUTING_ALGORITHM_PROMPT_TITLE, 
					JOptionPane.QUESTION_MESSAGE, null,
					RoutingAlgorithm.INSTANCES.keySet().toArray(), RandomRoutingAlgorithm.NAME);
			if (chosenAlgorithmName != null) {
				System.out.println("Selected " + chosenAlgorithmName);
				setRoutingAlgorithm(RoutingAlgorithm.INSTANCES.get(chosenAlgorithmName));
			}
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.RESTORE_MENU_NAME)) {
			JFileChooser fc = new JFileChooser(CURRENT_DIRECTORY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(XML_FILENAME_FILTER);
			int returnVal = fc.showOpenDialog(view);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				model.restoreState(file);
			}
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.SAVE_MENU_NAME)) {
			JFileChooser fc = new JFileChooser(CURRENT_DIRECTORY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(XML_FILENAME_FILTER);
			File file = new File(CURRENT_DIRECTORY + "/" + getRecommendedFileName());
			fc.setSelectedFile(file);
			int returnVal = fc.showSaveDialog(view);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				ShapesContainer sc = new ShapesContainer(view.getTopologyPanel().getNodes(), view.getTopologyPanel().getConnections());
				model.saveState(file, sc);
			}
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.EXIT_MENU_NAME)) {
			// When user clicks the 'Exit' button on the view, exit the application
			System.exit(0);
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.USER_MANUAL_NAME)) {
			// show a user manual window which displays the user manual webpage when
			// the user clicks the 'User Manual' menu on the view
			view.showUserManual();
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.ABOUT_NAME)) {
			// show an about window which displays version info and authors when
			// the user clicks the 'About' menu on the view
			view.showAbout();
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.REMOVE_CONNECTION)) {
			// remove a connection from the view when the 'Remove' button is clicked
			try {
				TopologyShape shape = view.getTopologyPanel().getSelected();
				if(shape != null && shape instanceof Connection) {
					Connection c = (Connection)shape;
					System.out.println("Deleting connection " + c.getNode1().getName() + "<->" + c.getNode2().getName());
					handleDeleteConnections(c);
					view.getTopologyPanel().clearSelected();
				}
			} catch(Exception ex) {
				System.out.println("Error removing Connection");
				ex.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.REMOVE_ROUTER)) {
			// remove a router from the view when the 'Remove' button is clicked
			TopologyShape shape =  view.getTopologyPanel().getSelected();
			if(shape instanceof Node) {
				Node node = (Node) shape;
				System.out.println("Deleted Router " + node.getName());
				handleRemoveRouter(node.getName());
				view.getTopologyPanel().clearSelected();
			}
		}
		else if (e.getActionCommand().equals(NetworkRoutingSimulatorView.ADD_CONNECTION_NAME)) {
			// allow user to add a connection between two nodes in the view when the 'Add' button is clicked
			view.addConnectionSetup();
		}
	}

	/**
	 * @return a default file name suggestion consisting of the date and time
	 */
	private String getRecommendedFileName() {
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		return "topology" + timeStampPattern.format(java.time.LocalDateTime.now()) + ".xml";
	}

	/**
	 * This private class handles the addition of a new router to the topology.
	 */
	private class AddRouterListener implements ActionListener{

		private static final String ADD_ROUTER_HELP_PROMPT = "Click anywhere on the topology to add your new router";
		private static final String ROUTER_ALREADY_EXISTS_PROMPT = "The router already exists! Please enter a name for your new router";
		private static final String NEW_ROUTER_PROMPT = "Please enter a name for your new router";

		private JPanel infoPanel;
		private boolean showAddRouterInfoMsg;

		public AddRouterListener() {
			// prepare the info panel so it will be ready to display a popup message to the user
			// when they click the add router button on the view
			// this is required to have a checkbox that allows the user not see the message anymore
			infoPanel = new JPanel();
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			infoPanel.add(new JLabel(ADD_ROUTER_HELP_PROMPT));
			infoPanel.add(Box.createVerticalStrut(20));
			JCheckBox jcb = new JCheckBox("Do not show me this again.");
			jcb.addActionListener(ae -> showAddRouterInfoMsg = !showAddRouterInfoMsg);
			infoPanel.add(jcb);
			showAddRouterInfoMsg = true;
		}

		/**
		 * Handles the process for adding a new router
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonPressed = (JButton)e.getSource();
			buttonPressed.setEnabled(false);
			viewPanel = view.getTopologyPanel();

			name = JOptionPane.showInputDialog(NEW_ROUTER_PROMPT);
			try{
				name = name.trim();
			}
			catch(NullPointerException NPE){

			}
			if (name == null || name.isEmpty()){
				buttonPressed.setEnabled(true);
				return;
			}

			if(!name.isEmpty()){
				System.out.println("New router name is " + name);
				if(model.containsRouter(name)){
					boolean keepGoing = true;
					while(keepGoing){
						name = JOptionPane.showInputDialog(ROUTER_ALREADY_EXISTS_PROMPT);
						if (name == null){
							buttonPressed.setEnabled(true);
							return;
						}

						name = name.trim();

						if (!name.isEmpty() && !model.containsRouter(name)) keepGoing = false;
					}
				}

				if (showAddRouterInfoMsg) JOptionPane.showMessageDialog(null, infoPanel);
				viewPanel.addMouseListener(new AddRouterClickListener());
			}
		}
	}

	/**
	 * This private class acts as the mouse listener when a router is to be added
	 * after the user has chosen the name of the router. The user must choose a location
	 * on the GUI for the node to be created and displayed.
	 */
	private class AddRouterClickListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		/**
		 * Creates a node on the GUI at the location of the click
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			float x = e.getX();
			float y = e.getY(); 
			view.createNewNode(x - Node.DEFAULT_DIAMETER/2, y - Node.DEFAULT_DIAMETER/2, name);
			viewPanel.removeMouseListener(this);
			if (buttonPressed != null) buttonPressed.setEnabled(true);
			Router router = new Router(name);
			model.addRouter(router);
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
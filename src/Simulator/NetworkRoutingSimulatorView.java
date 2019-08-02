package Simulator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Model.NetworkEvent;
import Model.NetworkTopology;
import View.Node;
import View.TopologyPanel;

/**
 * This class is the view of the network routing simulator.
 * 
 * @author Zaidoon Abd Al Hadi, Aaron Bungay, Jaspreet Sanghra, Shawn Morreau
 * @version 0.2
 */
public class NetworkRoutingSimulatorView extends JFrame implements Observer {

	private static final long serialVersionUID = -6703635331381860350L;

	private static final String ABOUT_RELATIVE_FILE_PATH = "/About.html";
	private static final String USER_MANUAL_RELATIVE_FILE_PATH = "/User_Manual.html";
	private static final String FRAME_ICON_RELATIVE_PATH_16x16 = "/network-icon-16x16.png";
	private static final String FRAME_ICON_RELATIVE_PATH_32x32 = "/network-icon-32x32.png";
	private static final String FRAME_ICON_RELATIVE_PATH_64x64 = "/network-icon-64x64.png";
	private static final String FRAME_ICON_RELATIVE_PATH_128x128 = "/network-icon-128x128.png";

	private static final String AVERAGE_NUMBER_OF_HOPS_TITLE = "Average Hops";
	private static final String TOTAL_PACKETS_TRANSMITTED_TITLE = "Packets Transmitted";
	private static final String METRICS_TITLE = "Metrics";
	private static final String CONNECTIONS_TITLE = "Connections";
	private static final String ROUTERS_TITLE = "Routers";
	private static final String HELP_MENU_NAME = "Help";
	private static final String SIMULATION_MENU_NAME = "Simulation";
	public static final String SAVE_MENU_NAME = "Save";
	public static final String RESTORE_MENU_NAME = "Restore";
	private static final String FILE_MENU_NAME = "File";
	private static final String NETWORK_TOPOLOGY_CONTROLS_TITLE = "Network Topology";
	public static final String STEP_MENU_NAME = "Step";
	public static final String RESET_MENU_NAME = "Clear Messages";
	public static final String MESSAGE_GENERATION_NAME = "Message Generation Rate";
	public static final String ROUTING_ALGORITHM_MESSAGE_NAME = "Routing Algorithm";
	public static final String EXIT_MENU_NAME = "Exit";
	public static final String USER_MANUAL_NAME = "User Manual";
	private static final String REMOVE_CONNECTION_NAME = "Remove";
	public static final String REMOVE_CONNECTION = "Remove Connection";
	public static final String ADD_CONNECTION_NAME = "Add";
	private static final String ADD_ROUTER_NAME = "Add";
	public static final String REMOVE_ROUTER = "Remove Router";
	private static final String REMOVE_ROUTER_NAME = "Remove";
	public static final String ABOUT_NAME = "About";
	private static final String FRAME_TITLE = "Network Routing Simulator";

	private static final int FRAME_DEFAULT_WIDTH = 800;
	private static final int FRAME_DEFAULT_HEIGHT = 600;
	private static final int VERTICAL_SCROLLBAR_INCREMENT = 16;
	public static final String UNDO_NAME = "Undo";

	private NetworkRoutingSimulatorController controller;
	private TopologyPanel centerTopologyPanelView;
	private JButton addRouter;
	private List<Image> frameIcons;
	private JTextField totalPacketsMetricValue;
	private JTextField averageHopsMetricValue;

	JMenuItem undoMenu = new JMenuItem(UNDO_NAME);

	/**
	 * Constructor of the MVC View
	 * @param controller the controller of the application
	 */
	public NetworkRoutingSimulatorView(NetworkRoutingSimulatorController controller) {
		super(FRAME_TITLE);
		if (controller != null) {
			this.controller = controller;
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			getContentPane().setLayout(new BorderLayout());
			setSize(FRAME_DEFAULT_WIDTH, FRAME_DEFAULT_HEIGHT);
			frameIcons = new ArrayList<>();
			try {
				frameIcons.add(ImageIO.read(getClass().getResource(FRAME_ICON_RELATIVE_PATH_16x16)));
				frameIcons.add(ImageIO.read(getClass().getResource(FRAME_ICON_RELATIVE_PATH_32x32)));
				frameIcons.add(ImageIO.read(getClass().getResource(FRAME_ICON_RELATIVE_PATH_64x64)));
				frameIcons.add(ImageIO.read(getClass().getResource(FRAME_ICON_RELATIVE_PATH_128x128)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setIconImages(frameIcons);
			createMenuBar();
			createTopologyView();
			createTopologyControls();
			centerTopologyPanelView.setController(controller);
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}

	/**
	 * Creates the menu bar of the the GUI and its menu items
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(FILE_MENU_NAME);

		JMenuItem restoreMenu = new JMenuItem(RESTORE_MENU_NAME);
		JMenuItem saveMenu = new JMenuItem(SAVE_MENU_NAME);
		JMenuItem exitMenu = new JMenuItem(EXIT_MENU_NAME);
		fileMenu.add(restoreMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(exitMenu);

		menuBar.add(fileMenu);

		restoreMenu.addActionListener(controller);
		saveMenu.addActionListener(controller);
		exitMenu.addActionListener(controller);

		JMenu simulationMenu = new JMenu(SIMULATION_MENU_NAME);

		JMenuItem stepMenu = new JMenuItem(STEP_MENU_NAME);

		stepMenu.addActionListener(controller);
		JMenuItem msgGenRateMenu = new JMenuItem(MESSAGE_GENERATION_NAME);
		msgGenRateMenu.addActionListener(controller);
		JMenuItem resetMenu = new JMenuItem(RESET_MENU_NAME);
		resetMenu.addActionListener(controller);
		JMenuItem routingAlgorithmMenu = new JMenuItem(ROUTING_ALGORITHM_MESSAGE_NAME);
		routingAlgorithmMenu.addActionListener(controller);

		undoMenu.setEnabled(false);
		undoMenu.addActionListener(controller);

		simulationMenu.add(stepMenu);
		simulationMenu.add(undoMenu);
		simulationMenu.add(msgGenRateMenu);
		simulationMenu.add(resetMenu);
		simulationMenu.add(routingAlgorithmMenu);

		menuBar.add(simulationMenu);

		JMenu helpMenu = new JMenu(HELP_MENU_NAME);

		JMenuItem userManualMenu = new JMenuItem(USER_MANUAL_NAME);
		JMenuItem aboutMenu = new JMenuItem(ABOUT_NAME);
		helpMenu.add(userManualMenu);
		helpMenu.add(aboutMenu);

		userManualMenu.addActionListener(controller);

		aboutMenu.addActionListener(controller);

		menuBar.add(helpMenu);

		this.add(menuBar, BorderLayout.NORTH);
	}

	/**
	 * Creates the view showing the topology (nodes/routers and
	 * connections/neighbours)
	 */
	private void createTopologyView() {
		centerTopologyPanelView = new TopologyPanel();
		centerTopologyPanelView.addMouseListener(centerTopologyPanelView);

		centerTopologyPanelView.setBorder(new LineBorder(Color.WHITE));
		centerTopologyPanelView.setBackground(Color.WHITE);

		JScrollPane scroller = new JScrollPane(centerTopologyPanelView);
		scroller.getVerticalScrollBar().setUnitIncrement(VERTICAL_SCROLLBAR_INCREMENT);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
	}

	/**
	 * Creates the GUI buttons on the right hand side that add/remove
	 * routers and neighbouring connections
	 */
	private void createTopologyControls() {
		JPanel topologyControls = new JPanel();
		topologyControls.setLayout(new BoxLayout(topologyControls, BoxLayout.Y_AXIS));

		JLabel controlsTitle = new JLabel(NETWORK_TOPOLOGY_CONTROLS_TITLE);
		Font headerFont = new Font(controlsTitle.getFont().getName(), controlsTitle.getFont().getStyle(), 14);
		controlsTitle.setFont(headerFont);
		topologyControls.add(controlsTitle);

		topologyControls.add(Box.createVerticalStrut(20));

		JLabel routersTitle = new JLabel(ROUTERS_TITLE);
		Font subHeaderFont = routersTitle.getFont().deriveFont(routersTitle.getFont().getStyle() ^ Font.BOLD);
		routersTitle.setFont(subHeaderFont);
		topologyControls.add(routersTitle);

		topologyControls.add(Box.createVerticalStrut(5));

		addRouter = new JButton(ADD_ROUTER_NAME);
		addRouter.setFocusPainted(false);
		Font f2 = addRouter.getFont();
		addRouter.setFont(f2.deriveFont(f2.getStyle() ^ Font.BOLD));

		topologyControls.add(addRouter);

		topologyControls.add(Box.createVerticalStrut(5));

		JButton removeRouter = new JButton(REMOVE_ROUTER_NAME);
		removeRouter.setActionCommand(REMOVE_ROUTER);
		removeRouter.setFocusPainted(false);
		Font f3 = removeRouter.getFont();
		removeRouter.setFont(f3.deriveFont(f3.getStyle() ^ Font.BOLD));

		removeRouter.addActionListener(controller);

		topologyControls.add(removeRouter);

		Dimension maxButtonSize = new Dimension(controlsTitle.getMaximumSize().width, removeRouter.getMaximumSize().height);

		addRouter.setMaximumSize(maxButtonSize);
		removeRouter.setMaximumSize(maxButtonSize);

		topologyControls.add(Box.createVerticalStrut(40));

		JLabel connectionsTitle = new JLabel(CONNECTIONS_TITLE);
		Font f4 = connectionsTitle.getFont();
		connectionsTitle.setFont(f4.deriveFont(f4.getStyle() ^ Font.BOLD));
		topologyControls.add(connectionsTitle);

		topologyControls.add(Box.createVerticalStrut(5));

		JButton addConnection = new JButton(ADD_CONNECTION_NAME);
		addConnection.setFocusPainted(false);
		Font f5 = addConnection.getFont();
		addConnection.setFont(f5.deriveFont(f5.getStyle() ^ Font.BOLD));
		addConnection.addActionListener(controller);
		topologyControls.add(addConnection);

		topologyControls.add(Box.createVerticalStrut(5));

		JButton removeConnection = new JButton(REMOVE_CONNECTION_NAME);
		removeConnection.setActionCommand(REMOVE_CONNECTION);
		removeConnection.setFocusPainted(false);
		Font f6 = removeConnection.getFont();
		removeConnection.setFont(f6.deriveFont(f6.getStyle() ^ Font.BOLD));
		removeConnection.addActionListener(controller);
		topologyControls.add(removeConnection);

		topologyControls.add(Box.createVerticalStrut(40));

		JLabel metricsTitle = new JLabel(METRICS_TITLE);
		metricsTitle.setFont(headerFont);
		topologyControls.add(metricsTitle);

		topologyControls.add(Box.createVerticalStrut(20));

		JLabel totalPacketsMetricTitle = new JLabel(TOTAL_PACKETS_TRANSMITTED_TITLE);
		totalPacketsMetricTitle.setFont(subHeaderFont);
		topologyControls.add(totalPacketsMetricTitle);

		topologyControls.add(Box.createVerticalStrut(5));

		totalPacketsMetricValue = new JTextField();
		totalPacketsMetricValue.setEditable(false);
		totalPacketsMetricValue.setBackground(UIManager.getColor("TextField.background"));
		totalPacketsMetricValue.setHorizontalAlignment(JTextField.CENTER);

		Dimension maxTextFieldSize = new Dimension(260, 30);
		totalPacketsMetricValue.setMaximumSize(maxTextFieldSize);
		totalPacketsMetricValue.setText("0"); // initial display value before getting an update from model
		topologyControls.add(totalPacketsMetricValue);

		topologyControls.add(Box.createVerticalStrut(10));

		JLabel averageHopsMetricTitle = new JLabel(AVERAGE_NUMBER_OF_HOPS_TITLE);
		averageHopsMetricTitle.setFont(subHeaderFont);
		topologyControls.add(averageHopsMetricTitle);

		topologyControls.add(Box.createVerticalStrut(5));

		averageHopsMetricValue = new JTextField();
		averageHopsMetricValue.setEditable(false);
		averageHopsMetricValue.setBackground(UIManager.getColor("TextField.background"));
		averageHopsMetricValue.setHorizontalAlignment(JTextField.CENTER);
		averageHopsMetricValue.setMaximumSize(maxTextFieldSize);
		averageHopsMetricValue.setText("0.0"); // initial display value before getting an update from model
		topologyControls.add(averageHopsMetricValue);

		addConnection.setMaximumSize(maxButtonSize);
		removeConnection.setMaximumSize(maxButtonSize);

		topologyControls.setBorder(new CompoundBorder(new LineBorder(Color.WHITE), new EmptyBorder(10, 10, 10, 10)));

		add(topologyControls, BorderLayout.EAST);
	}

	/**
	 * Shows the user manual to the user, containing instructions for using the GUI
	 */
	public void showUserManual() {
		JFrame frame = new JFrame(USER_MANUAL_NAME);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		JEditorPane editorPane = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(editorPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		URL webpageURL = getClass().getResource(USER_MANUAL_RELATIVE_FILE_PATH);
		try {
			editorPane.setPage(webpageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		editorPane.setEditable(false);
		frame.add(scrollPane);
		frame.setIconImages(frameIcons);
		frame.setVisible(true);
	}

	/**
	 * Shows the about to the user, which contains all group members and the version
	 */
	public void showAbout() {
		JFrame frame = new JFrame(ABOUT_NAME);
		frame.setSize(250, 250);
		frame.setLocationRelativeTo(null);
		JEditorPane editorPane = new JEditorPane();
		URL webpageURL = getClass().getResource(ABOUT_RELATIVE_FILE_PATH);
		try {
			editorPane.setPage(webpageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		editorPane.setEditable(false);
		frame.add(editorPane);
		frame.setIconImages(frameIcons);
		frame.setVisible(true);
	}

	/**
	 * Creates a new node at the given position on the GUI
	 * @param x the x coordinate to create the new node on
	 * @param y the y coordinate to create the new node on
	 * @param name the new name of the new node
	 */
	public void createNewNode(float x, float y, String name) {
		Node newNode = new Node(x,y);
		newNode.setName(name);
		centerTopologyPanelView.enqueueNewNode(newNode);
	}

	/**
	 * Handles the click of the Add Neighbour button
	 */
	public void addConnectionSetup() {
		centerTopologyPanelView.setIsAddingConnection(true);
		centerTopologyPanelView.clearSelected();
		centerTopologyPanelView.repaint();
	}

	/**
	 * @return the view containing the topology
	 */
	public TopologyPanel getTopologyPanel() {
		return centerTopologyPanelView;
	}

	/**
	 * Sets the listener for the add router button
	 * @param listenForAddButton the actionListener for the Add Router button
	 */
	public void addAddRouterListener(ActionListener listenForAddButton) {
		addRouter.addActionListener(listenForAddButton);
	}

	/**
	 * @param value the totalPacketsMetricValue to set
	 */
	private void updateTotalPacketsMetricValue(int value) {
		totalPacketsMetricValue.setText(Integer.toString(value));
	}

	/**
	 * @param value the averageHopsMetricValue to set
	 */
	private void updateAverageHopsMetricValue(float value) {
		averageHopsMetricValue.setText(Float.toString(value));
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an Observable object's
	 * notifyObservers method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the notifyObservers
	 */
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Model Updated View!");
		if (arg instanceof NetworkEvent) {
			NetworkEvent ne = (NetworkEvent) arg;
			NetworkTopology topology = ne.getTopology();
			int messageCount = ne.getMessageCount();
			int baseMessageCount = ne.getBaseMessageCount();
			System.out.println(topology.getRouters());
			if (messageCount != baseMessageCount) undoMenu.setEnabled(true);
			else undoMenu.setEnabled(false);
			if (ne.isRestoreEvent()) {
				// update from model after a restore was triggered
				// this simply loads the serialized shapes to the view panel
				// at their correct locations
				centerTopologyPanelView.update(ne.getShapesContainer());
			} else {
				// standard update from model
				centerTopologyPanelView.update(topology);
			}
			updateTotalPacketsMetricValue(topology.getTotalPacketsTransmittedMetric());
			updateAverageHopsMetricValue(topology.getAverageHopsMetric());
		}
	}
}

package Simulator;

/**
 * This class handles the setup of the application and sets the dependencies between
 * the model, view, and controller.
 * 
 * @author Zaidoon Abd Al Hadi, Aaron Bungay, Jaspreet Sanghra
 */
public class NetworkRoutingSimulatorLauncher {

	/**
	 * Starts the Network Routing Simulator Application
	 */
	public static void main(String args[]) {
		NetworkRoutingSimulatorModel model = new NetworkRoutingSimulatorModel();
		NetworkRoutingSimulatorController controller = new NetworkRoutingSimulatorController(model);
		NetworkRoutingSimulatorView view = new NetworkRoutingSimulatorView(controller);
		controller.addView(view);
		model.addObserver(view);
	}
}

package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Message;
import Model.NetworkTopology;
import Model.Router;

/**
 * Tests for the NetworkTopology class (ex: adding routers, removing routers,
 * adding neighbours, removing neighbours, etc)
 * 
 * @author Shawn Morreau, Zaidoon Abd Al Hadi
 */
public class NetworkTopologyTest {

	private NetworkTopology network,network2;
	private Router router, router2;
	private Set<Router> set;
	private boolean thisIsTrue;
	private Message message;

	@Before
	public void setUp() throws Exception {
		network = new NetworkTopology();
		network2 = new NetworkTopology();
		router = new Router("bob");
		router2 = new Router("jim");
		set = network.getRouters();
		network.addRouter(router);
		network2.addRouter(router);
		network2.addRouter(router2);
		message = new Message(0, null, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddRouter() {
		assertEquals("Topology size should be 1.", 1, set.size());
	}

	@Test
	public void testContains() {
		assertEquals("Topology should contain 'bob' router.", true, network2.contains("bob"));
	}

	@Test
	public void testRemoveRouter() {
		network.removeRouter("bob");
		assertEquals("Topology should be empty after removing router.", 0, network.getRouters().size());
	}

	@Test
	public void testGetRouter() {
		assertEquals("Should get the 'bob' router.", router, network.getRouter("bob"));
	}

	@Test
	public void testSetRouterNeighbour() {
		thisIsTrue = network2.setRouterNeighbour("bob", "jim");
		assertEquals("Router neighbour should be successfully set.", true, thisIsTrue);
	}

	@Test
	public void testRemoveNeighbour() {
		network2.setRouterNeighbour("bob", "jim");
		thisIsTrue = network2.removeNeighbour(router.getName(), router2.getName());
		assertEquals("Router should be succesfully removed.", true, thisIsTrue);
	}

	@Test
	public void testClearMessages() {
		router.storeMessage(message);
		network.clearMessages();
		ArrayList<Message> messages = router.getStoredMessages();
		assertEquals("Messages on router should be empty after clearing messages", 0, messages.size());
	}

	@Test
	public void testToString() {
		assertEquals("Router toString() should be equal to the router's name.", router.getName(), router.toString());
	}

	@Test
	public void testNumberOfRouters() {
		assertEquals("There should be 2 routers currently in the topology.", 2, network2.numberOfRouters());
	}

}

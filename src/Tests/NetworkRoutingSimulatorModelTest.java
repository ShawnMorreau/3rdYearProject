package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Router;
import Simulator.NetworkRoutingSimulatorModel;

/**
 * Tests for the acessor and mutator methods of the model (ex: adding routers, removing routers,
 * adding neighbours, removing neighbours, etc), also undo functionality
 * 
 * @author Jaspreet Sanghra, Shawn Morreau
 */
public class NetworkRoutingSimulatorModelTest {

	private NetworkRoutingSimulatorModel model;

	@Before
	public void setUp() throws Exception {
		System.out.println("Setting up ...");
		model = new NetworkRoutingSimulatorModel();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Tearing down ...");
		model = null;
	}

	@Test
	public void testGetMessageGenerationRate_ReturnsCorrectly() throws Exception {
		model.setMessageGenerationRate(5);
		assertTrue("The message generation rate should be 5", model.getMessageGenerationRate() == 5);

		model.setMessageGenerationRate(-3);
		assertTrue("The message generation rate should be 5.", model.getMessageGenerationRate() == 5);

		model.setMessageGenerationRate(0);
		assertTrue("The message generation rate should be 5.", model.getMessageGenerationRate() == 5);

		model.setMessageGenerationRate((float)0.5);
		assertTrue("The message generation rate should be 0.5.", model.getMessageGenerationRate() == 0.5);

		model.setMessageGenerationRate((float)-0.5);
		assertTrue("The message generation rate should be 0.5.", model.getMessageGenerationRate() == 0.5);
	}

	@org.junit.Test
	public void testSetMessageGenerationRate_SetsCorrectly() throws Exception {
		model.setMessageGenerationRate(6);
		assertTrue("The message generation rate should be 6", model.getMessageGenerationRate() == 6);

		model.setMessageGenerationRate(-5);
		assertTrue("The message generation rate should be 6.", model.getMessageGenerationRate() == 6);

		model.setMessageGenerationRate(0);
		assertTrue("The message generation rate should be 6.", model.getMessageGenerationRate()  == 6);

		model.setMessageGenerationRate((float)1.5);
		assertTrue("The message generation rate should be 1.5.", model.getMessageGenerationRate() == 1.5);

		model.setMessageGenerationRate((float)-1.5);
		assertTrue("The message generation rate should be 1.5.", model.getMessageGenerationRate() == 1.5);
	}

	@org.junit.Test
	public void testAttachNeighbour_WorksSuccessfully() throws Exception {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));

		String neighbourName = "anotherRouter";
		model.addRouter(new Router(neighbourName));
		assertTrue("Neighbour connection should be created successfully.", model.attachNeighbour(routerName, neighbourName));

		String specialName = "specialRouter";
		model.addRouter(new Router(specialName));
		assertTrue("Neighbour connection should be created successfully.", model.attachNeighbour(specialName, neighbourName));
		assertTrue("Neighbour connection should be created successfully.", model.attachNeighbour(routerName, specialName));
	}

	@org.junit.Test
	public void testAttachNeighbour_FailsGracefully() throws Exception {
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(null, null));

		String routerName = "newRouter";
		model.addRouter(new Router(routerName));
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(routerName, null));
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(null, routerName));
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(routerName, routerName));

		String neighbourName = "anotherRouter";
		model.addRouter(new Router(neighbourName));
		model.attachNeighbour(routerName, neighbourName);
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(routerName, neighbourName));
		assertFalse("Neighbour connection should NOT be created successfully.", model.attachNeighbour(neighbourName, routerName));
	}

	@Test
	public void testDeleteNeighbour_DeletesSuccessfully() {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));

		String anotherRouterName = "anotherRouter";
		model.addRouter(new Router(anotherRouterName));
		model.attachNeighbour(routerName, anotherRouterName);
		assertTrue("Neighbour connection should be deleted successfully.", model.deleteNeighbour(routerName, anotherRouterName));
	}

	@Test
	public void testDeleteNeighbour_FailsGracefully() {
		assertFalse("Neighbour connection should NOT be deleted successfully.", model.deleteNeighbour(null, null));
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));

		String anotherRouterName = "anotherRouter";
		model.addRouter(new Router(anotherRouterName));

		assertFalse("Neighbour connection should NOT be deleted successfully.", model.deleteNeighbour(routerName, null));
		assertFalse("Neighbour connection should NOT be deleted successfully.", model.deleteNeighbour(null, anotherRouterName));
		assertFalse("Neighbour connection should NOT be deleted successfully.", model.deleteNeighbour(routerName, anotherRouterName));
	}

	@org.junit.Test
	public void testAddRouter_AddsSuccessfully() throws Exception {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));
		assertTrue("Router '" + routerName + "' should exist in the model.", model.containsRouter(routerName));

		routerName = "another";
		model.addRouter(new Router(routerName));
		assertTrue("Router '" + routerName + "' should exist in the model.", model.containsRouter(routerName));

		assertEquals("There should be 2 routers in the model.", 2,  model.numberOfRouters());
	}

	@org.junit.Test
	public void testAddRouter_SkipsAddingDuplicateRouter() throws Exception {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));
		assertTrue("There should be 1 router in the model.", model.numberOfRouters() == 1);
		model.addRouter(new Router(routerName));
		assertEquals("There should be 1 router in the model.", 1,  model.numberOfRouters());
	}

	@org.junit.Test
	public void testRemoveRouter_RemovesSuccessfully() throws Exception {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));
		model.removeRouter(routerName);
		assertFalse("Router '" + routerName + "' should NOT exist in the model.", model.containsRouter(routerName));

		routerName = "another";
		model.addRouter(new Router(routerName));
		model.removeRouter(routerName);
		assertFalse("Router '" + routerName + "' should NOT exist in the model.", model.containsRouter(routerName));

		assertEquals("There should be 0 routers in the model.", 0,  model.numberOfRouters());
	}

	@org.junit.Test
	public void testContainsRouter_DoesNotFindGivenRouter() throws Exception {
		String routerName = "newRouter";
		assertFalse("Router 'newRouter' should not exist.", model.containsRouter(routerName));
		model.addRouter(new Router("otherRouter"));
		assertFalse("Router 'newRouter' should still not exist.", model.containsRouter(routerName));
	}
	
	@org.junit.Test
	public void testContainsRouter_GivenEmptyStringThenReturnsFalse() throws Exception {
		String routerName = "";
		assertFalse("Router '' should not exist.", model.containsRouter(routerName));
	}
	
	
	@org.junit.Test
	public void testContainsRouter_FindsExistingRouterSuccessfully() throws Exception {
		String routerName = "newRouter";
		model.addRouter(new Router(routerName));
		assertTrue("Router 'newRouter' should exist.", model.containsRouter(routerName));
		model.addRouter(new Router(routerName));
		assertTrue("Router 'newRouter' should still exist.", model.containsRouter(routerName));
	}
	
	@Test
	public void testNumberOfRouters_ReturnsCorrectly() {
		assertEquals("There should be 0 routers in the model.", 0,  model.numberOfRouters());

		String newRouterName = "newRouter";
		model.addRouter(new Router(newRouterName));
		assertEquals("There should be 1 router in the model.", 1,  model.numberOfRouters());

		String routerName = "anotherRouter";
		model.addRouter(new Router(routerName));
		assertEquals("There should be 2 routers in the model.", 2,  model.numberOfRouters());

		model.addRouter(new Router(routerName));
		assertEquals("There should be 2 routers in the model.", 2,  model.numberOfRouters());

		model.removeRouter(routerName);
		assertEquals("There should be 1 router in the model.", 1,  model.numberOfRouters());

		model.removeRouter(newRouterName);
		assertEquals("There should be 0 routers in the model.", 0,  model.numberOfRouters());
	}
	/**
	 * adds a router to the topology, simulates a step to get a message count of 1, then performs an undo to get a message count of 0
	 */
	@Test
	public void testUndo(){
		model.addRouter(new Router("a"));
		model.step();
		assertEquals("There should be 1 message", 1, model.getMessageCount());
		model.stepBack();
		assertEquals("There should be 0 messages", 0, model.getMessageCount());
	}
}

package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Message;
import Model.Router;

/**
 * Tests for the behaviour of the messages in the simulation.
 * 
 * @author Jaspreet Sanghra
 */
public class MessageTest {

	private Message message;
	private Router router1;
	private Router router2;
	private final String ROUTER1_NAME = "router1";
	private final String ROUTER2_NAME = "router2";

	@Before
	public void setUp() throws Exception {
		System.out.println("Setting up ...");
		router1 = new Router(ROUTER1_NAME);
		router2 = new Router(ROUTER2_NAME);
		message = new Message(0, router1, router2);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Tearing down ...");
		router1 = null;
		router2 = null;
		message = null;
	}

	@Test
	public void testGetSource_ReturnsRouterSuccessfully() {
		assertEquals("Source router should be '" + ROUTER1_NAME + "'", router1, message.getSource());

		message = new Message(1, null, router2);
		assertEquals("Source router should be ''", null, message.getSource());
	}

	@Test
	public void testGetPreviousDestination_ReturnsCorrectly() {
		assertEquals("Previous destination should be 'null'", null, message.getPreviousDestination());

		message.setPreviousDestination(router2);
		assertEquals("Previous destination should be 'null'", router2, message.getPreviousDestination());

		message.setPreviousDestination(router1);
		assertEquals("Previous destination should be 'null'", router1, message.getPreviousDestination());
	}

	@Test
	public void testSetPreviousDestination_SetCorrectly() {
		message.setPreviousDestination(router1);
		assertEquals("Previous destination should be 'null'", router1, message.getPreviousDestination());

		message.setPreviousDestination(router2);
		assertEquals("Previous destination should be 'null'", router2, message.getPreviousDestination());	
	}

	@Test
	public void testGetDestination_ReturnsRouterSuccessfully() {
		assertEquals("Source router should be '" + ROUTER1_NAME + "'", router2, message.getDestination());
		message = new Message(1, null, router1);
		assertEquals("Source router should be '" + ROUTER2_NAME + "'", router1, message.getDestination());
		message = new Message(2, router1, null);
		assertEquals("Source router should be ''", null, message.getDestination());
	}

	@Test
	public void testGetID() {
		assertEquals("message id is set to 0", 0, message.getID());
	}

	@Test
	public void testGetSteps() {
		assertEquals("message hasn't moved", 0, message.getSteps());
	}

	@Test
	public void testIncrementSteps() {
		message.incrementSteps();
		assertEquals("step once", 1, message.getSteps());
	}

}

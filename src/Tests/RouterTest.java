package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Message;
import Model.Router;

/**
 * Tests for the Router class.
 * 
 * @author Jaspreet Sanghra
 */
public class RouterTest {

	private Router router = null;
	private final String ROUTER_NAME = "newRouter";

	@Before
	public void setUp() throws Exception {
		System.out.println("Setting up ...");
		router = new Router(ROUTER_NAME);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Tearing down ...");
		router = null;
	}

	@Test
	public void testGetName_ReturnsSuccessfully() {
		assertEquals("Router Name should be '" + ROUTER_NAME + "'", ROUTER_NAME, router.getName());

		Router empty = new Router("");
		assertEquals("Router Name should be ''", "", empty.getName());

		Router whiteSpace = new Router("  ");
		assertEquals("Router Name should be '  '", "  ", whiteSpace.getName());
	}

	@Test
	public void testStoreMessage_AddsSuccessfully() {
		assertEquals("Message List size should be '0'", 0, router.getStoredMessages().size());

		Message msg = new Message(0, null, null);
		router.storeMessage(msg);
		assertEquals("Message List size should be '1'", 1, router.getStoredMessages().size());

		msg = new Message(1, null, null);
		router.storeMessage(msg);
		assertEquals("Message List size should be '2'", 2, router.getStoredMessages().size());
	}

	@Test
	public void testRemoveMessage_DeletesSuccessfully() {
		Message msg = new Message(0, null, null);
		router.storeMessage(msg);
		router.removeMessage(msg);
		assertEquals("Message List size should be '0'", 0, router.getStoredMessages().size());

		msg = new Message(1, null, null);
		router.storeMessage(msg);
		router.removeMessage(msg);
		assertEquals("Message List size should be '0'", 0, router.getStoredMessages().size());

		router.removeMessage(null);
		assertEquals("Message List size should be '0'", 0, router.getStoredMessages().size());
	}

	@Test
	public void testRemoveAllMessages() {
		Message msg = new Message(0, null, null);
		router.storeMessage(msg);
		msg = new Message(1, null, null);
		router.storeMessage(msg);
		router.removeAllMessages();
		assertEquals("Message List size should be '0'", 0, router.getStoredMessages().size());
	}

	@Test
	public void testToString() {
		assertEquals("Router Name should be '" + ROUTER_NAME + "'", ROUTER_NAME, router.toString());

		Router empty = new Router("");
		assertEquals("Router Name should be ''", "", empty.toString());

		Router whiteSpace = new Router("  ");
		assertEquals("Router Name should be '  '", "  ", whiteSpace.toString());
	}

}

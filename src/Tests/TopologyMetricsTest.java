package Tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Message;
import Model.NetworkTopology;

/**
 * Tests for the topology metrics.
 * 
 * @author Shawn Morreau, Aaron Bungay
 *
 */
public class TopologyMetricsTest {

	private NetworkTopology network;
	private Message message;

	@Before
	public void setUp() throws Exception {
		network = new NetworkTopology();
		message = new Message(0, null, null);
		message.incrementSteps();
		message.incrementSteps();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Ensure average hops metric starts at 0
	 */
	@Test
	public void testAverageHopsMetric(){
		assertEquals("Average hops should start at 0", 0, network.getAverageHopsMetric(), 0.0);
	}

	/**
	 * Ensure total packets transmitted starts at 0
	 */
	@Test
	public void testGetTotalPacketsTransmittedMetric(){
		assertEquals("Metrics should all be 0", 0, network.getTotalPacketsTransmittedMetric());
	}

	/**
	 * Ensure total packets transmitted gets updated on an increment call
	 */
	@Test
	public void testIncrementTotalPacketsTransmittedMetric() {
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementTotalPacketsTransmittedMetric();
		assertEquals("Metrics should all be 3", 3, network.getTotalPacketsTransmittedMetric());
	}

	/**
	 * Ensure the reset call for the metrics works properly
	 */
	@Test
	public void testResetMetrics() {
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementNumMessagesReachedDest();
		network.calculateAverageHopsMetric();
		network.resetMetrics();
		assertEquals("Total packets transmitted metric should be 0", 0, network.getTotalPacketsTransmittedMetric());
		assertEquals("Average hops metric should be 0", 0, network.getAverageHopsMetric(), 0.0);
	}

	/**
	 * Test calculating the average message hops metric
	 */
	@Test
	public void testCalculateAverageHopsMetric() {
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementNumMessagesReachedDest();
		network.calculateAverageHopsMetric();
		assertEquals("Average should be 2", 2, network.getAverageHopsMetric(), 0.0);
	}

	/**
	 * Test getting the average hops metric
	 */
	@Test
	public void testGetAverageHopsMetric()  {
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementTotalPacketsTransmittedMetric();
		network.incrementNumMessagesReachedDest();
		network.calculateAverageHopsMetric();
		assertEquals("Average should be 2", 2, network.getAverageHopsMetric(), 0.0);
	}

}

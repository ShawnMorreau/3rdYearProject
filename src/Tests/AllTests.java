package Tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A Test Suite to run all the tests for the network routing simulator application.
 * 
 * @author Aaron Bungay
 * @version 0.3
 */
@RunWith(Suite.class)
@SuiteClasses(
		{ MessageTest.class, NetworkRoutingSimulatorModelTest.class, NetworkTopologyTest.class, 
			RouterTest.class, TopologyMetricsTest.class, XMLTest.class })
public class AllTests {}

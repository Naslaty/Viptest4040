package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scenario6 {

	private static Logger logger = LoggerFactory.getLogger(Scenario6.class);

	public static void main(String[] args) throws Exception {

		String apikey = System.getProperty("apikey");
		logger.debug("Launch scenario 6");
		logger.info("waited result: false");
		Scenario scenarioTest6 = new Scenario();
		logger.info("Scenario 6 result: {}",scenarioTest6.scenario6(apikey));
	}
}
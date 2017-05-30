package auto_test.vip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scenario5 {
	
	private static Logger logger = LoggerFactory.getLogger(Scenario5.class);

	public static void main(String[] args) throws Exception {

		String apikey = System.getProperty("apikey");
		logger.debug("Launch scenario 5");
		logger.info("waited result: false");
		Scenario scenarioTest5 = new Scenario();
		logger.info("Scenario 5 result: {}",scenarioTest5.scenario5(apikey));
	}
}

package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;
import io.swagger.client.model.Execution.StatusEnum;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Scenario3 {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(Scenario3.class);
	
	
	//tries kill a bugged execution end restart it
	public boolean scenario3(String key) throws Exception{		
		//create and start the execution
		Execution body = vth.initExecution("newScenarioKo", 1, 2);
		Execution result = client.initAndStartExecution(body);
		String resId = result.getIdentifier();				
		assertThat("the execution is not launched", result.getStatus(), is(StatusEnum.RUNNING));
		
		//execution history
		client.listExecutions();

		//kill the bugged execution and check its status
		client.killExecution(resId);
		result = client.getExecution(resId);
		assertThat("the execution is on killed", result.getStatus(), is(StatusEnum.KILLED));
		
		Boolean test1 = result.getStatus().equals(StatusEnum.KILLED);
		
		//create and restart the execution check its status
		body = vth.initExecution("newScenario3", 1, 2);
		result = client.initAndStartExecution(body);
		assertThat("the execution has not been launched", result.getStatus(), is(StatusEnum.RUNNING));
		resId = result.getIdentifier();
		client.killExecution(resId);
		
		boolean test2 = result.getStatus().equals(StatusEnum.RUNNING);
		return test1 && test2;
	}
	
	public static void main(String args[])throws Exception{
		
		String apikey = System.getProperty("apikey");
		Scenario3 scenarioTest3 = new Scenario3();
		logger.debug("Launch scenario 3");
		logger.info("waited result: true");		
		logger.info("Scenario 3 result: {}",scenarioTest3.scenario3(apikey));

	}
}

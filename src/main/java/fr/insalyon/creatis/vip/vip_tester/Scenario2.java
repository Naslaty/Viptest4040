package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.model.Execution;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Scenario2 {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private static Logger logger = LoggerFactory.getLogger(Scenario2.class);
		
	//tries to modify an execution by changing name 
	public boolean scenario2(String key) throws Exception{			
		//execution history
		String exeId = vth.defaultApiClient.listExecutions().iterator().next().getIdentifier();
		logger.debug("1");
		assertNotNull("the execution Id is null",exeId);
		
		//check a particular execution
		Execution result1 = vth.defaultApiClient.getExecution(exeId);
		logger.debug("2");
		assertNotNull("the execution Id is null",result1);
		
		//modification of name parameter of the execution
		String newName = vth.randomSelection();
		logger.debug("3");
		Execution body = vth.modifExecution(newName, 0L, vth.getAdditionTestPipelineIdString());
		vth.defaultApiClient.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = vth.defaultApiClient.getExecution(exeId);
		assertNotNull("the execution Id is null",result2);
		
		boolean cond = !(result1.getName().equals(result2.getName())) || 
					   !(result1.getTimeout().equals(result2.getTimeout()));
		
		assertThat("Timeout and name not modified", cond, is(true));

		return cond;
	}
	
	public static void main(String[] args) throws Exception{
		
		String apikey = System.getProperty("apikey");
		logger.debug("Launch scenario 2");
		logger.info("waited result: true");
		Scenario2 scenarioTest2 = new Scenario2();
		logger.info("Scenario 2 result: {}",scenarioTest2.scenario2(apikey));
	}
}

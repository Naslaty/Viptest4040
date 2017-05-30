package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ScenarioUpdateExe {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(ScenarioUpdateExe.class);
		
	//tries to modify an execution by changing name 
	public boolean scenario2( String key) throws Exception{			
		//execution history
		String exeId = client.listExecutions().iterator().next().getIdentifier();
		assertNotNull("the execution Id is null",exeId);
		
		//check a particular execution
		Execution result1 = client.getExecution(exeId);
		assertNotNull("the execution is null",result1);
		
		//modification of name parameter of the execution
		String newName = vth.randomSelection();
		Execution body = vth.modifExecution(newName, 0L);
		client.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = client.getExecution(exeId);
		assertNotNull("the execution is null",result2);
		
		boolean cond = !(result1.getName().equals(result2.getName())) || 
					   !(result1.getTimeout().equals(result2.getTimeout()));
		
		assertThat("Timeout and name not modified", cond, is(true));
		return cond;
	}
	
	//tries to modify an execution but put the same name and the same timeout so NO modification
	public boolean scenario5(String key) throws Exception{			
		//find an execution to modify it
		String exeId = client.listExecutions().iterator().next().getIdentifier();
		assertNotNull("the execution Id is null",exeId);
		
		//check a particular execution
		Execution result1 = client.getExecution(exeId);
		assertNotNull("the execution is null",result1);
		
		//modification of name parameter of the execution
		Execution body = vth.modifExecution(result1.getName(), result1.getTimeout());
		client.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = client.getExecution(exeId);
		assertNotNull("the execution is null",result2);
		
		boolean cond = (result1.getName().equals(result2.getName())) &&
					   (result1.getTimeout().equals(result2.getTimeout()));
		return cond;
	}
	
	public static void main(String[] args) throws Exception{
		
		ScenarioUpdateExe scenarioTest2 = new ScenarioUpdateExe();
		String apikey = System.getProperty("apikey");
		logger.debug("Launch scenario 2");
		logger.info("waited result: true");
		logger.info("Scenario 2 result: {}",scenarioTest2.scenario2(apikey));
		
		logger.debug("Launch scenario 5");
		logger.info("waited result: true");
		logger.info("Scenario 2 result: {}",scenarioTest2.scenario2(apikey));
	}
}

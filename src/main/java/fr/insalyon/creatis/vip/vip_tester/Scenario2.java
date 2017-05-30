package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;

public class Scenario2 {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private static Logger logger = LoggerFactory.getLogger(Scenario2.class);
		
	//tries to modify an execution by changing name 
	public boolean scenario2(String key) throws Exception{			
		//execution history
		String exeId = vth.defaultApiClient.listExecutions().iterator().next().getIdentifier();
		
		//check a particular execution
		Execution result1 = vth.defaultApiClient.getExecution(exeId);
		
		//modification of name parameter of the execution
		String newName = appScenario.randomSelection();
		Execution body = appScenario.modifExecution(newName, 0L, prop.getProperty("viptest.additiontest.pipelineidentifierString"));
		vth.defaultApiClient.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = defaultApiClient2.getExecution(exeId);

		return !(result1.getName().equals(result2.getName())) || !(result1.getTimeout().equals(result2.getTimeout()));
	}
	
	public static void main(String[] args) throws Exception{
		
		String apikey = System.getProperty("apikey");
		logger.debug("Launch scenario 2");
		logger.info("waited result: true");
		Scenario scenarioTest2 = new Scenario();
		System.out.println(scenarioTest2.scenario2(apikey));
		logger.info("Scenario 2 result: {}",scenarioTest2.scenario2(apikey));

	}
}

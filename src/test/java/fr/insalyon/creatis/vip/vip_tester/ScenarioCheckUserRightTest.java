package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;
import io.swagger.client.model.Execution.StatusEnum;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class ScenarioCheckUserRightTest {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(ScenarioCheckUserRight.class);
	
	@Test
	public void scenario6() throws Exception{	
		//create and start an execution
		Execution body = vth.initExecution("newScenario3", 1, 2);
		Execution result = client.initAndStartExecution(body);
		assertThat("the execution is not running", result.getStatus(), is(StatusEnum.RUNNING));
		String resId = result.getIdentifier(); 
		
		//create and start another execution
		try{
			body = vth.initExecution("newScenario3", 1, 2);
			result = client.initAndStartExecution(body);
		}catch(ApiException ae){
			client.killExecution(resId);
			return;
		}
		client.killExecution(resId);
		client.killExecution(result.getIdentifier());
		throw new RuntimeException(" with that type of right you can't launch 2 execution in the same time");

	}
}
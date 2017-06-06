package fr.insalyon.creatis.vip.vip_tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ScenarioUpdateExeTest {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(ScenarioUpdateExe.class);
		
	//tries to modify an execution by changing name 
	@Test
	public void scenario2() throws Exception{			
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
	}
	
	//tries to modify an execution which doesn't exist
	@Test(expected = ApiException.class)
	public void scenario5() throws Exception{			
		//find an execution to modify it
		String exeId = vth.randomSelection();
		assertNotNull("the execution Id is null",exeId);
		
		//check a particular execution
		client.getExecution(exeId);
		throw new RuntimeException("That particular execution doesn't exist");	
	}
}

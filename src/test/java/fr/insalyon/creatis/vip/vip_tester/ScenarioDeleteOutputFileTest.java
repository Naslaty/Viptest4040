package fr.insalyon.creatis.vip.vip_tester;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.DeleteExecutionConfiguration;
import io.swagger.client.model.Execution;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ScenarioDeleteOutputFileTest {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(ScenarioKillAndRestart.class);
	
	@Test
	public void scenario4() throws Exception{
		boolean bool = true;
		String exeId = null;
		//delete phase initialization
		DeleteExecutionConfiguration body = new DeleteExecutionConfiguration();
		body.setDeleteFiles(true);
		//Class c = body.getClass();
		//assertThat("body is not a deleteExecutionConfig class", c, is(DeleteExecutionConfiguration.class));
		
		//execution history
		Iterator<Execution> list = client.listExecutions().iterator();
		while(list.hasNext() && bool){
			Execution e= (Execution) list.next();
			if(e.getStatus().toString().equals("finished") && client.getExecution(e.getIdentifier())!=null){ 
				exeId = e.getIdentifier();
				client.deleteExecution(exeId, body);
				bool = false;
			}
		}
	}
}
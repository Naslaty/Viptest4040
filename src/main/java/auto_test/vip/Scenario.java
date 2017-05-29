package auto_test.vip;

import java.util.Iterator;
import java.util.Properties;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.DeleteExecutionConfiguration;
import io.swagger.client.model.Execution;


public class Scenario {
	private App appScenario = new App();
	Properties prop = appScenario.propertiesExtraction();

	//tries to launch an execution an waits the end of it
	public boolean scenario1(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient1 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//pipelines list
		defaultApiClient1.listPipelines("");
		
		//check parameters for a specified pipeline
		defaultApiClient1.getPipeline(prop.getProperty("viptest.additiontest.pipelineidentifier"));
		
		//create and start an execution
		Execution result = defaultApiClient1.initAndStartExecution(appScenario.initExecution("testScenario1", prop.getProperty("viptest.additiontest.pipelineidentifierString"), 40, 41));
				
		//keep the identifier
		String exeId = result.getIdentifier();
		
		while(result.getStatus().toString().equals("running")){
			Thread.sleep(Long.valueOf(prop.getProperty("viptest.additiontest.timecheck")));
			//check the execution
			result = defaultApiClient1.getExecution(exeId);
		}
		
		return result.getStatus().toString().equals("finished");
	}

	//tries to modify an execution by changing name 
	public boolean scenario2(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient2 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//execution history
		String exeId = defaultApiClient2.listExecutions().iterator().next().getIdentifier();
		
		//check a particular execution
		Execution result1 = defaultApiClient2.getExecution(exeId);
		
		//modification of name parameter of the execution
		String newName = appScenario.randomSelection();
		Execution body = appScenario.modifExecution(newName, 0L, prop.getProperty("viptest.additiontest.pipelineidentifierString"));
		defaultApiClient2.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = defaultApiClient2.getExecution(exeId);

		return !(result1.getName().equals(result2.getName())) || !(result1.getTimeout().equals(result2.getTimeout()));
	}
	
	//tries kill a bugged execution end restart it
	public boolean scenario3(String key) throws Exception{
		//Client initialization
		DefaultApi defaultApiClient3 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);		
		
		//create and start the execution
		Execution body = appScenario.initExecution("testOldScenario3", prop.getProperty("viptest.additiontest.pipelineidentifierString"), 1, 2); 
		Execution result = defaultApiClient3.initAndStartExecution(body);
		String resId = result.getIdentifier();
				
		//execution history
		defaultApiClient3.listExecutions();

		//kill the bugged execution
		defaultApiClient3.killExecution(resId);
		
		//check the status execution
		result = defaultApiClient3.getExecution(resId);
		
		Boolean test1 = result.getStatus().toString().equals("killed");
		
		//create and restart the execution
		body = appScenario.initExecution("testNewScenario3", prop.getProperty("viptest.additiontest.pipelineidentifierString"), 1, 2);
		result = defaultApiClient3.initAndStartExecution(body);
		resId = result.getIdentifier();
		defaultApiClient3.killExecution(resId);

		boolean test2 = result.getStatus().toString().equals("running");
		return test1 && test2;
	}
	
	// tries to delete the output file of an execution
	public boolean scenario4(String key) throws Exception{
		//Client initialization
		DefaultApi defaultApiClient4 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);

		boolean bool = true;
		String exeId = null;
		//delete phase initialization
		
		DeleteExecutionConfiguration body = new DeleteExecutionConfiguration();
		body.setDeleteFiles(true);

		//execution history
		Iterator<Execution> list = defaultApiClient4.listExecutions().iterator();
		while(list.hasNext() && bool){
			Execution e= (Execution) list.next();
			if(e.getStatus().toString().equals("finished") && defaultApiClient4.getExecution(e.getIdentifier())!=null){ 
				exeId = e.getIdentifier();
				defaultApiClient4.deleteExecution(exeId, body);
				bool = false;
			}
		}

		return defaultApiClient4.getExecution(exeId)==null;
	}
		
	// tries to modify but put the same name and the same timeout
	public boolean scenario5(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient5 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//find an execution to modify it
		String exeId = defaultApiClient5.listExecutions().iterator().next().getIdentifier();
		
		//check a particular execution
		Execution result1 = defaultApiClient5.getExecution(exeId);
		
		//modification of name parameter of the execution
		Execution body = appScenario.modifExecution(result1.getName(), result1.getTimeout(), prop.getProperty("viptest.additiontest.pipelineidentifierString"));
		defaultApiClient5.updateExecution(exeId, body);
		
		//check execution modification
		Execution result2 = defaultApiClient5.getExecution(exeId);

		return !(result1.getName().equals(result2.getName()) || result1.getTimeout().equals(result2.getTimeout()));
	}
	
	// tries to launch two Execution without good right
	public boolean scenario6(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient6 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//create and start an execution
		Execution body = appScenario.initExecution("newScenario3.1", prop.getProperty("viptest.additiontest.pipelineidentifierString"), 1, 2);
		Execution result = defaultApiClient6.initAndStartExecution(body);
		String resId = result.getIdentifier(); 
		
		//create and start another execution
		try{
		body = appScenario.initExecution("newScenario3.2", prop.getProperty("viptest.additiontest.pipelineidentifierString"), 1, 2);
		result = defaultApiClient6.initAndStartExecution(body);
		}catch(ApiException ae){
			defaultApiClient6.killExecution(resId);
			return false;
		}
		defaultApiClient6.killExecution(resId);
		return true;
	}
}

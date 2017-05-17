package auto_test.vip;

import java.util.List;
import java.util.Iterator;
import java.util.Properties;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;


public class Scenario {
	private App appScenario = new App();
	Properties prop = appScenario.propertiesExtraction();

	public boolean scenario1(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient1 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//pipelines list
		System.out.println(defaultApiClient1.listPipelines(""));
		
		//check parameters for a specified pipeline
		System.out.println(defaultApiClient1.getPipeline(prop.getProperty("viptest.additiontest.pipelineidentifier")));
		
		//create and start an execution
		Execution result = defaultApiClient1.initAndStartExecution(appScenario.initExecution("test_335", "AdditionTest/0.9", 40, 41));
		System.out.println("result: "+result);
				
		//check the execution
		System.out.println("result: "+defaultApiClient1.getExecution(result.getIdentifier()));
		
		while(result.getStatus().toString().equals("running")){
			Thread.sleep(Long.valueOf(prop.getProperty("viptest.additiontest.timecheck")));
		
			//check the execution
			result = defaultApiClient1.getExecution(result.getIdentifier());
			System.out.println("result: "+result);
		}
		
		return result.getStatus().toString().equals("finished");
	}

	public boolean scenario2(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient2 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//execution history
		//System.out.println(defaultApiClient2.listExecutions());
		
		//check a particular execution
		Execution result1 = defaultApiClient2.getExecution("workflow-PvlSwI");
		//System.out.println(result1);
		
		//modification of name parameter of the execution
		String newName = appScenario.randomSelection();
		Execution body = appScenario.modifExecution(newName, 0L, "AdditionTest/0.9");
		defaultApiClient2.updateExecution("workflow-PvlSwI", body);
		
		//check execution modification
		Execution result2 = defaultApiClient2.getExecution("workflow-PvlSwI");
		//System.out.println(result2);

		return !(result1.getName().equals(result2.getName())) || !(result1.getTimeout().equals(result2.getTimeout()));
	}
	
	public boolean scenario3(String key) throws Exception{
		//Client initialization
		DefaultApi defaultApiClient3 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);		
		
		//create and restart the execution
		Execution body = appScenario.initExecution("newScenarioKo", "AdditionTest/0.9", 1, 2);
		Execution result = defaultApiClient3.initAndStartExecution(body);
		String resId = result.getIdentifier();
				
		//execution history
		List<Execution> list = defaultApiClient3.listExecutions();
		boolean b = true;
		Iterator it = list.iterator();
		while(it.hasNext()){
			Execution e= (Execution) it.next();
			if(e.getIdentifier().equals(resId)){
				defaultApiClient3.killExecution(resId);
			}
			//System.out.println("Identifier: "+e.getIdentifier()+"  status: "+e.getStatus());
		}
		
		//check the execution
		result = defaultApiClient3.getExecution(resId);
		//System.out.println("result: "+result);
		
		Boolean test1 = result.getStatus().toString().equals("killed");
		
		//create and restart the execution
		body = appScenario.initExecution("newScenario3", "AdditionTest/0.9", 1, 2);
		result = defaultApiClient3.initAndStartExecution(body);
		resId = result.getIdentifier();
		defaultApiClient3.killExecution(resId);

		boolean test2 = result.getStatus().toString().equals("running");
		//System.out.println(test1 + " " + test2);
		return test1 && test2;
	}
	
	public boolean scenario4() throws Exception{
		return false;
	}
	
	
	// try to modify but put the same name and the same timeout
	public boolean scenario5(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient5 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//execution history
		System.out.println(defaultApiClient5.listExecutions());
		
		//check a particular execution
		Execution result1 = defaultApiClient5.getExecution("workflow-ulum4P");
		System.out.println(result1);
		
		//modification of name parameter of the execution
		Execution body = appScenario.modifExecution("test_335", 0L, "AdditionTest/0.9");
		defaultApiClient5.updateExecution("workflow-8CsWAA", body);
		
		//check execution modification
		Execution result2 = defaultApiClient5.getExecution("workflow-8CsWAA");
		System.out.println(result2);

		return !(result1.getName().equals(result2.getName()) || result1.getTimeout().equals(result2.getTimeout()));
	}
	
	// try to launch two Execution without good right
	public boolean scenario6(String key) throws Exception{	
		//Client initialization
		DefaultApi defaultApiClient6 = appScenario.initClient(prop.getProperty("viptest.additiontest.url"), key);
		
		//create and start an execution
		Execution body = appScenario.initExecution("newScenario3", "AdditionTest/0.9", 1, 2);
		Execution result = defaultApiClient6.initAndStartExecution(body);
		String resId = result.getIdentifier(); 
		//create and start another execution
		try{
		body = appScenario.initExecution("newScenario3", "AdditionTest/0.9", 1, 2);
		result = defaultApiClient6.initAndStartExecution(body);
		}catch(ApiException ae){
			defaultApiClient6.killExecution(resId);
			return false;
		}
		defaultApiClient6.killExecution(resId);
		return true;
	}
}

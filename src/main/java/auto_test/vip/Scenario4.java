package auto_test.vip;

import java.util.Properties;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;

public class Scenario4 {
	
	public static void main(String[] args) throws Exception{
		
		App scenario4 = new App();
		
		//properties extraction
		Properties prop = scenario4.propertiesExtraction();
		
		//Client initialization
		DefaultApi defaultApiClient4 = scenario4.initClient(prop.getProperty("url"), prop.getProperty("apiKey"));				
		System.out.println("***************************************************");
		
		//execution history
		System.out.println(defaultApiClient4.listExecutions());
		System.out.println("***************************************************");
		
		//check a particular execution
		Execution result = defaultApiClient4.getExecution("workflow-ulum4P");
		System.out.println(result);
		System.out.println("***************************************************");
		
		if(result.getStatus().toString().equals("finished")){
			//to get result file name
			System.out.println(defaultApiClient4.getExecutionResults("workflow-PvlSwI", ""));
			System.out.println("***************************************************");
			
			//to get stdout
			System.out.println(defaultApiClient4.getStdout("workflow-PvlSwI"));
			System.out.println("***************************************************");
			
			//to get stderr
			System.out.println(defaultApiClient4.getStderr("workflow-PvlSwI"));
			System.out.println("***************************************************");
		}
		
		else{
			System.out.println("the execution is not finished");
		}
	}
}

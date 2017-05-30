package fr.insalyon.creatis.vip.vip_tester;

import io.swagger.client.model.Execution;
import io.swagger.client.model.Execution.StatusEnum;
import io.swagger.client.model.ParameterType;
import io.swagger.client.model.Pipeline;
import io.swagger.client.model.PipelineParameter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scenario1 {
	VipTesterHelper vth = new VipTesterHelper();
	private static Logger logger = LoggerFactory.getLogger(Scenario1.class);
	
	//tries to launch an execution an waits the end of it
	public boolean scenario1(String key) throws Exception{	
		
		//pipelines list
		vth.defaultApiClient.listPipelines(null);
		// TODO : assert that the additionTest pipeline is present
		
		//check parameters for a specified pipeline
		Pipeline pipelineResult = vth.defaultApiClient.getPipeline(vth.getAdditionTestPipelineId());
		List<PipelineParameter> pipelineParam = pipelineResult.getParameters();
		logger.debug("size of list Pipeline parameter: {}", pipelineParam.size());
		assert pipelineParam.size() == 3 : "It must have 3 parameters";
		//pipelineResult.getParameters().iterator();
		int cmptInt = 0;
		for(PipelineParameter pp : pipelineResult.getParameters()){
			if(!(pp.getName().equals("results-directory"))){
				if(pp.getType().equals(ParameterType.STRING)){
					cmptInt++;
				}
			}
			logger.debug("type of parameters: {}",pp.getType());
		}
		cmptInt = 5;
		assert cmptInt == 2 : "It must have 2 parameters for the addition";
		// TODO : assert that the number and type of parameters are good
		
		//create and start an execution
		Execution result = vth.defaultApiClient.initAndStartExecution(vth.initExecution("testScenario1", 40, 41));
		// TODO : assert that the status is running
		
		//keep the identifier
		String exeId = result.getIdentifier();
		
		// TODO : use  executorService to wait
		while(result.getStatus().equals(StatusEnum.RUNNING)){
			Thread.sleep(Long.valueOf(vth.getAdditionTestTimeCheck()));
			//check the execution
			result = vth.defaultApiClient.getExecution(exeId);
			
		}
		
		assert result.getStatus().equals(StatusEnum.FINISHED) : "The status must be \"finished\" but it is" + result.getStatus();
		
		return result.getStatus().equals(StatusEnum.FINISHED);
		// TODO : assert that the status is finished
	}
		
	public static void main(String[] args) throws Exception{

		String apikey = System.getProperty("apikey");
		Scenario1 scenarioTest1 = new Scenario1();
		logger.debug("Launch scenario 1");
		logger.info("waited result: true");		
		logger.info("Scenario 1 result: {}",scenarioTest1.scenario1(apikey));

	}
}

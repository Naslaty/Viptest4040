package fr.insalyon.creatis.vip.vip_tester;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;
import io.swagger.client.model.Execution.StatusEnum;
import io.swagger.client.model.ParameterType;
import io.swagger.client.model.Pipeline;
import io.swagger.client.model.PipelineParameter;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Scenario1 {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(Scenario1.class);
	
	//tries to launch an execution an waits the end of it
	public boolean scenario1(String key) throws Exception{	
		
		//pipelines list
		Iterator<Pipeline> listPipelineResultIt = client.listPipelines(null).iterator();
		boolean bool = true;
		String pipelineId = null;
		while(bool && listPipelineResultIt.hasNext()){
			Pipeline p = listPipelineResultIt.next();
			pipelineId = p.getIdentifier();
			if(pipelineId.equals("AdditionTest/0.9")){
				bool = false;
			}			
		}
		assertThat("AdditionTest/0.9 is not present", pipelineId, is("AdditionTest/0.9"));
		
		//check parameters for a specified pipeline
		Pipeline pipelineResult = client.getPipeline(vth.getAdditionTestPipelineId());
		List<PipelineParameter> pipelineParam = pipelineResult.getParameters();
		assertThat("It must have 3 parameters", pipelineParam.size(), is(3));

		int cmptInt = 0;
		for(PipelineParameter pp : pipelineResult.getParameters()){
			if(!(pp.getName().equals("results-directory"))){
				if(pp.getType().equals(ParameterType.STRING)){
					cmptInt++;
				}
			}
			logger.debug("type of parameters: {}",pp.getType());
		}
		assertThat("It must have 2 parameters of type string for the addition", cmptInt, is(2)) ;
		
		//create and start an execution
		Execution result = client.initAndStartExecution(vth.initExecution("testScenario1", 40, 41));
		assertThat("The status must be \"running\"", result.getStatus(), is(StatusEnum.RUNNING));
		
		//keep the identifier
		String exeId = result.getIdentifier();
		
		// TODO : use  executorService to wait
		while(result.getStatus().equals(StatusEnum.RUNNING)){
			Thread.sleep(Long.valueOf(vth.getAdditionTestTimeCheck()));
			//check the execution
			result = client.getExecution(exeId);
			
		}
		
		assertThat("The status must be \"finished\" but it is", result.getStatus(), is(StatusEnum.FINISHED));
		
		return result.getStatus().equals(StatusEnum.FINISHED);
		
	}
		
	public static void main(String[] args) throws Exception{

		String apikey = System.getProperty("apikey");
		Scenario1 scenarioTest1 = new Scenario1();
		logger.debug("Launch scenario 1");
		logger.info("waited result: true");		
		logger.info("Scenario 1 result: {}",scenarioTest1.scenario1(apikey));

	}
}

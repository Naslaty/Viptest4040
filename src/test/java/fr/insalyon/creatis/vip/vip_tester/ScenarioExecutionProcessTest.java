package fr.insalyon.creatis.vip.vip_tester;

import io.swagger.client.ApiException;
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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class ScenarioExecutionProcessTest {
	
	private VipTesterHelper vth = new VipTesterHelper();
	private DefaultApi client = vth.getDefaultApi();
	private static Logger logger = LoggerFactory.getLogger(ScenarioExecutionProcess.class);
	
	public class CallableTimeout implements Callable<Boolean>{
		private ScheduledFuture<Boolean> expected = null;
		
		public Boolean call() throws InterruptedException, ExecutionException{
			logger.debug("timeout call-->");
			logger.debug("expected: {}", expected);
			boolean b = expected.cancel(true);
			logger.debug("cancel success {}",b);
			return true;
		}
		
		public void setExpected(ScheduledFuture<Boolean> expected) {
			this.expected = expected;
		}
	}
	
	//tries to launch an execution an waits the end of it
	@Test
	public void scenario1Test() throws Exception{	
		
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
		final String exeId = result.getIdentifier();
		
		// TEST ExecutorService 1
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		CallableTimeout callTimeout = new CallableTimeout();
		ScheduledExecutorService executorT = Executors.newSingleThreadScheduledExecutor();
		
		Boolean isFinished = false;
		ScheduledFuture<Boolean> timeout = executorT.schedule(callTimeout, 10,TimeUnit.MINUTES);
		while(!isFinished){
			 ScheduledFuture<Boolean>expected = executor.schedule(new Callable<Boolean>(){
				 							public Boolean call() throws ApiException{
				 								StatusEnum result = client.getExecution(exeId).getStatus();
				 								logger.debug("new check	result: {}", result);
				 								return result.equals(StatusEnum.FINISHED);
				 							}}, 20, TimeUnit.SECONDS);
			 callTimeout.setExpected(expected);
			 isFinished = expected.get();
		}
		timeout.cancel(true);
		executor.shutdownNow();
	
		assertThat("The status must be \"finished\" but it is", isFinished, is(true));
		
		//return isFinished.equals(true);		
	}
}

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
	
	public boolean searchPipelineId() throws Exception{
		String pipelineId = null;
		Iterator<Pipeline> listPipelineResultIt = client.listPipelines(null).iterator();

		boolean isFound = false;
		while(!isFound && listPipelineResultIt.hasNext()){
			Pipeline p = listPipelineResultIt.next();
			pipelineId = p.getIdentifier();
			if(pipelineId.equals("AdditionTest/0.9")){
				isFound = true;
			}			
		}
		return isFound;
	}
	
	public void checkPipelineParameter(Pipeline pipeline){
		List<PipelineParameter> pipelineParam = pipeline.getParameters();
		assertThat("It must have 3 parameters", pipelineParam.size(), is(3));
		
		int cmptInt = 0;
		for(PipelineParameter pp : pipeline.getParameters()){
			if(!(pp.getName().equals("results-directory"))){
				if(pp.getType().equals(ParameterType.STRING)){
					cmptInt++;
				}
			}
		}
		assertThat("It must have 2 parameters of type string for the addition", cmptInt, is(2)) ;		
		return;
	}
	
	public boolean checkExecutionProcess(boolean isFinished, final String executionId) throws Exception{
		// TEST ExecutorService 1
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

		CallableTimeout callTimeout = new CallableTimeout();		
		Callable<Boolean> callable = new Callable<Boolean>(){
			public Boolean call() throws ApiException{
				StatusEnum result = client.getExecution(executionId).getStatus();
				return result.equals(StatusEnum.FINISHED);
			}
		};
		
		ScheduledFuture<Boolean> timeout = executor.schedule(callTimeout, 10,TimeUnit.MINUTES);
		while(!isFinished){
			 ScheduledFuture<Boolean>expected = executor.schedule(callable, 20, TimeUnit.SECONDS);
			 callTimeout.setExpected(expected);
			 isFinished = expected.get();
		}
		timeout.cancel(true);
		executor.shutdownNow();
		return isFinished;
	}
	
	public class CallableTimeout implements Callable<Boolean>{
		private ScheduledFuture<Boolean> expected = null;
		
		public Boolean call() throws InterruptedException, ExecutionException{
			return expected.cancel(true);
		}
		
		public void setExpected(ScheduledFuture<Boolean> expected) {
			this.expected = expected;
		}
	}
	
	//tries to launch an execution an waits the end of it
	@Test
	public void scenario1Test() throws Exception{					
		assertThat("AdditionTest/0.9 is not present", searchPipelineId(), is(true));
		
		//check parameters for a specified pipeline
		Pipeline pipelineResult = client.getPipeline(vth.getAdditionTestPipelineId());
		checkPipelineParameter(pipelineResult);
		
		//create and start an execution
		Execution result = client.initAndStartExecution(vth.initExecution("testScenario1", 40, 41));
		assertThat("The status must be \"running\"", result.getStatus(), is(StatusEnum.RUNNING));		
		//keep the identifier
		final String executionId = result.getIdentifier();
				
		boolean isFinished = false;
		assertThat("The status must be \"finished\" but it is", checkExecutionProcess(isFinished, executionId), is(true));	
	}
}

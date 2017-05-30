package fr.insalyon.creatis.vip.vip_tester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import io.swagger.client.ApiClient;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Execution;

public class VipTesterHelper {
	
	/* TODO : create constructor where 
	- the properties are extracted
	- the api key is get
	- the api client is created 
	 */
	//public or private with a getter ???
	public Properties prop = new Properties();
	private String apikey = null;
	//public or private with a getter ???
	public DefaultApi defaultApiClient;
	
	public VipTesterHelper(){
		prop = propertiesExtraction(prop);
		this.apikey = System.getProperty("apikey");
		defaultApiClient = initClient(prop.getProperty("viptest.additiontest.url"), apikey);
	}
	
	public String getAdditionTestPipelineId(){
		return prop.getProperty("viptest.additiontest.pipelineidentifier");
	}
	
	public String getAdditionTestTimeCheck(){
		return prop.getProperty("viptest.additiontest.timecheck");
	}
	
	private Properties propertiesExtraction(Properties prop){
		try{
			FileInputStream in = new FileInputStream("src/main/resources/testVipAdditiontest.properties");
			try{
				prop.load(in);
				in.close();
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
		}catch(FileNotFoundException fnfe){
			System.out.println(fnfe.getMessage());
		}
		return prop;
	}
	
	private static DefaultApi initClient(String url, String apiKey){
		ApiClient testAPiclient = new ApiClient();
		testAPiclient.setBasePath(url);
		testAPiclient.setApiKey(apiKey);
		return new DefaultApi(testAPiclient);
	}
	
	public Execution initExecution(String name, int n1, int n2){
		Execution testExe = new Execution();
		testExe.setName(name);
		testExe.setPipelineIdentifier("AdditionTest/0.9");
		Map<String,Object> testMap = new HashMap<String, Object>();
		testMap.put("number1", n1);
		testMap.put("number2", n2);
		testMap.put("results-directory", "/vip/Home");
		testExe.setInputValues(testMap);
		return testExe;
	}
	
	public Execution modifExecution(String newName, long newTimeout, String pipelineIdentifier){
		Execution body = new Execution();
		body.setName(newName);
		body.setTimeout(newTimeout);
		body.setPipelineIdentifier(pipelineIdentifier);
		return body;
	}
	
	public String randomSelection(){
    	int i, j = 40 ;
    	char[] table = new char[83];
    	char aleaChar;
    	String aleaName = "";
    	
    	// fill the character table
    	for(i = 0; i<83;i++){
    		table[i] = (char)j;
    		j++;
    	}
    	//System.out.println(table);

    	for(i=0; i<15;i++){
	    	Random randomer = new Random();
	    	int indice = randomer.nextInt(table.length);
	    	aleaChar = table[indice];
	    	aleaName += aleaChar;
    	}
    	return aleaName;
	}
}

package auto_test.vip;

public class Scenario3 {
	
	public static void main(String args[])throws Exception{
		
		String apikey = System.getProperty("apikey");
		Scenario scenarioTest3 = new Scenario();
		System.out.println(scenarioTest3.scenario3(apikey));
	}
}

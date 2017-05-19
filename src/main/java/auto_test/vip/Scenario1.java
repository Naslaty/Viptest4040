package auto_test.vip;

public class Scenario1 {
		
	public static void main(String[] args) throws Exception{

		String apikey = System.getProperty("apikey");
		Scenario scenarioTest1 = new Scenario();
		System.out.println(scenarioTest1.scenario1(apikey));		
	}
}

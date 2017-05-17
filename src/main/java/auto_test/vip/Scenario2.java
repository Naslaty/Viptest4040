package auto_test.vip;

public class Scenario2 {
		
	public static void main(String[] args) throws Exception{
		
		String apikey = System.getProperty("apikey");
		Scenario scenarioTest2 = new Scenario();
		System.out.println(scenarioTest2.scenario2(apikey));
	}
}

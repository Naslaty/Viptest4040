package auto_test.vip;

public class Scenario4 {
	
	public static void main(String[] args) throws Exception{
		
		String apikey = System.getProperty("apikey");
		Scenario scenarioTest4 = new Scenario();
		System.out.println(scenarioTest4.scenario4(apikey));
	}
}

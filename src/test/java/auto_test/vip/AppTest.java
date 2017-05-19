package auto_test.vip;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase{
	
	private String apikey = System.getProperty("apikey");

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ){
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite( AppTest.class );
    }

//    /**
//     * Rigourous Test scenario 1:-)
//     * @throws Exception 
//     */
//    public void testAppScenario1() throws Exception{	
//		Scenario scenarioTest1 = new Scenario();
//        assertTrue( scenarioTest1.scenario1(apikey) );
//    }
//    
    /**
     * Rigourous Test scenario 2:-)
     * @throws Exception 
     */
    public void testAppScenario2() throws Exception{	
		Scenario scenarioTest2 = new Scenario();
        assertTrue( scenarioTest2.scenario2(apikey) );
    }
//    
//    /**
//     * Rigourous Test scenario 3:-)
//     * @throws Exception 
//     */
//    public void testAppScenario3() throws Exception{	
//		Scenario scenarioTest3 = new Scenario();
//        assertTrue( scenarioTest3.scenario3(apikey) );
//    }
//    
//    /**
//     * Rigourous Test scenario 4:-)
//     * @throws Exception 
//     */
//    public void testAppScenario4() throws Exception{	
//		Scenario scenarioTest4 = new Scenario();
//        assertTrue( scenarioTest4.scenario4(apikey) );
//    }
//    
//    /**
//     * Rigourous Test scenario 5:-)
//     * @throws Exception 
//     */
//    public void testAppScenario5() throws Exception{
//    	
// 
//    	Scenario scenarioTest5 = new Scenario();
//        assertFalse(scenarioTest5.scenario5(apikey));
//    }
//    
//    /**
//     * Rigourous Test scenario 6:-)
//     * @throws Exception 
//     */
//    public void testAppScenario6() throws Exception{
//    	Scenario scenarioTest6 = new Scenario();
//        assertFalse(scenarioTest6.scenario6(apikey));
//    }
}

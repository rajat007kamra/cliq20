package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.json.simple.parser.ParseException;

import com.squareup.javapoet.MethodSpec.Builder;

import actions.json.mapper.FindActions;
import actions.mappers.Tm4jSynchronizer;
import core.testdata.manager.TestCase;
import core.testdata.manager.TestDataManager;
import core.testdata.manager.TestSuite;
import core.utilities.TestGenerator;
import selenium.context.Base;
import selenium.listeners.WebDriverListener;
import testNG.utils.DynamicTestNG;
import testNG.utils.MasterTestNg;
import testNG.utils.SuiteIdentifiers;

/**
 * @summary This is entry point of framework and is responsible for syncing TCs
 *          from TESTCASES screen to Zephyr and later generate java/testNG
 *          runner from JSONs
 * @author Manoj.Jain
 */
public class TestExecutionOrchestrator {
	private String testSuite;
	TestDataManager testDataManager;
	private Class<?> baseClazz;
	private LinkedList<String> testCaseList;
	private LinkedList<String> testNgList;
	public Map<String, Class<?>> actionMap;
	public static List<SuiteIdentifiers> list_suite = null;
	final static Logger logger = Logger.getLogger(TestExecutionOrchestrator.class);

	/**
	 * @summary Constructor for class and creating objects
	 * @throws Exception
	 */
	public TestExecutionOrchestrator() throws Exception {
		testDataManager = new TestDataManager();
		testCaseList = new LinkedList<String>();
		testNgList = new LinkedList<String>();
	}

	/**
	 * @summary Get list of test suites from testsuite.json
	 * @param suiteJson
	 * @return
	 * @throws ParseException
	 */
	public List<TestSuite> getTestSuites(String suiteJson) throws ParseException {
		return testDataManager.loadTestDataFromJsonMultipleSuitesJson(suiteJson);
	}

	/**
	 * @summary Fetches test cases from given test suite
	 * @param testSuite
	 * @return
	 */
	public ArrayList<TestCase> getTestCases(TestSuite testSuite) {
		return testSuite.get_testCases();
	}

	/**
	 * @summary This will return name of base class
	 * @return
	 */
	public Class<?> getBaseClazz() {
		return baseClazz;
	}

	/**
	 * @summary Sets name of base class
	 * @param baseClazz
	 */
	public void setBaseClazz(Class<?> baseClazz) {
		this.baseClazz = baseClazz;
	}

	/**
	 * @summary Populated java test cases from test suite
	 * @param testSuite
	 * @param actionMap
	 * @return
	 */
	public LinkedList<String> populateTestValues(TestSuite testSuite, Map<String, Class<?>> actionMap) {
		LinkedList<String> testList = new LinkedList<String>();
		try {
			ArrayList<TestCase> testCases = getTestCases(testSuite);
			logger.info("This test converting json to java class");
			for (TestCase testCase : testCases) {
				logger.info(testCase.getKey() +" steps :");
				TestGenerator testsGenerator = new TestGenerator(testCase);
				Builder builder = testsGenerator.generateMethod();
				testsGenerator.generateAnnotations(builder);
				testsGenerator.generateStatements(builder, actionMap, testCase.getBattery());
				String testCaseName = testsGenerator.generateTestClass(builder.build(), Base.class,
						WebDriverListener.class);
				testList.add(testCaseName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testList;
	}

	/**
	 * @summary Responsible for generating testNG.xml
	 * @param testSuite
	 * @param testList
	 * @param thread_count
	 * @return
	 */
	public String populateTestNg(TestSuite testSuite, LinkedList<String> testList, String thread_count) {
		try {
			SuiteIdentifiers suiteIdentifiers = new SuiteIdentifiers();
			suiteIdentifiers.setSuiteName(testSuite.get_name());
			suiteIdentifiers.setCategory(testSuite.getCategory());
			suiteIdentifiers.setBattery(testSuite.getBattery());
			suiteIdentifiers.setTestCases(testList);
			// For Performance Suite
			if (thread_count !=null && !thread_count.isBlank() ) {
				suiteIdentifiers.setThreadCount(Integer.parseInt(thread_count));
			}
			if (thread_count == null)
			{
				thread_count ="1" ;
			}
			DynamicTestNG dynamicTestNG = new DynamicTestNG();
			String testngFile = dynamicTestNG.createTestNGFile(suiteIdentifiers, thread_count);
			testNgList.add(testngFile);
			return testngFile;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @summary Generates MasterTestNG.xml
	 */
	public void createMasterTestng() {
		MasterTestNg masterTestNg = new MasterTestNg();
		masterTestNg.createMasterFile(testNgList);
	}

	/**
	 * @summary Returns map of actions
	 * @return
	 */
	public Map<String, Class<?>> getActionMap() {
		return actionMap;
	}

	/**
	 * @summary Sets map of actions
	 * @param actionMap
	 */
	public void setActionMap(Map<String, Class<?>> actionMap) {
		this.actionMap = actionMap;
	}

	/**
	 * @summary Method responsible for SYNC & EXECUTE based on parameters provided
	 * @param jobType
	 * @param category
	 * @param subcategory
	 * @param thread_count
	 * @throws Exception
	 */
	public void executeJob(String jobType, String category, String subcategory, String thread_count) throws Exception {
		Map<TestSuite, LinkedList<String>> suiteMap = new HashMap<TestSuite, LinkedList<String>>();
		Tm4jSynchronizer tm4jOrchestrator = new Tm4jSynchronizer();
		if (jobType != null) {
			if (jobType.equalsIgnoreCase("SYNC")) {
				// Update TM4J
				logger.info("SYNCJOB: sync starts");
				tm4jOrchestrator.syncTestCases(category, subcategory);
			} else if (jobType.equalsIgnoreCase("EXECUTE")) {
				// Get All test cases from TM4J
				testSuite = tm4jOrchestrator.getTestSuite(category, subcategory);

				// Generate Testcases
				setActionMap(new FindActions().findAction());
				List<TestSuite> testSuites = getTestSuites(testSuite);
				for (TestSuite testSuite : testSuites) {
					LinkedList<String> testLists = populateTestValues(testSuite, getActionMap());
					suiteMap.put(testSuite, testLists);
				}

//				 Compile the code once you test files are generated
				
				  InvocationRequest request = new DefaultInvocationRequest();
				  request.setGoals(Arrays.asList("install", "-DskipTests=true",
				  "-Dmaven.exec.skip=true")); Invoker invoker = new DefaultInvoker();
				  InvocationResult execute = invoker.execute(request);
				  logger.info(execute.getExitCode());
				 

				Set<TestSuite> keySet = suiteMap.keySet();
				for (TestSuite testSuite : keySet) {
					// Generate testng file
					populateTestNg(testSuite, suiteMap.get(testSuite), thread_count);
				}
				createMasterTestng();
			} else {
				logger.info("Please provide appropriate command [EXECUTE or SYNC]");

			}
		}

	}

	/**
	 * @summary Main method
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		int executed = 1;
		try {
			TestExecutionOrchestrator testOrchestrator = new TestExecutionOrchestrator();

//			testOrchestrator.executeJob("SYNC", "SPECIFIC", "ENTITY MASTER", "1");
//			testOrchestrator.executeJob("EXECUTE", "SPECIFIC", "ENTITY MASTER", "1");

//			testOrchestrator.executeJob("EXECUTE", "PERFORMANCE", "LOAD", "1");
//			testOrchestrator.executeJob("EXECUTE", "PERFORMANCE", "LOAD", "3");

			  String thread_count = System.getProperty("threadCount"); // Number of VU for Performance
			  logger.info("Job type - " +args[0] +", Category - " +args[1] +", Subcategory - " +args[2] +", Thread count - " +thread_count);
			  testOrchestrator.executeJob(args[0], args[1], args[2], thread_count);
			 

			executed = 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(executed);
		}
	}
}

package actions.mappers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tm4j.pojo.AllTestsData;
import com.tm4j.pojo.CustomFields;
import com.tm4j.pojo.Status;
import com.tm4j.pojo.Tm4JTestCaseData;
import com.tm4j.pojo.TestsExecutionData;
import com.tm4j.utils.TM4JApiHelper;
import com.tm4j.utils.Tm4jConstatnts;

import core.cliqdb.model.TestSubcategoryJsonData;
import core.cliqdb.model.TestCategoryJsonData;
import core.cliqdb.model.TestcaseJsonData;
import core.db.CommonConstants;
import core.db.DataExtractor;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class Tm4jSynchronizer {

	final static Logger logger = Logger.getLogger(Tm4jSynchronizer.class);
	private TestCategoryJsonData getAllTestCasesCliqDb(String catagory, String subcategory) throws Exception {
		String finalJson = new JsonCreator().generateFinalJson(catagory, subcategory);
		return new Gson().fromJson(finalJson, TestCategoryJsonData.class);
	}

	// Method to fecth zephyr cases on fields bases (category, subcategory)
	private Map<String, Tm4JTestCaseData> getMatchingTm4JtestCases(String category, String subcategory) throws IOException {
		//AllTestsData filteredCases = new AllTestsData();
		List<Tm4JTestCaseData> testList = new ArrayList<Tm4JTestCaseData>();
		Map<String, Tm4JTestCaseData> testcaseMap = new HashMap<String, Tm4JTestCaseData>();

		AllTestsData allTestCasesTm4j = TM4JApiHelper.getAllTestCases(Tm4jConstatnts.projectyKey);
		List<Tm4JTestCaseData> testCases = allTestCasesTm4j.getTestCases();
		for (Tm4JTestCaseData tm4jTestCaseData : testCases) {
			if (tm4jTestCaseData.getCustomFields().getCATEGORY() != null && tm4jTestCaseData.getCustomFields().getSubCategory()!=null && tm4jTestCaseData.getStatus().getId() != null && !tm4jTestCaseData.getLabels().contains(CommonConstants.LABEL_INVALIDJSON)) {
				if (tm4jTestCaseData.getCustomFields().getCATEGORY().equalsIgnoreCase(category) && tm4jTestCaseData.getCustomFields().getSubCategory().equalsIgnoreCase(subcategory.toString())) {
					if (tm4jTestCaseData.getCustomFields().getAutomated() != null && tm4jTestCaseData.getCustomFields().getAutomated().equalsIgnoreCase(CommonConstants.YES)) {
						if (tm4jTestCaseData.getCustomFields().getJSON() != null
								&& !tm4jTestCaseData.getCustomFields().getJSON().isEmpty()) {
							if(DataExtractor.isEmpty(tm4jTestCaseData.getCustomFields().getENTERPRISEID()))
							{
								testcaseMap.put(tm4jTestCaseData.getId(), tm4jTestCaseData);
							}
							else {
								testcaseMap.put(tm4jTestCaseData.getCustomFields().getENTERPRISEID(), tm4jTestCaseData);
							}
						}
					}
				}
			}
		}
		return testcaseMap;
	}

	public String getTestSuite(String categoryName, String subCategory) throws IOException {
		TestCategoryJsonData testCategoryJsonData = new TestCategoryJsonData();
		List<TestSubcategoryJsonData> batteryList = new ArrayList<TestSubcategoryJsonData>();
		TestSubcategoryJsonData testBatteryData = new TestSubcategoryJsonData();
		testBatteryData.setCategory(categoryName);
		testBatteryData.setBattery(subCategory);
		testBatteryData.setSubCategory(subCategory);
		testBatteryData.setSuiteName(subCategory);

		List<TestcaseJsonData> testList = new ArrayList<TestcaseJsonData>();
		Map<String, Tm4JTestCaseData> allTestCasesTm4j = getMatchingTm4JtestCases(categoryName, subCategory);
		
		for (Tm4JTestCaseData testCaseData : allTestCasesTm4j.values()) 
		{
//			logger.info(testCaseData.getKey()); // Printing TEST Case KEY
			String testJson = testCaseData.getCustomFields().getJSON();
			testJson = StringEscapeUtils.unescapeHtml(testJson);
//			logger.info(testJson);
			TestcaseJsonData testCase = new Gson().fromJson(testJson, TestcaseJsonData.class);
//			logger.info(testCase);
			testCase.setTestKey(testCaseData.getKey());
			testList.add(testCase);
		}
		testBatteryData.setTestCases(testList);
		batteryList.add(testBatteryData);
		testCategoryJsonData.setSubcategoryTestCases(batteryList);
		logger.info("TOTAL TEST CASES - " + testList.size());
		return new Gson().toJson(testCategoryJsonData);
	}

	public TestsExecutionData syncTestCases(String categoryName, String subcategory) {
		TestsExecutionData testsExecutionData = new TestsExecutionData();
		try {
			List<String> testKeys = new ArrayList<String>();

			// Read all Testcases from TestCase screen
			TestCategoryJsonData allTestCasesCliqDb = getAllTestCasesCliqDb(categoryName, subcategory);

			// Read all Testcases from Tm4J
			Map<String, Tm4JTestCaseData> allTestCasesTm4j = getMatchingTm4JtestCases(categoryName, subcategory);
			

			// Sync-up CliQ TestCases with Tm4J
			List<TestSubcategoryJsonData> allSubcategoryTestCases = allTestCasesCliqDb.getSubcategoryTestCases();
			for (TestSubcategoryJsonData subcategoryTestCases : allSubcategoryTestCases) {
				List<TestcaseJsonData> testCases = subcategoryTestCases.getTestCases();
				for (TestcaseJsonData testcase : testCases) 
				{
					Tm4JTestCaseData testCasetm4j = allTestCasesTm4j.get(testcase.getEnterpriseId());
					if(testcase.getActions().size()>0)
					{
						if (testCasetm4j != null)
						{
							String testKey = updateTestCase(testCasetm4j, testcase);
							testKeys.add(testKey + CommonConstants.UNDERSCORE + testcase.getEnterpriseId());
						}
						else
						{
							testCasetm4j = new Tm4JTestCaseData();
							String testKey = createTestCase(testCasetm4j, testcase);
							testKeys.add(testKey + CommonConstants.UNDERSCORE + testcase.getEnterpriseId());
						}
					}
				}
			}
			testsExecutionData.setProjectKey(Tm4jConstatnts.projectyKey);
			testsExecutionData.setTestCases(testKeys);
			logger.info("SYNCJOB: Test Keys - " +new Gson().toJson(testsExecutionData));

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("SYNCJOB: Synced TC's - " +testsExecutionData.getTestCases().size());
		return testsExecutionData;
	}

	public String updateTestCase(Tm4JTestCaseData tm4jTest, TestcaseJsonData testcase) throws IOException {
		CustomFields customFields = new CustomFields();
		customFields.setCATEGORY(testcase.getCategory());
		customFields.setBATTERY(testcase.getBattery());
		customFields.setSubCategory(testcase.getBattery());
		customFields.setType(testcase.getType());
		customFields.setENTERPRISEID(testcase.getEnterpriseId());
		customFields.setMODE(testcase.getMode());
		customFields.setJSON(new Gson().toJson(testcase));
		customFields.setCaption(testcase.getCaption());
//		customFields.setActivationDate(getFormatedDate(testcase.getActivationDate()));
		tm4jTest.setCustomFields(customFields);
		tm4jTest.setProjectKey(Tm4jConstatnts.projectyKey);
		tm4jTest.setPriorityName("HIGH");
		List<String> labels = testcase.getLabels();
		labels.add("BOTZ-TEST");
		tm4jTest.setLabels(labels);
		
		// ID FOR ACTIVE
		String stateOrStatus = testcase.getState().toUpperCase();
		Status status = new Status();
		switch (stateOrStatus) {				
		case "ACTIVE":
			status.setId(1026286);
			status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1026286");
			break;
		case "REVISING":
			status.setId(1144735);
			status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1144735");
			break;
		case "INACTIVE":
			status.setId(1144736);
			status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1144736");
			break;
		case "ONBOARDING":
			status.setId(1139843);
			status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1139843");
			break;
		case "PENDING":
			status.setId(1026290);
			status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1026290");
			break;

		default:
			break;
		}
		tm4jTest.setStatus(status);

		String requestJson = new GsonBuilder().serializeNulls().create().toJson(tm4jTest);
		JsonElement jsonElem = new GsonBuilder().serializeNulls().create().fromJson(requestJson, JsonElement.class);

		JsonObject asJsonObject = jsonElem.getAsJsonObject();
		asJsonObject.remove("componentId");
		asJsonObject.remove("priorityName");
		asJsonObject.remove("statusName");
		asJsonObject.remove("folderId");
		asJsonObject.remove("ownerId");
		asJsonObject.remove("projectKey");
		Tm4JTestCaseData testCases = TM4JApiHelper
				.updateTestCases(new GsonBuilder().serializeNulls().create().toJson(asJsonObject), tm4jTest.getKey());
		logger.info("Test Updated");
		return tm4jTest.getKey();
	}

	public String createTestCase(Tm4JTestCaseData tm4jTest, TestcaseJsonData testcase) throws IOException {
		tm4jTest.setName(testcase.getTestDescription());
		tm4jTest.setPriorityName("HIGH");
		CustomFields customFields = new CustomFields();
		customFields.setCATEGORY(testcase.getCategory());
		customFields.setBATTERY(testcase.getBattery());
		customFields.setSubCategory(testcase.getBattery());
		customFields.setType(testcase.getType());
		customFields.setENTERPRISEID(testcase.getEnterpriseId());
		customFields.setMODE(testcase.getMode());
		customFields.setJSON(new Gson().toJson(testcase));
		customFields.setCaption(testcase.getCaption());
//		customFields.setActivationDate(getFormatedDate(testcase.getActivationDate()));

		tm4jTest.setCustomFields(customFields);
		tm4jTest.setProjectKey(Tm4jConstatnts.projectyKey);
		List<String> labels = testcase.getLabels();
		labels.add("BOTZ-TEST");
		tm4jTest.setLabels(labels);

		// ID FOR ACTIVE
		String stateOrStatus = testcase.getState().toUpperCase();
			Status status = new Status();
			switch (stateOrStatus) {				
			case "ACTIVE":
				status.setId(1026286);
				status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1026286");
				break;
			case "REVISING":
				status.setId(1144735);
				status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1144735");
				break;
			case "INACTIVE":
				status.setId(1144736);
				status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1144736");
				break;
			case "ONBOARDING":
				status.setId(1139843);
				status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1139843");
				break;
			case "PENDING":
				status.setId(1026290);
				status.setSelf("https://api.tm4j.smartbear.com/rest-api/v2/statuses/1026290");
				break;

			default:
				break;
			}
			tm4jTest.setStatus(status);

		logger.info("Test Created - " + testcase.getEnterpriseId());
		String requestJson = new GsonBuilder().serializeNulls().create().toJson(tm4jTest);
		JsonElement jsonElem = new GsonBuilder().serializeNulls().create().fromJson(requestJson, JsonElement.class);
		JsonObject asJsonObject = jsonElem.getAsJsonObject();
		asJsonObject.remove("key");
		asJsonObject.remove("id");
		asJsonObject.remove("folder");
		asJsonObject.remove("owner");
		asJsonObject.remove("project");
		asJsonObject.remove("status");
		asJsonObject.remove("priority");
		asJsonObject.remove("testScript");
		String testJson = new GsonBuilder().serializeNulls().create().toJson(asJsonObject);
		logger.info(testJson);
		JsonElement testCaseData = TM4JApiHelper.createTest(testJson);
		return testCaseData.getAsJsonObject().get("key").toString();
	}

	public String getFormatedDate(String dateString) {
		try {
			Date date = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(dateString);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}

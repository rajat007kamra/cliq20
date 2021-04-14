package actions.mappers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mezocliq.bytes.common.cacher.CacherException;
import com.mezocliq.bytes.qrector.exception.DatabaseException;

import core.cliqdb.model.AttributeData;
import core.cliqdb.model.SubcategoryOrderData;
import core.cliqdb.model.ProfileData;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.StepData;
import core.cliqdb.model.SummaryData;
import core.cliqdb.model.TestcaseActionJsonData;
import core.cliqdb.model.TestSubcategoryJsonData;
import core.cliqdb.model.TestCaseData;
import core.cliqdb.model.TestCategoryJsonData;
import core.cliqdb.model.TestcaseJsonData;
import core.db.CliqDBHelper;
import core.db.DataComparator;
import core.db.DataExtractor;
import core.db.TestcaseDataValidator;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class JsonCreator {
	final static Logger logger = Logger.getLogger(JsonCreator.class);
	private CliqDBHelper cliqDBHelper;
	private static final String UUID_REGEX = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";

	public JsonCreator() throws CacherException {
		cliqDBHelper = new CliqDBHelper(false);
	}

	// Need to have a different class for caching.
	private List<TestcaseJsonData> generateTestcaseJsonData(String subcategory) throws Exception {
		List<TestcaseJsonData> testJsonDataList = new ArrayList<TestcaseJsonData>();
		List<TestCaseData> testCases = cliqDBHelper.getTestCaseData();
		Map<String, Map<String, String>> screenData = cliqDBHelper.getScreenData();
		Map<String, String> mzoneData = cliqDBHelper.getMzoneData();
		Map<String, String> allSimpleName = cliqDBHelper.getAllSimpleName();
		for (TestCaseData testCase : testCases) 
		{			
			List<String> orderedStepList = new ArrayList<String>();
			//Validating JSON is Valid or Invalid
			TestcaseDataValidator.validateTestcaseData(testCase);
			TestcaseJsonData testJsonData = converttoTestcaseJsonData(testCase, orderedStepList, mzoneData, allSimpleName, screenData, subcategory);			
			testJsonDataList.add(testJsonData);
		}
		logger.info("SYNCJOB: total test cases " + testCases.size());
		return testJsonDataList;
	}

	private TestcaseJsonData converttoTestcaseJsonData(TestCaseData testCase, List<String> orderedStepList,
			Map<String, String> mzoneData, Map<String, String> allSimpleName,
			Map<String, Map<String, String>> screenData, String subcategory) throws Exception 
	{	//orderedStepList.clear();
		TestcaseJsonData testJsonData = new TestcaseJsonData();
		ProfileData profileData = testCase.getProfileData();
		testJsonData.setCategory(profileData.getCategory());
		testJsonData.setSubCategory(profileData.getSubcategory());
		testJsonData.setBattery(profileData.getSubcategory());
		
		testJsonData.setType(profileData.getConditions());
		testJsonData.setMode(profileData.getMode());

		SummaryData summaryData = testCase.getSummaryData();
		testJsonData.setTestName(summaryData.getDescription());
		testJsonData.setTestDescription(summaryData.getDescription());
		testJsonData.setNote(summaryData.getDescription());
		testJsonData.setTestId(testCase.getRowKey());

		AttributeData attributeData = testCase.getAttributeData();
		testJsonData.setMethod(attributeData.getMethod());
		testJsonData.setReport_type(attributeData.getReport_type());
		testJsonData.setMethod(attributeData.getMethod());
		testJsonData.setReort_caption(attributeData.getReort_caption());
		testJsonData.setDelivery_format(attributeData.getDelivery_format());
		testJsonData.setReport_type(attributeData.getReport_type());
		testJsonData.setFrequency(attributeData.getFrequency());
		testJsonData.setApiCaption(attributeData.getApiCaption());
		testJsonData.setApiConfiguration(attributeData.getConfiguration());
		testJsonData.setApiMethod(attributeData.getMethod());
		testJsonData.setApiUsage(attributeData.getUsage());
		testJsonData.setApiRhythm(attributeData.getRhythm());

		testJsonData.setState(testCase.getState());
		testJsonData.setCaption(testCase.getCaption());
//		testJsonData.setActivationDate(testCase.getActivationDate());
		testJsonData.setEnterpriseId(testCase.getEnterpriseId());
		testJsonData.setLabels(testCase.getLabels());
		
		// Sort the TestCases by the Sequence Number (needed to add LOGIN-LOGOUT as needed)
		DataComparator.sortSteps(testCase.getSteps());
		List<StepData> steps = testCase.getSteps();		
		boolean addLoginAction = true;
		for (int i = 0; i < steps.size(); i++) 
		{			
			StepData currentStep = steps.get(i);
			StepData nextStep = i+1 < steps.size() ? steps.get(i+1) : null;
			List<SectionData> sections = currentStep.getStepSections();
			if (subcategory.startsWith("API"))
			{
				generateJsonActionsforAPIStep(sections, subcategory, orderedStepList, screenData, mzoneData, testJsonData);
			} 
			else 
			{									
				StepContext sc = new StepContext(profileData, currentStep, addLoginAction);
				generateJsonActionsforStep(sections, subcategory, orderedStepList, screenData, mzoneData, allSimpleName, sc);	
				
				// Set AddLoginAction as needed
				if  (nextStepNeedsDifferentUser(currentStep, nextStep))					
				{					
					orderedStepList.addAll(new LogoutMapper().getMappedAction(null, null));		
					addLoginAction = true;		
				}
				else
				{
					addLoginAction = false;					
				}
			}
		}

		List<TestcaseActionJsonData> sectionList = new ArrayList<TestcaseActionJsonData>();
		for (String action : orderedStepList) {
			sectionList.add(new Gson().fromJson(action, TestcaseActionJsonData.class));
		}

		testJsonData.setActions(sectionList);
		return testJsonData;
	}

	private boolean nextStepNeedsDifferentUser(StepData currentStep, StepData nextStep) 
	{
		if (nextStep == null)
		{
			return true;
		}
		else
		{
			if (DataExtractor.isEmpty(nextStep.getUser()))
			{
				nextStep.setUser(currentStep.getUser());
				return false;
			}
			else 
			{
				if (!currentStep.getUser().equals(nextStep.getUser()))
				{
					return true;
				}
			}
		}
		return false;
	}

	private String getProcessName(List<SectionData> sections) throws DatabaseException {
		for (SectionData section : sections) 
		{
			if (section.getName().equals("PARAMETERS")) 
			{
				List<Map<String, String>> context = section.getSectionContext();
				for (Map<String, String> map : context) {
					if (map.get("SCREEN") != null) {
						Set<UUID> uuids = new HashSet<UUID>();
						uuids.add(UUID.fromString(map.get("SCREEN")));
						Map<String, String> caption = cliqDBHelper.getCaption(uuids);
						Set<String> keySet = caption.keySet();
						for (String key : keySet) {
							return caption.get(key);
						}
					}
				}
			}
		}
		return null;
	}

	private void generateJsonActionsforStep(List<SectionData> sections, String subcategory, List<String> orderedStepList,
			Map<String, Map<String, String>> screenData, Map<String, String> mZoneData,
			Map<String, String> allSimpleName, StepContext stepContext)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException, Exception 
	{
		//orderedStepList.clear();
		JsonElement jsonElement = SubcategoryOrderData.getOrderData(subcategory);
		String processName = getProcessName(sections);
		int size = jsonElement.getAsJsonObject().entrySet().size();
		Map<String, SectionData> sectionDatamap = prepareSectionDataMap(sections);		
		for (int i = 1; i < size; i++) 
		{
			JsonElement element = jsonElement.getAsJsonObject().get(String.valueOf(i));
			if (element != null) 
			{
				String actionName = element.getAsString().toUpperCase();	
				SectionData section = sectionDatamap.get(actionName);
				if (section != null) 
				{
					section.setProcessName(processName);
					section.setSimpleName(allSimpleName);
					// Only for screen for now . we can extends this if required
					if (section.getName().equals("PARAMETERS")) 
					{
						section.setScreenData(screenData);
						section.setMzoneData(mZoneData);
					}
					List<String> action = JsonActionMapper.generateJsonActionforSection(section, stepContext);
					if (action != null) 
					{						
						orderedStepList.addAll(action);
					}						
				}
			}
		}
	}

	private Map<String, SectionData> prepareSectionDataMap(List<SectionData> sections) 
	{
		Map<String, SectionData> sectionDatamap = new HashMap<>();
		for (SectionData section : sections)
		{
			sectionDatamap.put(section.getName().toUpperCase(), section);
		}
		return sectionDatamap;
	}

	private void generateJsonActionsforAPIStep(List<SectionData> sections, String batteryName, List<String> orderedStepList,
			Map<String, Map<String, String>> screenData, Map<String, String> mZoneData, TestcaseJsonData testJsonData)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		List<String> action = APIActionMapper.getAction(sections, screenData, mZoneData, testJsonData);

		if (action != null) {
			orderedStepList.addAll(action);
		}
	}

	/**
	 * 
	 * @param templateName
	 * @return
	 * @throws Exception`
	 */
	public String generateFinalJson(String category, String subcategory) throws Exception {
		String finalJson = generateJson(category, subcategory);

		finalJson = finalJson.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
		//		.replace("\"[", "[").replace("]\"", "]");

		Set<UUID> allUUID = getAllUUID(finalJson);
		if(allUUID!=null && !allUUID.isEmpty()) {
			Map<String, String> captionMap = cliqDBHelper.getCaption(allUUID);
			Set<String> keySet = captionMap.keySet();
			for (String key : keySet) {
				finalJson = finalJson.replaceAll(key, captionMap.get(key));

			}
			logger.info("SYNCJOB: " +finalJson);
		}
		return finalJson;
	}

	private Set<UUID> getAllUUID(String jsonString) {
		Set<UUID> uuidSet = new HashSet<UUID>();
		Pattern pairRegex = Pattern.compile(UUID_REGEX);
		Matcher matcher = pairRegex.matcher(jsonString);
		while (matcher.find()) {
			String a = matcher.group(0);
			UUID uuid = UUID.fromString(a);
			uuidSet.add(uuid);
		}
		return uuidSet;
	}

	/**
	 * 
	 * @param category
	 * @param subcategory
	 * @return
	 * @throws Exception
	 */
	private String generateJson(String category, String subcategory) throws Exception {
		List<TestSubcategoryJsonData> suites = new ArrayList<TestSubcategoryJsonData>();
		List<TestcaseJsonData> testJsonList = generateTestcaseJsonData(subcategory);
		List<TestcaseJsonData> validTestcases = new ArrayList<TestcaseJsonData>();

		TestSubcategoryJsonData suiteData = new TestSubcategoryJsonData();
		suiteData.setCategory(category);
		suiteData.setSubCategory(subcategory);
		suiteData.setBattery(subcategory);
		suiteData.setSuiteName(category + "_" + subcategory);
		for (TestcaseJsonData testJsonData : testJsonList) 
		{
			if (testJsonData.getCategory() != null && testJsonData.getBattery() != null) 
			{
				if (testJsonData.getCategory().equals(category) && testJsonData.getSubCategory().equals(subcategory))
				{
					validTestcases.add(testJsonData);
				}				
			}

		}

		suiteData.setTestCases(validTestcases);
		suites.add(suiteData);

		TestCategoryJsonData testSuiteJsonData = new TestCategoryJsonData();
		testSuiteJsonData.setSubcategoryTestCases(suites);

		return new Gson().toJson(testSuiteJsonData);
	}
}

package actions.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;
import actions.model.LoginModel;
import actions.model.NavigateScreenModel;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

public class LoginMapper implements Mapper {
	/**
	 * STEP NAME=CONFIRM REVISION, SCREEN=cd357470-75e2-11e6-a87f-8f1364831101,
	 * RECORD COUNT=1, RECORD TYPE=EXISTING,
	 * USER=f499ec10-a87c-11e9-87bb-318152313422
	 * @param stepContext 
	 */

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) 
	{
		List<String> actions = new ArrayList<String>();
		List<Map<String, String>> sectionContextData = sectionData.getSectionContext();
		for (Map<String, String> map : sectionContextData) 
		{
			if (stepContext.isAddLoginAction())
			{
				actions.add(new Gson().toJson(getLoginAction(stepContext)));
				stepContext.setAddLoginAction(false);
			}
			
			if (map.get("SCREEN") != null && !map.get("SCREEN").isEmpty()) {
				actions.add(
						new Gson().toJson(getFindAction(sectionData.getScreenData(), sectionData.getMzoneData(), map)));
			}
			if (map.get("RECORD TYPE") != null) {
				if (map.get("RECORD TYPE").equalsIgnoreCase("NEW")) {
					actions.add(new Gson().toJson(getAddNewAction(map)));
				}
			}

		}
		return actions;
	}

	private TestcaseActionJsonData getLoginAction(StepContext stepContext) {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		try {
			testActionData.setName("LOGIN");
			LoginModel loginModel = new LoginModel();
			loginModel.setUser(stepContext.getStepData().getUser());
			testActionData.setContext(new Gson().toJson(loginModel));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testActionData;
	}

	private TestcaseActionJsonData getFindAction(Map<String, Map<String, String>> screenData, Map<String, String> mzoneData, Map<String, String> context) {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("NAVIGATE-SCREEN");
		NavigateScreenModel navigateScreenModel = new NavigateScreenModel();

		navigateScreenModel.setRealm("PROCESSES");
		navigateScreenModel.setProcess(getScreenFullName(screenData, mzoneData, context.get("SCREEN")));
		testActionData.setContext(new Gson().toJson(navigateScreenModel));

		return testActionData;
	}

	private TestcaseActionJsonData getAddNewAction(Map<String, String> context) {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("ADD");
		return testActionData;
	}

	private String getScreenFullName(Map<String, Map<String, String>> screenData, Map<String, String> mzoneData,
			String screenId) {
		Set<String> keySet = screenData.keySet();
		for (String key : keySet) {
			Map<String, String> map = screenData.get(key);
			if (map.containsKey("PROCESS")) {
				if (map.get("PROCESS").equals(screenId) && map.get("I").equals("STANDARD")) {
					return mzoneData.get(key);
				}

			}
		}
		return null;
	}
}

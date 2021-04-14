package actions.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import actions.model.CheckMessageModel;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

public class CheckMessageMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		sectionData.getName();
		List<String> actionList = new ArrayList<String>();
		List<Map<String, String>> context = sectionData.getSectionContext();
		for (Map<String, String> map : context) {
			TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
			
			testActionData.setName("CHECK-MESSAGE");
			testActionData.setContext(new Gson().toJson(getMappedAction(map)));
			actionList.add(new Gson().toJson(testActionData));
		}

		return actionList;
	}

	private CheckMessageModel getMappedAction(Map<String, String> context) {

		CheckMessageModel checkmodel = new CheckMessageModel();
		// checkmodel.setActionName("LOCATION");
		checkmodel.setVerifyMessage("MESSAGE");
		return checkmodel;
	}
}

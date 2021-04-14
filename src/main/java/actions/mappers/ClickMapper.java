package actions.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

public class ClickMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		List<String> actionList = new ArrayList<String>();
		List<Map<String, String>> sectionContext = sectionData.getSectionContext();
		for (Map<String, String> contextMap : sectionContext) 
		{
			TestcaseActionJsonData mappedAction = getMappedAction(contextMap);
			if (mappedAction != null) 
			{
				actionList.add(new Gson().toJson(mappedAction));
			}
		}
		return actionList;
	}

	/**
	 * SUBMIT SAVE REVISE REFRESH DELETE DISCARD ADD APPROVE
	 * 
	 * @param context
	 * @return
	 */
	public static TestcaseActionJsonData getMappedAction(Map<String, String> context) {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		if (context.containsKey("BUTTON")) {
			String sectName = context.get("BUTTON");
			switch (sectName) {
			case "SAVE":
				testActionData.setName("SAVE");
				break;
			case "SUBMIT":
				testActionData.setName("SUBMIT");
				break;
				//  Mode is REVISION
			case "REVISION":
				testActionData.setName("REVISE");
				break;
				// Click Button is Revise
			case "REVISE":
				testActionData.setName("REVISE");
				break;
			case "REFRESH":
				testActionData.setName("REFRESH");
				break;
			case "DELETE":
				testActionData.setName("DELETE");
				break;
			case "DISCARD":
				testActionData.setName("DISCARD");
				break;
			case "WITHDRAWAL":
				testActionData.setName("WITHDRAW");
				break;
			
			case "WITHDRAW":
				testActionData.setName("WITHDRAW");
				break;
			case "ADD":
				testActionData.setName("CREATE");
				break;
			case "APPROVE":
				testActionData.setName("APPROVE");
				break;
			default:
				break;
			}
		} else if (context.containsKey("LOCATION")) {
			if (context.get("LOCATION").equalsIgnoreCase("INTEGRATED-VIEW")) {
				testActionData.setName("OPEN-INTEGRATEDVIEW");
			}
		}
		return testActionData;
	}

}

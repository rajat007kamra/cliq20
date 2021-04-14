package actions.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import actions.model.Column;
import actions.model.FormViewModel;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

public class InputMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		List<String> actionList = new ArrayList<String>();
		List<FormViewModel> contextList = new ArrayList<FormViewModel>();

		List<Map<String, String>> context = sectionData.getSectionContext();
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("POPULATE-FORM");
		if(!stepContext.isFormOpen())
		{
			actionList.add(new Gson().toJson(openFormView()));
		}
		FormViewModel verticalFlyModel = new FormViewModel();

		List<Column> lists = new ArrayList<Column>();
		for (Map<String, String> map : context) {
			Column column = new Column();
			//column.setVariable(sectionData.getSimpleName().get(map.get("VARIABLE")) + "###" + map.get("UI FIELD"));
			column.setVariable(map.get("UI FIELD"));
			column.setValue(map.get("VALUE"));
			lists.add(column);
		}

		verticalFlyModel.setColumn(lists);

		testActionData.setContext(new Gson().toJson(contextList));
		testActionData.setContext(verticalFlyModel);
		actionList.add(new Gson().toJson(testActionData));
		return actionList;
	}
	
	private TestcaseActionJsonData openFormView() {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("OPEN-FORM");

		return testActionData;
	}
}

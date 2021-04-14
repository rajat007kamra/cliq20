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

public class CheckVariableMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		sectionData.getName();
		List<String> actionList = new ArrayList<String>();
		List<Map<String, String>> context = sectionData.getSectionContext();
		for (Map<String, String> map : context) {
			TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
			testActionData.setName("FORM-VIEW");
			if(!stepContext.isFormOpen())
			{
				actionList.add(new Gson().toJson(openFormView()));
			}
			
			testActionData.setName("CHECK-VARIABLE");
			testActionData.setContext(new Gson().toJson(getMappedAction(map)));
			actionList.add(new Gson().toJson(testActionData));
		}

		return actionList;
	}

	private FormViewModel getMappedAction(Map<String, String> context) {
		Column Columnmodel = new Column();
		Columnmodel.setTitle(context.get("UI FIELD"));
		Columnmodel.setText(context.get("VALUE"));

		List<Column> columnList = new ArrayList<Column>();
		columnList.add(Columnmodel);
		FormViewModel variablemodel = new FormViewModel();
		variablemodel.setColumn(columnList);

		return variablemodel;
	}
	
	private TestcaseActionJsonData openFormView() {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("OPEN-FORM");

		return testActionData;
	}
}

package actions.mappers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class LogoutMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		List<String> actions = new ArrayList<String>();
		TestcaseActionJsonData actionData = new TestcaseActionJsonData();
		actionData.setName("LOGOUT");
		actions.add(new Gson().toJson(actionData));

		return actions;
	}
}

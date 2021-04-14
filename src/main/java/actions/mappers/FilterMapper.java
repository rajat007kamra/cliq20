package actions.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.mezocliq.bytes.qrector.conjugates.DatabaseRow;
import actions.model.Column;
import actions.model.FilterModel;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;
import core.db.CliqDBHelper;

public class FilterMapper implements Mapper {

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		// resetDbRow(sectionData);
		List<String> actionList = new ArrayList<String>();
		List<Map<String, String>> context = sectionData.getSectionContext();
		for (Map<String, String> map : context) {
			TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
			testActionData.setName("PERFORM-VZFILTER");
			testActionData.setContext(new Gson().toJson(getMapperAction(map, sectionData)));
			actionList.add(new Gson().toJson(testActionData));
			/*
			 * Removed implicit OPEN-FORM after filter and handled in INPUT-PROFILE, INPUT, CHECK-VARIABLEsssss 
			 */
		}
		if (JsonActionMapper.isReviseorWithdrawMode(stepContext) && JsonActionMapper.isAnchorType(stepContext)) {
			Map<String, String> actionMap = constructActionMap(stepContext.getProfileData().getMode());
			actionMap.put("BUTTON", stepContext.getProfileData().getMode());
			actionList.add(new Gson().toJson(ClickMapper.getMappedAction(actionMap)));
		}
		return actionList;
	}

	private Map<String, String> constructActionMap(String usecaseMode) {
		Map<String, String> actionMap = new HashMap<>();
		actionMap.put("BUTTON", usecaseMode);
		return actionMap;
	}

	private FilterModel getMapperAction(Map<String, String> map, SectionData sectionData) {
		FilterModel filterModel = new FilterModel();
		Column column = new Column();
		// column.setTitle(sectionData.getSimpleName().get(map.get("VARIABLE")) + "###"
		// + map.get("UI FIELD"));
		column.setTitle(map.get("UI FIELD"));
		column.setText(map.get("VALUE"));

		List<Column> lists = new ArrayList<Column>();
		lists.add(column);
		filterModel.setColumn(lists);
		filterModel.setSelectRow(map.get("RECORD NUMBER"));
		return filterModel;
	}

	private void resetDbRow(SectionData section) {
		try {
			CliqDBHelper cliqDBHelper = new CliqDBHelper(false);
			List<DatabaseRow> dbRows = cliqDBHelper.getDBRows(section.getProcessName());
			for (DatabaseRow databaseRow : dbRows) {
				List<Map<String, String>> context = section.getSectionContext();
				for (Map<String, String> map : context) {
					if (map.get("RECORD CAPTION") != null && !map.get("RECORD CAPTION").isEmpty()) {
						if (databaseRow.getRowkey().toString().equals(map.get("RECORD CAPTION"))) {
							cliqDBHelper.markActive(databaseRow);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private TestcaseActionJsonData getReviseAction() {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("REVISE-RECORD");

		return testActionData;
	}
}

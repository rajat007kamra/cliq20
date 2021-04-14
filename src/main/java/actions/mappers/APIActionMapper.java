package actions.mappers;

import java.util.List;
import java.util.Map;

import core.cliqdb.model.SectionData;
import core.cliqdb.model.TestcaseJsonData;

public class APIActionMapper {

	public static List<String> getAction(List<SectionData> sections, Map<String, Map<String, String>> screenData,
			Map<String, String> mZoneData, TestcaseJsonData testJsonData) {
		String apiMethod = testJsonData.getApiMethod();
		switch (apiMethod) {
		case "GET":
			return new GetAPIMapper().getMappedAction(sections, screenData, mZoneData, testJsonData);

		case "POST":
			return new PostAPIMapper().getMappedAction(sections, screenData, mZoneData, testJsonData);

		case "DELETE":
			return new DeleteAPIMapper().getMappedAction(sections, screenData, mZoneData, testJsonData);

		case "PUT":
			return new PUTAPIMapper().getMappedAction(sections, screenData, mZoneData, testJsonData);

		default:
			break;
		}
		return null;
	}
}

package actions.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import core.api.model.ApiModel;
import core.api.model.Context;
import core.api.model.Header;
import core.api.model.Response;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseJsonData;

public class GetAPIMapper implements Mapper {

	private ApiModel apiModel;

	public GetAPIMapper() {
		this.apiModel = new ApiModel();
	}

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * This method is responsible for fetching data from TESTCASE SCREEN
	 *
	 */
	public List<String> getMappedAction(List<SectionData> sections, Map<String, Map<String, String>> screenData,
			Map<String, String> mZoneData, TestcaseJsonData testJsonData) {

		List<String> actionString = new ArrayList<String>();

		List<Header> headerList = new ArrayList<Header>();
		SectionData inputAPISection = getInputAPISection(sections);
		SectionData checkAPISection = getCheckAPISection(sections);
		SectionData uploadSection = getUploadSection(sections);
		SectionData queryParam = geQueryParam(sections);

		Context context = new Context();
		context.setApi(inputAPISection.getSectionContext().get(0).get("CALL STRING"));
		context.setApiVersion(inputAPISection.getSectionContext().get(0).get("VERSION"));
		if (queryParam != null) {
			context.setQueryParam(queryParam.getSectionContext());
		}

		Header contentType = new Header();
		contentType.setName("Content-Type");
		contentType.setValue("application/json");

		Header authHeader = new Header();
		authHeader.setName("Authorization");
		authHeader.setValue("Bearer " + inputAPISection.getSectionContext().get(0).get("JWT TOKEN"));
		headerList.add(contentType);
		headerList.add(authHeader);
		context.setHeader(headerList);
		context.setBody(inputAPISection.getSectionContext().get(0).get("JSON"));

		Response response = new Response();
		response.setStatuscode(checkAPISection.getSectionContext().get(0).get("HTTP CODE"));
		response.setStatusmessage(checkAPISection.getSectionContext().get(0).get("HTTP MESSAGE"));
		context.setResponse(response);

		this.apiModel.setContext(context);
		this.apiModel.setName(testJsonData.getApiMethod());

		String actionJson = new Gson().toJson(this.apiModel);
		actionString.add(actionJson);

		return actionString;
	}

	private SectionData getInputAPISection(List<SectionData> sections) {
		for (SectionData sectionData : sections) {
			if (sectionData.getName().equalsIgnoreCase("INPUT-API")) {
				return sectionData;
			}
		}
		return null;
	}

	private SectionData getCheckAPISection(List<SectionData> sections) {
		for (SectionData sectionData : sections) {
			if (sectionData.getName().equalsIgnoreCase("CHECK-API")) {
				return sectionData;
			}
		}
		return null;
	}

	private SectionData getUploadSection(List<SectionData> sections) {
		for (SectionData sectionData : sections) {
			if (sectionData.getName().equalsIgnoreCase("UPLOAD")) {
				return sectionData;
			}
		}
		return null;
	}

	private SectionData geQueryParam(List<SectionData> sections) {
		for (SectionData sectionData : sections) {
			if (sectionData.getName().equalsIgnoreCase("QUERY PARAM")) {
				return sectionData;
			}
		}
		return null;
	}
}
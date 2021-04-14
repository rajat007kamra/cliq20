
package core.api.model;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

	@SerializedName("api")
	@Expose
	private String api;
	@SerializedName("apiVersion")
	@Expose
	private String apiVersion;
	@SerializedName("header")
	@Expose
	private List<Header> header;
	@SerializedName("body")
	@Expose
	private Object body;
	@SerializedName("response")
	@Expose
	private Response response;
	@SerializedName("attachment")
	@Expose
	private String attachment;

	@SerializedName("queryParam")
	@Expose
	private List<Map<String, String>> queryParam;

	public List<Map<String, String>> getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(List<Map<String, String>> queryParam) {
		this.queryParam = queryParam;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public List<Header> getHeader() {
		return header;
	}

	public void setHeader(List<Header> header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}

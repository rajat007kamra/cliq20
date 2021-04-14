package core.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiOutputModel {

	@SerializedName("responseBody")
	@Expose
	private String responseBody;
	@SerializedName("statusCode")
	@Expose
	private String statusCode;
	@SerializedName("statusMessage")
	@Expose
	private String statusMessage;
	@SerializedName("responseHeader")
	@Expose
	private String responseHeader;

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(String responseHeader) {
		this.responseHeader = responseHeader;
	}

}
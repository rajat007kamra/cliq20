package core.api.utility;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import actions.Actions;
import core.api.model.ApiOutputModel;
import core.api.model.Context;
import core.api.model.Header;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

/**
 * This class s is responsible for POST call executions
 * 
 * @author Surbhi.Singh
 *
 */
public class DeleteUtility implements Actions {
	private Instant startTime;
	private Instant endTime;
	private Context apiModel;
	private ApiOutputModel apiOutputModel;
	private String baseUrl;
	final static Logger logger = Logger.getLogger(DeleteUtility.class);

	public DeleteUtility(String json) {
		this.apiModel = new Gson().fromJson(json, Context.class);
		this.apiOutputModel = new ApiOutputModel();
		this.baseUrl = System.getProperty("baseUrl");
	}
	/**
	 * This method is responsible for setting Header for the API endpoint
	 *
	 */
	private Builder setHeaders(Builder builder, List<Header> headers) {
		for (Header header : headers) {
			builder.addHeader(header.getName(), header.getValue());
		}

		return builder;
	}
	/**
	 * This method is responsible for creating URL/Endpoint for the API 
	 *
	 */
	private Builder setUrl(Builder builder, String apiname, String apiVersion) {
		if (!this.baseUrl.endsWith("/")) {
			builder.url(this.baseUrl + "/" + apiVersion + "/" + apiname);
		} else {
			builder.url(baseUrl + apiVersion + "/" + apiname);
		}

		return builder;
	}

	private Builder setMethod(Builder builder, String jsonBody) {
		if (jsonBody != null) {
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonBody);
			builder.delete(body);
		} else {
			builder.delete();
		}
		return builder;
	}
	/**
	 * This method is responsible for sending the DELETE request to the URL/Endpoint 
	 *
	 */
	private ApiOutputModel doDelete(Context apiModel) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Builder builder = new Request.Builder();
			setUrl(builder, apiModel.getApi(), apiModel.getApiVersion());
			setHeaders(builder, apiModel.getHeader());
			okhttp3.Response apiResponse = client.newCall(builder.delete().build()).execute();
			this.apiOutputModel.setResponseBody(apiResponse.body().toString());
			this.apiOutputModel.setStatusCode(String.valueOf(apiResponse.code()));
			this.apiOutputModel.setStatusMessage(apiResponse.message());
			return this.apiOutputModel;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Date execute() {
		try {
			startTime = Instant.now();
			doDelete(apiModel);
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
		} catch (Exception e) {
			logger.error(apiModel.getApi() + ": FAILED");
			return null;
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			JsonElement responseBody = new Gson().fromJson(this.apiOutputModel.getResponseBody(), JsonElement.class);
			JsonObject statusElem = responseBody.getAsJsonObject().get("status").getAsJsonObject();

			if (this.apiModel.getResponse().getStatuscode() != null) {
				logger.info("Validate that status code is : " + this.apiModel.getResponse().getStatuscode());

				assertEquals(statusElem.get("code").getAsString(), this.apiModel.getResponse().getStatuscode(),
						"Status code is not as expected");
			}

			if (this.apiModel.getResponse().getStatusmessage() != null) {
				logger.info("Validate that status message is :" + this.apiModel.getResponse().getStatusmessage());
				assertEquals(statusElem.get("message").getAsString(), this.apiModel.getResponse().getStatusmessage(),
						"Status message is not as expected");
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Assert.fail();
			return null;
		}
	}

	@Override
	public Date setup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date tearDown() {
		// TODO Auto-generated method stub
		return null;
	}
}

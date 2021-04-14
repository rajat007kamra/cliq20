package core.api.utility;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import actions.Actions;
import core.api.model.ApiOutputModel;
import core.api.model.Context;
import core.api.model.Header;
import okhttp3.HttpUrl;
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
public class PostUtility implements Actions {
	private Instant startTime;
	private Instant endTime;
	private Context apiModel;
	private ApiOutputModel apiOutputModel;
	private String baseUrl;
	final static Logger logger = Logger.getLogger(PostUtility.class);

	public PostUtility(String json) {
		this.apiModel = new Gson().fromJson(json, Context.class);
		this.apiOutputModel = new ApiOutputModel();
		//this.baseUrl = System.getProperty("baseUrl");
		this.baseUrl = "https://apis-qabin-b-test.mezocliq.com/oas/api/";
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
	private Builder setUrl(Builder builder, String apiname, String apiVersion, List<Map<String, String>> queryParams) {
		String url = null;
		if (!this.baseUrl.endsWith("/")) {
			url = this.baseUrl + "/" + apiVersion + "/" + apiname;
		} else {
			url = this.baseUrl + apiVersion + "/" + apiname;
		}
		if (queryParams != null && !queryParams.isEmpty()) {
			okhttp3.HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
			for (Map<String, String> map : queryParams) {
				urlBuilder.addQueryParameter(map.get("NAME"), map.get("VALUE"));
			}
			HttpUrl httpUrl = urlBuilder.build();
			builder.url(httpUrl);
		} else {
			builder.url(url);
		}

		return builder;
	}

	private Builder setMethod(Builder builder, String name, String jsonBody) {
		if (jsonBody != null) {
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonBody);
			builder.post(body);
		}
		return builder;
	}
	/**
	 * This method is responsible for sending the POST request to the URL/Endpoint 
	 *
	 */
	private ApiOutputModel doPost(Context apiModel) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Builder builder = new Request.Builder();
			setUrl(builder, apiModel.getApi(), apiModel.getApiVersion(), apiModel.getQueryParam());
			setHeaders(builder, apiModel.getHeader());
			setMethod(builder, apiModel.getApi(), apiModel.getBody().toString());
			okhttp3.Response apiResponse = client.newCall(builder.build()).execute();
			this.apiOutputModel.setResponseBody(apiResponse.body().source().readUtf8());
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
			doPost(apiModel);
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
		} catch (Exception e) {
			e.printStackTrace();
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

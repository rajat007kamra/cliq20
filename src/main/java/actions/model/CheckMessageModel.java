package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckMessageModel {
	@SerializedName("action")
	@Expose
	private String action;
	@SerializedName("message")
	@Expose
	private String message;
	
	public String getActionName() {
		return action;
	}

	public void setActionName(String name) {
		this.action = name;
	}
	public String getVerifyMessage() {
		return message;
	}

	public void setVerifyMessage(String message) {
		this.message = message;
	}
}

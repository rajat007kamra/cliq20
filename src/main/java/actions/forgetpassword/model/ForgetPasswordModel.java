package actions.forgetpassword.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordModel {
	@SerializedName("username")
	@Expose
	private String username;
	
	@SerializedName("securityanswer")
	@Expose
	private String securityanswer;
	
	public String getanswer() {
		return securityanswer;
	}

	public void setanswer(String message) {
		this.securityanswer = message;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String message) {
		this.username = message;
	}
}
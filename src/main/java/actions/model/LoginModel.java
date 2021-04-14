package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
	@SerializedName("user")
	@Expose
	private String user;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("subtenant")
	@Expose
	private String subtenant;

	@SerializedName("expected_error")
	@Expose
	private String expected_error;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubtenant() {
		return subtenant;
	}

	public void setSubtenant(String subtenant) {
		this.subtenant = subtenant;
	}

	public String getExpected_error() {
		return expected_error;
	}

	public void setExpected_error(String expected_error) {
		this.expected_error = expected_error;
	}

}

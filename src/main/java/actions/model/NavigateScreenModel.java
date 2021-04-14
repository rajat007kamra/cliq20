package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NavigateScreenModel {
	@SerializedName("realm")
	@Expose
	private String realm;
	@SerializedName("process")
	@Expose
	private String process;
	@SerializedName("access")
	@Expose
	private String access;

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}
	
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
	
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}

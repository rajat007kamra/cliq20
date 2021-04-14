package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TimeLineModel {

	@SerializedName("flyout")
	@Expose
	private String flyout;
	
	@SerializedName("access")
	@Expose
	private String access;
	
	public String getAccess() {
		return access;
	}
	
	public void setAccess(String access) {
		this.access = access;
	}
	
	public String getFlyout() {
		return flyout;
	}
	
	public void setFlyout(String fly) {
		this.flyout = fly;
	}
}
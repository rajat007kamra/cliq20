package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrowserNavigationModel {
	@SerializedName("navigation")
	@Expose
	private String direction;

    public String getnavigation() {
		return direction;
	}

	public void setnavigation(String message) {
		this.direction = message;
	}
}
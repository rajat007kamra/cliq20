package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SwitchBrowserTabModel {
	@SerializedName("switchtab")
	@Expose
	private String direction;

    public String getswitchtab() {
		return direction;
	}

	public void setswitchtab(String message) {
		this.direction = message;
	}

}

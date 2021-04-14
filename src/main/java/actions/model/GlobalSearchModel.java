package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalSearchModel {
	@SerializedName("text")
	@Expose
	private String text;

	public String getGlobalSearch() {
		return text;
	}

	public void setGlobalSearch(String searchText) {
		this.text = searchText;
	}
}

package actions.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListsModel {
	@SerializedName("options")
	@Expose
	private List<String> options;

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> option) {
		this.options = option;
	}
}

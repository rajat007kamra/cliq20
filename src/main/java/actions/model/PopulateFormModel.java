package actions.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopulateFormModel {

	@SerializedName("column")
	@Expose
	private List<Column> column = null;

	public List<Column> getColumn() {
		return column;
	}

	public void setColumn(List<Column> column) {
		this.column = column;
	}
}

package actions.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormViewModel {
	@SerializedName("column")
	@Expose
	private List<Column> column = null;
	
	@SerializedName("selectRow")
	@Expose
	private String selectRow = null;

	public List<Column> getColumn() {
		return column;
	}

	public void setColumn(List<Column> column) {
		this.column = column;
	}
	
	public String getSelectRow() {
		return selectRow;
	}

	public void setSelectRow(String row) {
		this.selectRow = row;
	}
}
package actions.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryModel {
	@SerializedName("flyout")
	@Expose
	private String flyout;
	
	@SerializedName("access")
	@Expose
	private String access;
	
	@SerializedName("column")
	@Expose
	private List<Column> column = null;

	public List<Column> getColumn() {
		return column;
	}

	public void setColumn(List<Column> column) {
		this.column = column;
	}
	
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

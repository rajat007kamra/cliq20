package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Column {
	@SerializedName("title")
	@Expose
	private String title;

	@SerializedName("condition")
	@Expose
	private String condition;

	@SerializedName("text")
	@Expose
	private String text;

	@SerializedName("variable")
	@Expose
	private String variable;

	@SerializedName("value")
	@Expose
	private String value;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

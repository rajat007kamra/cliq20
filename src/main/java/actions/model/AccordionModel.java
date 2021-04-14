package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccordionModel {
	@SerializedName("accordion")
	@Expose
	private String accordion;
	
	@SerializedName("state")
	@Expose
	private String state;
	
	public String getAccordion() {
		return accordion;
	}

	public void setAccordion(String option) {
		this.accordion = option;
	}
	
	public String getAccordionState() {
		return state;
	}

	public void setAccordionState(String state) {
		this.state = state;
	}
}

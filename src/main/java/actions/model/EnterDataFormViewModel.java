package actions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Deprecated
public class EnterDataFormViewModel {
	@SerializedName("templateTypeField")
	@Expose
	private String templateTypeField;
	@SerializedName("methodField")
	@Expose
	private String methodField;
	@SerializedName("extentField")
	@Expose
	private String extentField;
	@SerializedName("processField")
	@Expose
	private String processField;
	@SerializedName("templateNameField")
	@Expose
	private String templateNameField;
//	@SerializedName("frequencyField")
//	@Expose
//	private String frequencyField;

	public String getTemplateType() {
		return templateTypeField;
	}

	public void setTemplateType(String templateType) {
		this.templateTypeField = templateType;
	}

	public String getMethod() {
		return methodField;
	}

	public void setMethod(String method) {
		this.methodField = method;
	}

	public String getExtent() {
		return extentField;
	}

	public void setExtent(String extent) {
		this.extentField = extent;
	}

	public String getProcessName() {
		return processField;
	}

	public void setProcessName(String process) {
		this.processField = process;
	}

	public String getTemplateName() {
		return templateNameField;
	}

	public void setTemplateName(String templateName) {
		this.templateNameField = templateName;
	}

//	public String getFrequencyName() {
//		return frequencyField;
//	}
//
//	public void getFrequencyName(String frequencyName) {
//		this.frequencyField = frequencyName;
//	}
}

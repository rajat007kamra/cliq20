package actions.viewzone;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebElement;

import actions.PageHelper;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import io.github.sukgu.Shadow;
import selenium.driver.Driver;

public class FormViewTemplate extends Driver {
	final static Logger logger = Logger.getLogger(FormViewTemplate.class);
	private String fieldname;
	private Document document;
	public String zframeXpathLocator = "div.form-container-renderer";
	public String slotXpathLocator = "div.section-level1";
	public String headerXpathLocator = "h2#title.mdc-dialog__title";
	public String wgtXpathLocator = "div.grid-column";

	public String widgetInputXpathLocator = "mz-textfield[label='%s']";
	public String sectionInputXpathLocator = "body mz-dialog mz-form-renderer div.form-container mz-entryfield mz-textfield[label='%s'] label.mdc-text-field input.mdc-text-field__input";
	public String sectionGetXpathLocator = "body mz-dialog mz-form-renderer div.form-container mz-entryfield mz-textfield[label='%s']";
	private Shadow shadow;

	public FormViewTemplate(String fieldname) throws IOException {
		super(getDriver());
		shadow = new Shadow(getDriver());
		this.fieldname = fieldname;
	}

	public Map<String, String> counstructXpath(String type, String Value, String Variable) {
		Map<String, String> xpathMap = new HashMap<String, String>();
		List<WebElement> elements = null;
		if (type.equals("put")) {
			elements = shadow.findElements(String.format(sectionInputXpathLocator, Variable));
			for (WebElement elem : elements) {
				PageHelper.clickUsingJs(shadow, elem);
				PageHelper.sendKeys(shadow, elem, Value);
			}
		} else if (type.equals("get")) {
			elements = shadow.findElements(String.format(sectionGetXpathLocator, Variable));
			for (WebElement elem : elements) {
				if (elem.getText().contains(Variable)) {
					logger.info(elem.getText());
				}
			}
		}
		return xpathMap;
	}

}

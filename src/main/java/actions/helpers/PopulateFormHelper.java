package actions.helpers;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

public class PopulateFormHelper {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(PopulateFormHelper.class);
	private String variableField = "mz-dialog mz-entryfield mz-textfield[label='%1s']";
	private String valueField = "mz-dialog mz-entryfield mz-textfield[label='%1s'] label.mdc-text-field input.mdc-text-field__input";
	Shadow shadow;

	public PopulateFormHelper(WebDriver driver) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	@Step("Fetch the variable number of column {columnName}")
	public Integer getFieldNumber(String columnName) {
		int counter = -1;
		String variable = null;
		String updateVariable = null;
		logger.info(String.format(variableField, columnName));
		List<WebElement> variableElements = shadow.findElements((String.format(variableField, columnName)));
		for (int i = 0; i < variableElements.size(); i++) {
			variable = PageHelper.getText(shadow, variableElements.get(i));
//			if (variable.contains("*")) {
//				updateVariable = variable.replace("*", "");
//			} else {
//				updateVariable = variable;
//			}
			if ((variable.trim()).equals(columnName)) {
				counter = i;
				break;
			}
		}
		return counter;
	}

	@Step("Set value in variable")
	public String setFieldValue(String columnName, String varValue, int index) throws InterruptedException {
		int i;
		List<WebElement> valueElements = shadow.findElements((String.format(valueField, columnName)));
		for (i = 0; i < valueElements.size(); i++) {
			if (i == index) {
				PageHelper.click(shadow, valueElements.get(i));
				Thread.sleep(5000);
				PageHelper.sendKeys(shadow, valueElements.get(i), varValue);
				Thread.sleep(5000);
				valueElements.get(i).sendKeys(Keys.TAB);
				break;
			} else {

			}
		}
		return varValue;
	}
}

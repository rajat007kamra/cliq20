package actions.editzone;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.model.Column;
import actions.model.SummaryModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * @summary Handles Summary Button
 * @author Rajat.Kamra
 *
 */
public class Summary implements Actions {
	private WebDriver driver;
	private SummaryModel summaryModel;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(Summary.class);

	public Summary(WebDriver driver, String jsonString) {
		super();
		this.driver = driver;
		shadow = new Shadow(driver);
		this.summaryModel = new Gson().fromJson(jsonString, SummaryModel.class);
	}

	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Summary'] div.icon-label-button--disabled";
	private String summaryIcon = "mz-standard-realm[active] mz-icon-label-button[text='Summary'] div.icon-label-button";
	private String pinIcon = "mz-standard-realm[active] mz-editzone-content-swapper div.editzone.flx-ver-ctr div.buttons__pin-button mwc-icon[title='pin']";
	private String summaryVariableName = "mz-standard-realm[active] mz-textfield#input-element[label='%s']";
	private String summaryVariableValue = "mz-standard-realm[active] mz-textfield#input-element[label='%s'] label.mdc-text-field input.mdc-text-field__input";

	@Override
	public Date execute() {
		try {
			checkIfIconIsDisabled(this.driver, "Summary");
			WebElement element = this.shadow.findElement(summaryIcon);
			element.click();
			clickPinIcon();
			Thread.sleep(5000);
			List<Column> columns = this.summaryModel.getColumn();
			for (Column column : columns) {
				logger.info("Fetch field number in table for given field " + column.getVariable());
				int columnNumber = getVariableNumber(column.getVariable());
				if (columnNumber >= 0) {
					logger.info("Set text " + PageHelper.appendHashCode(this.driver, column.getValue().trim())
							+ " in field number " + columnNumber);
					setVariableValue(column.getVariable(),
							PageHelper.appendHashCode(this.driver, column.getValue().trim()), columnNumber);
					logger.warn("Static wait introduced");
					PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
					logger.info("Wait for page to load");
					PageHelper.waitForPageLoad(this.driver);
				} else {
					logger.error(column.getVariable() + " does not exists");
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
//		checkIfIconIsDisabled(this.driver, "SUMMARY");
		return null;
	}

	@Override
	public Date setup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date tearDown() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @summary Method To verify if icon is disabled or not
	 * @param driver
	 * @param button
	 * @return
	 */
	public boolean checkIfIconIsDisabled(WebDriver driver, String button) {
		boolean isActive = false;
		logger.info("Verify " + button + " icon");
		PageHelper.waitInSeconds(driver, PageHelper.X_TIMEOUT_SEC);
		try {
			if (driver.findElements(By.cssSelector(String.format(disabledIcons, button))).size() > 0) {
				isActive = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isActive;
	}

	/**
	 * @summary Method to pin Edit Zone
	 */
	public void clickPinIcon() {
		if (shadow.findElements(pinIcon).size() > 0) {
			shadow.findElement(pinIcon).click();
			logger.info("Pin icon clicked");
		} else {
			Assert.assertFalse(true, "Pin Icon not found");
		}
	}

	/**
	 * @summary Fetch the variable number of column
	 * @param varName
	 * @return
	 * @throws InterruptedException
	 */
	@Step("Fetch the variable number of column {varName}")
	public Integer getVariableNumber(String varName) throws InterruptedException {
		int counter = -1;
		List<WebElement> columnHeaders = shadow.findElements(String.format(summaryVariableName, varName));
		for (int i = 0; i < columnHeaders.size(); i++) {
			String columnName = PageHelper.getText(shadow, columnHeaders.get(i));
			if ((columnName.trim()).equals(varName)) {
				counter = i;
				break;
			}
		}
		return counter;
	}

	/**
	 * @summary Set value in variable
	 * @param varName
	 * @param varValue
	 * @param index
	 * @return
	 * @throws InterruptedException
	 */
	@Step("Set value in variable")
	public String setVariableValue(String varName, String varValue, int index) throws InterruptedException {
		int i;
		List<WebElement> valueElements = shadow.findElements((String.format(summaryVariableValue, varName)));
		for (i = 0; i < valueElements.size(); i++) {
			if (i == index) {
				PageHelper.click(shadow, valueElements.get(i));
				Thread.sleep(2000);
				PageHelper.clearText(shadow, valueElements.get(i));
				Thread.sleep(2000);
				PageHelper.sendKeys(shadow, valueElements.get(i), varValue);
				Thread.sleep(2000);
				valueElements.get(i).sendKeys(Keys.TAB);
				break;
			} else {

			}
		}
		return varValue;
	}
}
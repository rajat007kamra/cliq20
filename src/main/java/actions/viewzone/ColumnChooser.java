package actions.viewzone;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * @summary This class contains verify, click, and validate COLUMN CHOOSER button 
 * @author Arun.Kapoor
 */

public class ColumnChooser implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(ColumnChooser.class);
	Shadow shadow;

	public ColumnChooser(WebDriver driver, String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	public static String viewZoneButtonsXpath = "mz-viewzone[active] mz-icon-label-button[text='%s']";
	public static String commonViewZoneButtonClass = "mz-standard-realm[active] mz-icon-label-button[text='%s'] div.icon-label-button.icon-label-button--disabled";
	private String columnChooser = "mz-viewzone-column-chooser span.advanced-chooser__text";
	private String buttonName = "COLUMN CHOOSER";

	@Override
	public Date execute() {
		try {
			verifyIsButtonDisabled(buttonName);
			clickColumnChooserButton(this.driver, buttonName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date setup() {
		return null;
	}

	@Override
	public Date tearDown() {
		return null;
	}

	/**
	 * @summary This method is responsible to check whether COLUMN CHOOSER icon is enabled or disabled in viewzone 
	 * @param Button_Action
 	 * @return
	 */
	// Method to verify is viewzone buttons disabled
	public void verifyIsButtonDisabled(String Button_Action) {
		try {
			PageHelper.waitInSeconds(driver, PageHelper.X_TIMEOUT_SEC);
			if (shadow.findElements(String.format(commonViewZoneButtonClass, buttonName)).size() > 0) {
				String erroMsg = Button_Action + " icon is disabled or not found";
				logger.error(erroMsg);
				Assert.assertTrue(false, erroMsg);
			} else {
				logger.info(Button_Action + " icon is enabled");
			}
		} catch (ElementNotVisibleException e) {
			return;
		}
	}

	/**
	 * @summary This method is responsible to click on COLUMN CHOOSER icon 
	 * @param driver, buttonName
 	 * @return
	 */
	// Method to click on Column Chooser button
	@Step("Clicked at button name {buttonName}")
	public void clickColumnChooserButton(WebDriver driver, String buttonName) {
		if(shadow.findElements(String.format(viewZoneButtonsXpath, buttonName)).size()>0) {
			shadow.findElement(String.format(viewZoneButtonsXpath, buttonName)).click();
		}
	}
	
	/**
	 * @summary This method is responsible to validate whether on click of COLUMN CHOOSER, A new form opened 
	 */
	public void validateColumnChooserButton()
	{
		WebElement element = this.shadow.findElement(columnChooser);
		if (element.getText().equals("Advanced chooser")) {
			logger.info(buttonName + " validated successfully");
		} else {
			Assert.assertTrue(false,
					"Either " + buttonName + " not clicked or clicked but list not opened. Please check");
		}
	}
}

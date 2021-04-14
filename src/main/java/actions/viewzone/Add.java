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
 * @summary This class contains verify, click, and validate ADD Record icon
 * @author Arun.Kapoor
 */

public class Add implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(Add.class);
	private Shadow shadow;

	public Add(WebDriver driver, String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	public static String viewZoneButtonsXpath = "mz-viewzone[active] mz-icon-label-button[text='%s']";
	public static String formViewCloseIcon = "mz-form-renderer div.dialog-close-btn-hldr mz-icon-button[title='Close']";
	public static String commonViewZoneButtonClass = "mz-standard-realm[active] mz-icon-label-button[text='%s'] div.icon-label-button.icon-label-button--disabled";
	private String buttonName = "ADD RECORD";

	@Override
	public Date execute() {
		try {
			verifyIsButtonDisabled(buttonName);
			clickAddRecordButton(this.driver, buttonName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			validateAddRecordButton();
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
	 * @summary This method verifies whether ADD RECORD icon is disabled or enabled in viewzone  
	 * @param Button_Action
 	 * @return
	 */
	public void verifyIsButtonDisabled(String Button_Action) {
		try {
			PageHelper.waitInSeconds(driver, PageHelper.X_TIMEOUT_SEC);
			if (shadow.findElements(String.format(commonViewZoneButtonClass, buttonName)).size()>0) {
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
	 * @summary This method use to click on ADD RECORD icon 
	 * @param driver, buttonName
	 */
	@Step("Clicked at button name {buttonName}")
	public void clickAddRecordButton(WebDriver driver, String buttonName)
	{
		WebElement element = shadow.findElement(String.format(viewZoneButtonsXpath, buttonName));
		element.click();
	}
	
	/**
	 * @summary This method use to validate whether on click of ADD RECORD, A new form opened 
	 */
	public void validateAddRecordButton()
	{
		if (this.shadow.findElements(formViewCloseIcon).size() > 0) {
			logger.info(buttonName + " button validated successfully");
			//this.shadow.findElement(formViewCloseIcon).click();
		} else {
			Assert.assertTrue(false, "Either " + buttonName + "  not clicked or clicked but form not opened. Please check");
		}
	}
}
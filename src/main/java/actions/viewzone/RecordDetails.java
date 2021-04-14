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
 * @summary This class contains verify, click, and validate Record Details icon
 * @author Arun.Kapoor
 */

public class RecordDetails implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(RecordDetails.class);
	Shadow shadow;

	public RecordDetails(WebDriver driver,String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}
	
	public static String viewZoneButtonsXpath = "mz-viewzone[active] mz-icon-label-button[text='%s']";
	public static String formViewCloseIcon = "mz-form-renderer div.dialog-close-btn-hldr mz-icon-button[title='Close']";
	public static String commonViewZoneButtonClass = "mz-standard-realm[active] mz-icon-label-button[text='%s'] div.icon-label-button.icon-label-button--disabled";
	private String buttonName = "RECORD DETAILS";
	
	@Override
	public Date execute() {
		try {
			verifyIsButtonDisabled(buttonName);
			clickRecordDetailsButton(this.driver, buttonName);
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
			validateRecordDetailsButton();
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
	 * @summary This method use to click on Record Details icon 
	 * @param driver, buttonAction
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
	 * @summary This method use to click on Record Details icon 
	 * @param driver, buttonAction
 	 * @return
	 */
	@Step("Clicked at button name {buttonName}")
	public void clickRecordDetailsButton(WebDriver driver, String buttonName)
	{
		WebElement element = shadow.findElement(String.format(viewZoneButtonsXpath, buttonName));
		element.click();
	}
	
	/**
	 * @summary This method use to validate whether on click of Record Details, A new form opened 
	 */
	public void validateRecordDetailsButton()
	{
		if (this.shadow.findElements(formViewCloseIcon).size() > 0) {
			logger.info(buttonName + " button validated successfully");
			this.shadow.findElement(formViewCloseIcon).click();
		} else {
			Assert.assertTrue(false, "Either " + buttonName + "  not clicked or clicked but form not opened. Please check");
		}
	}
}
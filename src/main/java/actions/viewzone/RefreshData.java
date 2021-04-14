package actions.viewzone;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * @summary This class is to refresh page
 * @author Arun.Kapoor
 */

public class RefreshData implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(RefreshData.class);
	Shadow shadow;

	public RefreshData(WebDriver driver,String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	public static String viewZoneButtonsXpath = "mz-viewzone[active] mz-icon-label-button[text='%s']";
	public static String formViewCloseIcon = "mz-form-renderer div.dialog-close-btn-hldr mz-icon-button[title='Close']";
	public static String commonViewZoneButtonClass = "mz-standard-realm[active] mz-icon-label-button[text='%s'] div.icon-label-button.icon-label-button--disabled";
	private String buttonName = "REFRESH DATA";

	@Override
	public Date execute() {
		try {
			verifyIsButtonDisabled(buttonName);
			clickRefreshButton(this.driver, buttonName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			logger.info("Refresh Data working fine");
			//No suitable validation found for this action so only message passed
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
	 * @summary This method verifies whether REFRESH icon is disabled or enabled in viewzone  
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
	 * @summary This method use to click on REFRESH icon 
	 * @param driver, buttonAction
 	 * @return
	 */
	@Step("Clicked at button name {buttonName}")
	public void clickRefreshButton(WebDriver driver, String buttonName)
	{
		if(shadow.findElements(String.format(viewZoneButtonsXpath, buttonName)).size()>0) {
			shadow.findElement(String.format(viewZoneButtonsXpath, buttonName)).click();
		}
	}
}
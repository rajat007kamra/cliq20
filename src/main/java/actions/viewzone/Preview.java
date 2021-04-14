package actions.viewzone;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * @summary This class contains verify, click, and validate Preview icon
 * @author Arun.Kapoor
 */

public class Preview implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(Preview.class);
	Shadow shadow;

	public Preview(WebDriver driver, String jsonString) {
//		super(driver);
		this.driver = driver;
		shadow = new Shadow(driver);
	}
	
	public static String viewZoneButtonsXpath = "mz-viewzone[active] mz-icon-label-button[text='%s']";
	public static String commonViewZoneButtonClass = "mz-standard-realm[active] mz-icon-label-button[text='%s'] div.icon-label-button.icon-label-button--disabled";
	public static String previewPopUp = "mz-solution-preview-status div.flx-hor-ctr mz-button";
	private String buttonName = "PREVIEW";
	
	@Override
	public Date execute() {
		try {
			verifyIsButtonDisabled(buttonName);
			clickPreviewButton(this.driver, buttonName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			previewButtonValidation();
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
	 * @summary This method verifies whether Preview icon is disabled or enabled in viewzone  
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
	 * @summary This method use to click on Preview icon 
	 * @param driver, buttonAction
 	 * @return
	 */
	@Step("Clicked at button name {buttonName}")
	public void clickPreviewButton(WebDriver driver, String buttonName)
	{
		WebElement element = shadow.findElement(String.format(viewZoneButtonsXpath, buttonName));
		element.click();
	}
	
	/**
	 * @summary This method use to validate whether on click of Preview, A preview popup opened 
	 */ 
	public void previewButtonValidation() throws InterruptedException
	{
		TimeUnit.SECONDS.sleep(10);
		List<WebElement> buttons = shadow.findElements(previewPopUp);
		if(buttons.size()==2)
		{
			logger.info("Preview icon working fine");
			buttons.get(1).click();
			logger.info("Close icon clicked in preview popup");
		}
		else {
			Assert.assertTrue(false, "Launch button not found in Preview PopUp or not loaded within default element found time span");
		}
	}
}
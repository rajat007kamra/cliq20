package actions.logout;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import actions.Actions;
import actions.PageHelper;
import common.variables.CommonVariables;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import selenium.driver.Driver;

/**
 * 
 * @author Surbhi.singh
 *
 */

/**
 * TODO: Input model goes here.
 */
public class Logout extends Driver implements Actions {
	public static String forgot = "span.content-link.flt-rght";
	public static String securityanswer = "input.mdc-text-field__input";
	public static String send_button = "mz-button#submitButton.flt-rght.button-main";
	public static String message = "span.error-text";
	public static String closeDialogue = "mz-dialog mz-form-renderer mz-icon-button[title='Close'] i.material-icons";
	public static String closeToolbar = "mz-dialog mz-expasable-toolbar mz-icon-button[title='Close'] i.material-icons";
	public static String closeLists = "mz-dialog mz-lists-builder mz-icon-button[title='Close'] i.material-icons";
	public static String userSettings = "kor-avatar#options-btn div.image";
	public static String logoutButton = "mwc-list-item#test__logout";
	public static String loginButton = "mz-button#submitButton";

	private Shadow shadow;
	final static Logger logger = Logger.getLogger(Logout.class);

	public Logout(WebDriver driver, String jsonString) {
		super(driver);
		shadow = new Shadow(driver);
	}

	@Override
	public Date execute() {
		try {
			Thread.sleep(10000);
			checkAndCloseDialogue();
			logger.info("open user settings");
			clickUserSettings();
			logger.info("click logout option");
			clickLogoutButton();
			CommonVariables.actionTime = new Date().getTime();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			Thread.sleep(5000);
			Assert.assertEquals(verifyLogout().trim(), "SIGN IN");
			CommonVariables.responseTime = new Date().getTime();
			CommonVariables.timeTaken = (CommonVariables.responseTime - CommonVariables.actionTime);
			logger.info("[RESPTIME] " + CommonVariables.timeTaken);
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date setup() {
		logger.info("To Be configured");
		return null;
	}

	@Override
	public Date tearDown() {
		logger.info("To Be configured");

		return null;
	}

	@Step("Click user settings")
	private void clickUserSettings() {
		logger.info("Click user settings !!");
		WebElement _userSettings = shadow.findElement(userSettings);
		PageHelper.click(shadow, _userSettings);
	}

	@Step("Click logout button")
	private void clickLogoutButton() {
		logger.info("Click on logout button");
		WebElement _logoutButton = shadow.findElement(logoutButton);
		PageHelper.waitForElementToBeDisplayed(shadow, _logoutButton);
		PageHelper.clickUsingJs(shadow, _logoutButton);
	}

	@Step("Validate logout button")
	private String verifyLogout() {
		WebElement _loginButton = shadow.findElement(loginButton);
		logger.info("After logout verified text found ::- " + PageHelper.getText(shadow, _loginButton));
		return PageHelper.getText(shadow, _loginButton);
	}

	// Check if any Widget opened
	private void checkAndCloseDialogue() {
		if (shadow.findElements(closeDialogue).size() > 0) {
			shadow.findElement(closeDialogue).click();
			logger.info("Opened widget dialogue box closed");
		} else if (shadow.findElements(closeToolbar).size() > 0) {
			shadow.findElement(closeToolbar).click();
			logger.info("Opened widget toolbar box closed");
		} else if (shadow.findElements(closeLists).size() > 0) {
			shadow.findElement(closeLists).click();
			logger.info("Opened widget lists box closed");
		}
	}
}

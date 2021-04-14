package actions.login;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.model.LoginModel;
import actions.readcredentials.UserConfigYamlReader;
import common.variables.CommonVariables;
import core.db.DataExtractor;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import selenium.driver.Driver;

/**
 * 
 * @author Surbhi.singh
 *
 */

public class Login extends Driver implements Actions {

	public static String Login_submit = "mz-button#submitButton";
	public static String Login_element = "input.mdc-text-field__input";
	public static String Login_subtenant = "input.mdc-text-field__input";
	public static String subTenantDropDownExposed = "i.material-icons.mdc-text-field__icon.mdc-text-field__icon--trailing";
	public static String subTenantDropDown = "mz-button#submitButton.flt-rght.button-main";
	public static String staleness = "input.mdc-text-field__input";
	public static String readSubTenant = "div.logo.header__logo";
	public static String readNotificationIcon = "mz-notifications div.notifications";
	public static String Login_ErrorMessage = "div.login-error-message span.error-text";

	private LoginModel loginModel;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(Login.class);

	public Login(WebDriver driver, String jsonString) {
		super(driver);
		shadow = new Shadow(driver);
		this.loginModel = new Gson().fromJson(jsonString, LoginModel.class);
	}

	@Override
	public Date execute() {
		try  {
			PageHelper.maximizeWindow();
			logger.info("LOGIN: Set username " + loginModel.getUser());
			if(!DataExtractor.isEmpty(this.loginModel.getUser()))
			{
				setUserName(this.loginModel.getUser());
			}
			if(!DataExtractor.isEmpty(this.loginModel.getPassword())) {
				logger.info("LOGIN: Set password " + this.loginModel.getPassword());
				setPassword(this.loginModel.getPassword());
			} else {
//				logger.info("Get Password from DB");
//				String password = DbHelper.getPassword(this.loginModel.getUser());
				logger.info("LOGIN: Get password from UserConfig");				
				String password = UserConfigYamlReader.getInstance().getUserData(System.getProperty("appUrl"), this.loginModel.getUser()).getPassword();
				logger.info("LOGIN: Set password " + password);
				setPassword(password);
			}
			logger.info("LOGIN: Click login button");
			clickLoginButton();
			CommonVariables.actionTime = new Date().getTime();
			if (!DataExtractor.isEmpty(this.loginModel.getSubtenant())) {
				logger.info("LOGIN: Set subtenant " + loginModel.getSubtenant());
				setSubTanent(this.loginModel.getSubtenant());
				logger.info("LOGIN: Click submit button");
				clickSubmitButton();
			} 
//			else {
//				logger.info("LOGIN: Get subtenant from Testconfiguration");
//				String subtenant = TestConfigYamlReader.getInstance().getUserData("qabin-b-test.mezocliq.com", "LEM.IND.USER.01").getSubtenant();
//				logger.info("LOGIN: Set subtenant " + subtenant);
//				setSubTanent(subtenant);
//				logger.info("LOGIN: Click submit button");
//				clickSubmitButton();
//			}
			TimeUnit.SECONDS.sleep(5);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			TimeUnit.SECONDS.sleep(5);
			checkWhetherShowingErrorMessage();
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
		logger.info("LOGIN: Login action setup is not yet configured");
		return null;
	}

	@Override
	public Date tearDown() {
		logger.info("LOGIN: Login action teardown is not yet configured");
		return null;
	}

	@Step("Enter username {username}")
	private void setUserName(String username) {
		List<WebElement> element = this.shadow.findElements(Login_element);
		PageHelper.sendKeys(shadow, element.get(0), username);
	}

	@Step("Enter password {password}")
	private void setPassword(String password) {
		List<WebElement> element = this.shadow.findElements(Login_element);
		PageHelper.sendKeys(shadow, element.get(1), password);
	}

	private void clickLoginButton() {
		WebElement login = shadow.findElement(Login_submit);
		login.click();
	}

	@Step("Select subtenant {subtanentName}")
	private boolean setSubTanent(String subtanentName) {
		try {
			// Subtenant button
			PageHelper.waitForShadowElementStaleness(shadow, shadow.findElement(staleness), 5);
			WebElement subtenantField = shadow.findElement(Login_subtenant);
			TimeUnit.SECONDS.sleep(5);
			WebElement _subTenantDropDownExposed = shadow.findElement(subTenantDropDownExposed);	// subtenant drop-down exposed
			WebElement _subTenantDropDown = shadow.findElement(subTenantDropDown);	// subtenant drop-down exposed
			PageHelper.click(shadow, subtenantField);
			subtenantField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
			PageHelper.sendKeys(shadow, subtenantField, subtanentName);
			TimeUnit.SECONDS.sleep(5);
			PageHelper.waitForElementToBeDisplayed(shadow, _subTenantDropDownExposed);
			subtenantField.sendKeys(Keys.TAB);
			PageHelper.waitForElementToBeDisplayed(shadow, _subTenantDropDown);
			return true;
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
			return false;
		}
	}

	@Step("Submit button click")
	private void clickSubmitButton() {
		WebElement submitButton = shadow.findElement(Login_submit);
		PageHelper.click(shadow, submitButton);
	}

	@Step("Verify subtenant name at home page after login")
	private void verifySubtenantNameHomePage() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		WebElement SubTenant = shadow.findElement(readSubTenant);
		Assert.assertEquals(PageHelper.isElementDisplayed(SubTenant), true);
	}

	@Step("Verify the error message {errorMessage}")
	private void verifyErrorMessage(String errorMessage) {
		WebElement ExpectedErrorMessage = shadow.findElement(Login_ErrorMessage);
		PageHelper.waitForElementToBeDisplayed(shadow, ExpectedErrorMessage);
		PageHelper.waitForTextToPresent(shadow, ExpectedErrorMessage, errorMessage.trim(), PageHelper.DEFAULT_TIMEOUT_SEC);
		String errorText = PageHelper.getText(shadow, ExpectedErrorMessage);
		logger.info("LOGIN: expected error text displayed is " + errorText + "but expected error text is " + errorMessage);
		Assert.assertEquals(errorMessage.trim(), errorText.trim(), "LOGIN: Error message must be poped up");
	}

	// Method to check whether Login failed or not
	private void checkWhetherShowingErrorMessage() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		if (shadow.findElements(Login_ErrorMessage).size() > 0) {
			if (!DataExtractor.isEmpty(this.loginModel.getExpected_error())) {
				verifyErrorMessage(this.loginModel.getExpected_error());
			} else {
				Assert.assertTrue(false, "LOGIN: Login failed and error message is - " + shadow.findElement(Login_ErrorMessage).getText());
			}
		} else {
			verifySubtenantNameHomePage();
		}
	}
}
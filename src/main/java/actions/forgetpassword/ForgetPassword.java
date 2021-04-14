package actions.forgetpassword;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.forgetpassword.model.ForgetPasswordModel;
import io.github.sukgu.Shadow;
import selenium.driver.Driver;

public class ForgetPassword extends Driver implements Actions {
	private WebDriver driver;
	private ForgetPasswordModel forgetModel;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(ForgetPassword.class);
	public ForgetPassword(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		shadow = new Shadow(driver);
		this.forgetModel = new Gson().fromJson(jsonString, ForgetPasswordModel.class);
	}
	
	public static String forgot = "span.content-link.flt-rght";	
	public static String securityanswer = "input.mdc-text-field__input";
	public static String send_button = "mz-button#submitButton.flt-rght.button-main";
	public static String messagePath = "span.error-text";
	public static String marshal_msg = "LOGIN FAILED: PLEASE CONTACT YOUR MARSHAL";
	public static String Unknown = "UNKNOWN";

	@Override
	public Date execute() {
		try {
			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			ForgotPassword(this.forgetModel.getusername(), this.forgetModel.getanswer());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
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

	public void ForgotPassword(String username, String answer) throws InterruptedException {
		WebElement forget = shadow.findElement(forgot);
		List<WebElement> element = shadow.findElements("input.mdc-text-field__input");
		
		if (this.forgetModel.getusername() != null) {
			if (PageHelper.isElementDisplayed(forget)) {
				PageHelper.sendKeys(shadow, element.get(0), username);
				PageHelper.click(shadow, forget);
				logger.info("Click on Forgot Password ");
				WebElement secureanswer = shadow.findElement(securityanswer);
				PageHelper.sendKeys(shadow, secureanswer, answer);
				logger.info("Security answer is " + answer);
				WebElement sendbutton = shadow.findElement(send_button);
				PageHelper.click(shadow, sendbutton);
				Thread.sleep(10000);
				WebElement message = shadow.findElement(messagePath);
				logger.info("Submit button clicked");
				String msg = PageHelper.getText(shadow, message);
				if (msg.equals(Unknown)) {
					logger.info("UNKNOWN Error, Incorrect Answer");
				} else {
					logger.info(msg);
				}
			} else {
				logger.info("Forgot Password Buuton Not Found");
			}
		} else {
			logger.info("Either Username is Invalid or Empty");
		}
	}
}
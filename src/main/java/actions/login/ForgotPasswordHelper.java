package actions.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

public class ForgotPasswordHelper {
	private WebDriver driver;
	private String parentLocator = "div.DECKLAYER-PARENT[style*='z-index: 1;']";
	private String forgotPassLoc = parentLocator + " div.PASSWORD-LABEL";
	private String securityQuestionLoc = parentLocator + " div.MODAL-GROUP-HEADER-EL-DISPLAY";
	private String securityAnswerLoc = parentLocator + " input.ENTRYFLD-TXT-MODAL-DEFAULT";
	private String sendButtonLoc = parentLocator + " div.MODAL-BUTTON-SUBMIT";
	private Shadow shadow;

	public ForgotPasswordHelper(WebDriver driver) {
		shadow = new Shadow(driver);
	}

	@Step("Click forgot password link")
	public void clickForgotPassword() {
		By byForgotPass = By.cssSelector(forgotPassLoc);
		PageHelper.waitForElementVisibility(driver, byForgotPass, PageHelper.XX_TIMEOUT_SEC);
		WebElement elem = this.driver.findElement(byForgotPass);
		PageHelper.click(shadow, elem);
	}

	@Step("Enter security answer and click send button")
	public void handelVerificationPopUp() {
		By bySecAnswer = By.cssSelector(securityQuestionLoc);
		PageHelper.waitForElementVisibility(driver, bySecAnswer, PageHelper.XX_TIMEOUT_SEC);
		WebElement answerElem = this.driver.findElement(bySecAnswer);
		WebElement sendButtonElem = this.driver.findElement(By.cssSelector(sendButtonLoc));
		String securityAnswer = getSecurityAnswer();
		PageHelper.sendKeys(shadow, answerElem, securityAnswer);
		PageHelper.click(shadow, sendButtonElem);
	}

	private String getSecurityAnswer() {
		String answer = null;
		By bySecQuestion = By.cssSelector(securityQuestionLoc);
		PageHelper.waitForElementVisibility(driver, bySecQuestion, PageHelper.XX_TIMEOUT_SEC);
		WebElement questionElem = this.driver.findElement(bySecQuestion);
		String securityQuestion = PageHelper.getText(shadow, questionElem);

		String substring = securityQuestion.substring(securityQuestion.lastIndexOf(' '), securityQuestion.length());
		if (securityQuestion.endsWith("?")) {
			answer = securityQuestion.replace("?", "").trim();
		}
		return answer;
	}

	public void getTempPasswordFromEmail() {
	
	}

	public void LoginWithTempPassword(String tmpPassword) {

	}

	public void changePassword() {

	}

	private void updatePasswordInDb() {

	}

}

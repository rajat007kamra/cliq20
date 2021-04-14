package actions.viewzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.google.gson.Gson;
import actions.PageHelper;
import actions.model.CheckMessageModel;
import common.variables.CommonVariables;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;


public class CheckMessage implements ViewZone {
	private Instant startTime;
	private Instant endTime;
	private WebDriver driver;
	private CheckMessageModel checkMsgModel;
	final static Logger logger = Logger.getLogger(CheckMessage.class);
	Shadow shadow;

	public CheckMessage(WebDriver driver, String jsonString) {
//		super(driver);
		this.driver = driver;
		shadow = new Shadow(driver);
		this.checkMsgModel = new Gson().fromJson(jsonString, CheckMessageModel.class);
	}

	private String checkMainMessage;

//	private String CheckTopMessage = "div.cmplt-noti-dlg-lbl.cmplt-noti-dlg-lbl-top";
	
	@FindBy(css = "div.cmplt-noti-dlg-lbl.cmplt-noti-dlg-lbl-bottom")
	private String checkBottomMessage ;
	
	@Override
	public Date execute() {
		try {
			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			startTime = Instant.now();
			Assert.assertEquals(getMessage(), this.checkMsgModel.getVerifyMessage().trim().toUpperCase());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Message not found");
			return null;
		}
	}

	@Override
	public Date validate() {
		endTime = Instant.now();
		logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
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

	@Step("Get message")
	private String getMessage()
	{
		WebElement check_mainMessage = shadow.findElement(checkMainMessage);
		WebElement Check_topMessage = shadow.findElement(checkMainMessage);
		WebElement Check_bottomMessage = shadow.findElement(checkMainMessage);
		
		if (this.checkMsgModel.getActionName().trim().toUpperCase().equals("SAVE")
				|| this.checkMsgModel.getActionName().trim().toUpperCase().equals("SUBMIT")
				|| this.checkMsgModel.getActionName().trim().toUpperCase().equals("REFRESH")
				|| this.checkMsgModel.getActionName().trim().toUpperCase().equals("RESET")) {
			CommonVariables.notificationMsg = PageHelper.getText(shadow, check_mainMessage);
			logger.info("Notification message found ::- " + CommonVariables.notificationMsg);
		} else if (this.checkMsgModel.getActionName().trim().toUpperCase().equals("DUPLICATE")
				|| this.checkMsgModel.getActionName().trim().toUpperCase().equals("DOWNLOAD")) {
			logger.info("waiting for downloading message popup");
			PageHelper.waitForElementToBeDisplayed(shadow, Check_topMessage);
			CommonVariables.notificationMsg = PageHelper.getText(shadow, Check_topMessage);
			logger.info("Top notification message found ::- " + CommonVariables.notificationMsg);
			String bottomNotification = PageHelper.getText(shadow, Check_bottomMessage);
			logger.info("Bottom notification message found ::- " + bottomNotification);
		}
		return CommonVariables.notificationMsg;
	}
}

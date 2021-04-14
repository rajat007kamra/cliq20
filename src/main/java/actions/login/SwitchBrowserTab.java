package actions.login;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.model.SwitchBrowserTabModel;
import io.qameta.allure.Step;
import selenium.driver.Driver;

public class SwitchBrowserTab extends Driver implements Actions {
	private WebDriver driver;
	private SwitchBrowserTabModel model;
	final static Logger logger = Logger.getLogger(SwitchBrowserTab.class);

	public SwitchBrowserTab(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		this.model = new Gson().fromJson(jsonString, SwitchBrowserTabModel.class);
	}

	@Override
	public Date execute() {
		try {
			Thread.sleep(10000);
			// PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			tabSwitch(this.model.getswitchtab());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
			return null;
		}
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

	@Step("Switch Browser Tab {expectedString}")
	public void tabSwitch(String expectedString) throws InterruptedException {
		String url = driver.getCurrentUrl();
		((JavascriptExecutor) this.driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(this.driver.getWindowHandles());
		if (expectedString.trim().toUpperCase().equals("CHILD")) {
			this.driver.switchTo().window(tabs.get(1));
			logger.info("Driver is now on Child Window");
			Thread.sleep(5000);
			this.driver.get(url);
		} else if (expectedString.trim().toUpperCase().equals("PARENT")) {
			this.driver.switchTo().window(tabs.get(0));
			logger.info("Driver is now on Parent Window");
		} else {
			logger.info(expectedString + " Not Valid ");
		}
	}

}

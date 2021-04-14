package actions.login;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.model.BrowserNavigationModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import selenium.driver.Driver;

public class BrowserNavigation extends Driver implements Actions {
    
	private WebDriver driver;
	private Shadow shadow;
	private BrowserNavigationModel backmodel;
    final static Logger logger =Logger.getLogger(BrowserNavigation.class);

    
	public BrowserNavigation(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		shadow = new Shadow(driver);
        this.backmodel=new Gson().fromJson(jsonString, BrowserNavigationModel.class);
	}

	@Override
	public Date execute() {
	    try {
	    	//Thread.sleep(10000);
			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			browserNavigation(this.backmodel.getnavigation());
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
	@Step("Browser Button Click {expectedString}")
	public static void navigate(WebDriver driver,String expectedString) {
		if(expectedString.trim().toUpperCase().equals("BACK"))
			{
			driver.navigate().back();
			logger.info("Browser Back Button Clicked");
			}
		else if(expectedString.trim().toUpperCase().equals("FORWARD")) {
			driver.navigate().forward();
		}
		else {
			logger.info(expectedString+" Not Valid ");
		}
		}
	public void browserNavigation(String direction) {
		navigate(this.driver,this.backmodel.getnavigation());
		String currentURL = driver.getCurrentUrl();
		if(currentURL.equals("")){
			logger.info("User landed on blank Page");
		}
		else {
			logger.info("Timeout Error");
		}
	}

}
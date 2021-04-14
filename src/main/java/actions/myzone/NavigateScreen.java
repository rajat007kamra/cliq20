package actions.myzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.helpers.NavigationHelper;
import actions.model.NavigateScreenModel;
import selenium.driver.Driver;

public class NavigateScreen extends Driver implements MyZone {

	private Instant startTime;
	private Instant endTime;

	private WebDriver driver;
	private NavigateScreenModel navigateScreenModel;
	private NavigationHelper navigationHelper;
	final static Logger logger = Logger.getLogger(NavigateScreen.class);

	/**
	 *
	 * @param driver
	 * @param jsonString
	 */
	public NavigateScreen(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		navigateScreenModel = new Gson().fromJson(jsonString, NavigateScreenModel.class);
		navigationHelper = new NavigationHelper(this.driver);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Date execute() {
		try {
			startTime = Instant.now();
			if (navigateScreenModel.getRealm().trim().toUpperCase().equals(("KINETICS"))) {
				navigationHelper.clickOnRealm("realmId-Kinetics");
			} else if (navigateScreenModel.getRealm().trim().toUpperCase().equals(("PROCESSES"))) {
				navigationHelper.clickOnRealm("realmId-Processes");
			} else if (navigateScreenModel.getRealm().trim().toUpperCase().equals(("ADMIN"))) {
				navigationHelper.clickOnRealm("realmId-Admin");
			} else {
				Assert.assertTrue(false, navigateScreenModel.getRealm() + " realm not exists");
			}
			navigationHelper.searchForProcess("", navigateScreenModel.getProcess().trim().toUpperCase(), "YES");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}

		return null;
	}

	@Override
	public Date validate() {
		endTime = Instant.now();
		logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
//		PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
//		if (navigateScreenModel.getColumn() != null && !navigateScreenModel.getColumn().isEmpty()) {
//			logger.info("Filter for column");
//			navigationHelper.filter(navigateScreenModel.getColumn());
//		}
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

}

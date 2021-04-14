package actions.editzone;

import java.util.Date;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;

/**
 * @summary Handles APPROVE action
 * @author Rajat.Kamra
 */

public class Approve implements EditZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(Approve.class);
	private Shadow shadow;
	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Approve'] div.icon-label-button--disabled";
	private String approveIcon = "mz-standard-realm[active] mz-icon-label-button[text='Approve']";

	public Approve(WebDriver driver) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	@Override
	public Date execute() {
		try {
			checkIfIconIsDisabled(this.driver, "APPROVE");
			if (shadow.findElements(approveIcon).size() > 0) {
				logger.info("Save Found");
				// shadow.findElement(saveIcon).click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		checkIfIconIsDisabled(this.driver, "APPROVE");
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
	 * @summary Check if button is enabled
	 * @param driver
	 * @param button
	 * @return
	 */
	public boolean checkIfIconIsDisabled(WebDriver driver, String button) {
		boolean isActive = false;
		logger.info("Verify " + button + " icon");
		PageHelper.waitInSeconds(driver, PageHelper.X_TIMEOUT_SEC);
		try {
			if (driver.findElements(By.cssSelector(String.format(disabledIcons, button))).size() > 0) {
				isActive = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isActive;
	}

}

package actions.editzone;

import java.util.Date;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;

/**
 * @summary Handles DELETE icon
 * @author Rajat.Kamra
 *
 */

public class Delete implements EditZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(Delete.class);
	private Shadow shadow;
	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Delete'] div.icon-label-button--disabled";
	private String deleteIcon = "mz-standard-realm[active] mz-icon-label-button[text='Delete']";

	public Delete(WebDriver driver, String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	@Override
	public Date execute() {
		try {
			checkIfIconIsDisabled(this.driver, "DELETE");
			if (shadow.findElements(deleteIcon).size() > 0) {
				logger.info("Delete Found");
				// shadow.findElement(discardIcon).click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		checkIfIconIsDisabled(this.driver, "DELETE");
		return null;
	}

	@Override
	public Date setup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date tearDown() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @summary Method to verify if icon is disable or not
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

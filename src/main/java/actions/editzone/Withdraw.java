package actions.editzone;

import java.util.Date;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;

/**
 * @summary Handles Withdraw Button
 * @author Rajat.Kamra
 *
 */
public class Withdraw implements EditZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(Withdraw.class);
	private Shadow shadow;
	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Withdraw'] div.icon-label-button--disabled";
	private String withdrawIcon = "mz-standard-realm[active] mz-icon-label-button[text='Withdraw'] div.icon-label-button";

	public Withdraw(WebDriver driver, String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	@Override
	public Date execute() {
		try {
			checkIfIconIsDisabled(this.driver, "WITHDRAW");
			if (shadow.findElements(withdrawIcon).size() > 0) {
				logger.info("Withdraw Found");
				// shadow.findElement(withdrawIcon).click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		checkIfIconIsDisabled(this.driver, "WITHDRAW");
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
	 * @summary Method To verify if icon is disabled or not
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isActive;
	}

}

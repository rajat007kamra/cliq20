package actions.editzone;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.PageHelper;
import actions.model.QuickListModel;
import io.github.sukgu.Shadow;

/**
 * @summary Handles Quicklist Button
 * @author Rajat.Kamra
 *
 */
public class QuickList implements EditZone {
	private WebDriver driver;
	private QuickListModel quicklistmodel;
	final static Logger logger = Logger.getLogger(QuickList.class);
	private Shadow shadow;
	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Quicklist'] div.icon-label-button--disabled";
	private String quicklistIcon = "mz-standard-realm[active] mz-icon-label-button[text='Quicklist']";
	private String pinIcon = "mz-standard-realm[active] mz-editzone-content-swapper div.editzone.flx-ver-ctr div.buttons__pin-button mwc-icon[title='pin']";
	private String quicklistActions = "mz-standard-realm[active] mz-editzone-content-swapper mz-editzone-quicklist div.editzone-item__el span.editzone-item__text";
	private String editzoneHeader = "mz-standard-realm[active] mz-editzone-content-swapper mz-editzone-header div.editzone-header__el h3.editzone-header__text";

	public QuickList(WebDriver driver, String jsonString) {
		super();
		this.driver = driver;
		quicklistmodel = new Gson().fromJson(jsonString, QuickListModel.class);
		shadow = new Shadow(driver);
	}

	@Override
	public Date execute() {
		try {
			checkIfIconIsDisabled(this.driver, "QUICKLIST");
			WebElement element = this.shadow.findElement(quicklistIcon);
			String elemText = element.getAttribute("text");
			logger.info("TEXT FOUND ::- " + elemText);
			element.click();
			clickPinIcon();
			openTimelineFlyouts(this.quicklistmodel.getFlyout());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			validateEditZone(this.quicklistmodel.getFlyout());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}
		return isActive;
	}

	/**
	 * @summary Method to pin Edit Zone
	 */
	public void clickPinIcon() {
		if (shadow.findElements(pinIcon).size() > 0) {
			shadow.findElement(pinIcon).click();
			logger.info("Pin icon clicked");
		} else {
			Assert.assertFalse(true, "Pin Icon not found");
		}
	}

	/**
	 * @summary Method to open widget flyout in editzone
	 * @param flyoutName
	 */
	public void openTimelineFlyouts(String flyoutName) {
		try {
			List<WebElement> timelineList = null;
			timelineList = shadow.findElements(quicklistActions);
			for (int i = 0; i < timelineList.size(); i++) {
				String caption = timelineList.get(i).getText();
				logger.info(caption);
				if (caption.trim().toUpperCase().equals(flyoutName)) {
					timelineList.get(i).click();
					logger.info(flyoutName + " found and clicked");
					break;
				}
			}
		} catch (ElementNotVisibleException e) {
			Assert.assertFalse(true, flyoutName + " Not Exists");
		}
	}

	/**
	 * @summary validate Edit Zone Flyouts
	 * @param flyoutName
	 * @throws InterruptedException
	 */
	public void validateEditZone(String flyoutName) throws InterruptedException {
		Thread.sleep(5000);
		WebElement header = shadow.findElement(editzoneHeader);
		String headerText = header.getText();
		logger.info("Header found for opened editzone " + flyoutName + " is - " + headerText);
		if (flyoutName.trim().toUpperCase().equals(headerText.trim().toUpperCase())) {
			logger.info("Flyout opened successfully");
		} else {
			Assert.assertTrue(false, flyoutName + "Not Exists");
		}
	}

}

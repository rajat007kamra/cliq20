package actions.helpers;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 *
 * @summary This class is for navigate to process
 * @author Arun.Kapoor
 * @Date 26-10-2020
 *
 */

public class NavigationHelper {
	private WebDriver driver;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(NavigationHelper.class);

	private String realmLocator = "mz-icon-label-button[id='%1s'] div.icon-label-button";
	private String disabledRealmLocator = "mz-icon-label-button[id='%1s'] div.icon-label-button--disabled";
	private String activeRealmLocator = "mz-icon-label-button[id='%1s'] div.icon-label-button--selected";
	private String expandMenu = "mz-standard-realm[active] div.hght-wdth-100pct mz-my-zone";
	private String searchFieldLocator = "mz-standard-realm[active] mz-textfield[label='SEARCH PROCESSES'] input.mdc-text-field__input";
	private String suggestionPopupLocator = "mwc-list mwc-list-item span.realm-search-result__first-line span";
	private String activeTabName = "mz-viewzone-tab[display='%1s'][active]";

	public NavigationHelper(WebDriver driver) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	/**
	 * @Summary This method checks whether the realm(Admin, Processes, Settings) already selected or not
	 * @param realm
	 */
	@Step("Check if realm is already selected")
	public boolean checkIfRealmIsAlreadySelected(String realm) {
		boolean isRealmActive = false;
		try {
			WebElement activeRealmElem = this.driver
					.findElement(By.cssSelector(String.format(activeRealmLocator, realm)));
			PageHelper.waitForPageLoad(driver);
			isRealmActive = PageHelper.isElementDisplayed(activeRealmElem);
		} catch (Exception e) {
			// Absorb the error if any occurs here
		}
		return isRealmActive;
	}

	/**
	 * @Summary This method checks whether the realm(Admin, Processes, Settings) is disabled or enabled
	 * @param realm
	 */
	@Step("Check is realm is disabled")
	public boolean checkIfRealmIsDisabled(String realm) {
		boolean isRealmActive = false;
		try {
			logger.info("Verify " + realm + " realm");
			WebElement activeRealmElem = this.driver
					.findElement(By.cssSelector(String.format(disabledRealmLocator, realm)));
			PageHelper.waitForPageLoad(driver);
			isRealmActive = PageHelper.isElementDisplayed(activeRealmElem);
		} catch (Exception e) {
			// Absorb the error if any occurs here
		}
		return isRealmActive;
	}

	/**
	 * @Summary This method clicks realm
	 * @param realm
	 * @throws InterruptedException
	 */
	@Step("Click in realm {realm}")
	public void clickOnRealm(String realm) throws InterruptedException {
		if (!checkIfRealmIsDisabled(realm)) {
			logger.info(realm + " realm is enabled");
			logger.info("Click on realm " + realm);
			WebElement realmElement = this.shadow.findElement(String.format(realmLocator, realm));
			if (!checkIfRealmIsAlreadySelected(realm)) {
				PageHelper.click(this.shadow, realmElement);
			}
			logger.info("Verify if user is navigated to realm " + realm);
//			verifyNavigationOnRealm(realm);
		} else {
			String errorMsg = realm + " realm is disabled but expected enabled";
			logger.error(errorMsg);
			Assert.fail(errorMsg);
		}
	}

	/**
	 * @Summary This method checks whether the realm(Admin, Processes, Settings) already selected or not
	 * @param realmName
	 */
	@Step("Verify if user is navigated on realm {realmName}")
	public void verifyNavigationOnRealm(String realmName) {
//		PageHelper.waitForElementVisibility(this.driver, By.cssSelector(String.format(activeRealmLocator, realmName)), PageHelper.DEFAULT_TIMEOUT_SEC);
		PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
		WebElement activeRealmElem = this.driver
				.findElement(By.cssSelector(String.format(activeRealmLocator, realmName)));
		PageHelper.waitForElementToBeDisplayed(shadow, activeRealmElem);
		Assert.assertTrue(PageHelper.isElementDisplayed(activeRealmElem), "Not able to navigate to realm");
	}

	/**
	 * @Summary This method searching for process
	 * @param processName
	 * @throws InterruptedException
	 */
	@Step("Search for the process {processName}")
	public void searchForProcess(String realm, String processName, String access) throws InterruptedException {
		mouseHover("HOVER");
		WebElement searchFieldElem = this.shadow.findElement(searchFieldLocator);
		searchFieldElem.sendKeys(processName);
		if (access.equals("YES")) {
			if (this.shadow.findElements(suggestionPopupLocator).size() > 0) {
				logger.info(processName + " exists");
				clickOnProcessInSearchList(realm, processName);
				logger.info("Clicked on searched process " + processName);
//				mouseHover(realm, "RELEASE");
				logger.info("Verify if user is navigated to process " + processName);
				mouseHover("RELEASE");
				Thread.sleep(5000);
				verifyNavigationOnProcess(processName);
				Thread.sleep(5000);
			} else {
				String errorMsg = processName + " process does not exists while access value is " + access;
				logger.info(errorMsg);
				Assert.fail(errorMsg);
			}
		} else if (access.equals("NO")) {
			if (this.shadow.findElements(suggestionPopupLocator).size() > 0) {
				String errorMsg = processName + " process exists while access value is " + access;
				logger.info(errorMsg);
				Assert.fail(errorMsg);
			} else {
				logger.info(processName + " does not exists");
			}
		} else {
			String errorMsg = "Access process value is not valid i.e. it could be only YES/NO";
			logger.info(errorMsg);
			Assert.fail(errorMsg);
		}
	}

	/**
	 * @Summary This method clicking on searched process
	 * @param realmName
	 * @param processName
	 */
	@Step("Click on process in search list under realm {realmName} and process name {processName}")
	public void clickOnProcessInSearchList(String realmName, String processName) {
		List<WebElement> suggestions = this.shadow.findElements(suggestionPopupLocator);
		for (WebElement suggestion : suggestions) {
			PageHelper.waitForElementToBeDisplayed(shadow, suggestion);
			String suggestionText = PageHelper.getText(shadow, suggestion);
			if (suggestionText.equals(processName)) {
				PageHelper.click(shadow, suggestion);
			} else {
				Assert.assertTrue(false, processName + " not exists");
			}
		}
	}

	/**
	 * @Summary This method verifies that searched process opened or not
	 * @param processName
	 * @throws InterruptedException
	 */
	@Step("Verify if user is navigated on process {processName}")
	public void verifyNavigationOnProcess(String processName) throws InterruptedException {
		PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
		WebElement userProfileElem = this.shadow.findElement(String.format(activeTabName, processName.toUpperCase()));
		if (PageHelper.isElementDisplayed(userProfileElem)) {
			logger.info(processName + " opened successfully");
		} else {
			Assert.assertTrue(false, processName + " either not opened or not verified");
		}
	}

	/**
	 * @Summary This method is to hover mouse on left accordion for searching process
	 * @throws InterruptedException
	 */
	// Method to hover on REALMS
	public void mouseHover(String option) throws InterruptedException {
		Thread.sleep(5000);
		WebElement myZone = this.shadow.findElement(expandMenu);
		Actions tooltip = new Actions(driver);
		if (option.equals("HOVER")) {
			tooltip.moveToElement(myZone).build().perform();
			if (myZone.getAttribute("class").contains("expanded")) {
				logger.info("My zone expand successfully");
			} else {
				Assert.assertTrue(false, "My zone not expanded for search process");
			}
		} else if (option.equals("RELEASE")) {
			PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
			this.driver.findElement(By.cssSelector("body")).click();
		} else {
			Assert.assertTrue(false, "Option could be only HOVER/RELEASE");
		}
	}
}

package actions.viewzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.google.gson.Gson;
import actions.Actions;
import actions.PageHelper;
import actions.model.GlobalSearchModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import selenium.driver.Driver;

public class GlobalSearch extends Driver implements Actions {
	private Instant startTime;
	private Instant endTime;
	private Shadow shadow;
	private GlobalSearchModel globalSearchModel;
	final static Logger logger = Logger.getLogger(GlobalSearch.class);
	
	public GlobalSearch(WebDriver driver, String jsonString) {
		super(driver);
		this.shadow = new Shadow(driver);
		globalSearchModel = new Gson().fromJson(jsonString, GlobalSearchModel.class);
		shadow = new Shadow(driver);
	}

	private String globalSearch = "div.search-box input.mdc-text-field__input";
	private String suggestPopUpList = "div.gbl-srch-div.no-srch";
	private String suggestedlist = "mwc-list#menu-list span.search-highlight";
	
	@Override
	public Date execute() {
		try {
			startTime = Instant.now();
			enterInSearchTextbox(this.globalSearchModel.getGlobalSearch().trim().toUpperCase());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			endTime = Instant.now();
			isSuggestListOpen(this.globalSearchModel.getGlobalSearch().trim().toUpperCase());
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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

	@Step("Enter search text")
	private void enterInSearchTextbox(String searchValue) throws InterruptedException {
		this.shadow.findElement(globalSearch).click();
		Thread.sleep(10000);
		this.shadow.findElement(globalSearch).sendKeys(searchValue);
		logger.info("Enter " +searchValue+ " text in global search textbox");
	}
	
	@Step("Verify suggestion list")
	private void isSuggestListOpen(String searchValue) throws InterruptedException {
		Thread.sleep(5000);
		logger.info("Check if suggestion list present");
		if (this.shadow.findElements(suggestedlist).size()>0) 
		{
			logger.info("Found some similar match");
			List<WebElement> suggestions = this.shadow.findElements(suggestedlist);
			for (WebElement suggestion : suggestions) {
				PageHelper.waitForElementToBeDisplayed(shadow, suggestion);
				String suggestionText = PageHelper.getText(shadow, suggestion);
				logger.info(suggestionText);
				if (suggestionText.equals(searchValue)) {
					PageHelper.click(shadow, suggestion);
					logger.info("Searched text exists in suggestion list");
				} else {
					Assert.assertTrue(false, searchValue + " not exists");
				}
			}
		}
		else if(this.shadow.findElements(suggestPopUpList).size()>0)
		{
			logger.info("No Records Found");
		}
		else {
			logger.error("No recent searches for " +searchValue);
			Assert.fail("No recent searches for " +searchValue);
		}
	}
}

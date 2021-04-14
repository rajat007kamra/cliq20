package actions.viewzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import actions.PageHelper;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * 
 * @summary This is a ClearFilter action class and responsible to click on ClearFilter icon and validate same 
 * @author Arun.Kapoor
 * @Date 14-02-2021
 *
 */

public class ClearFilter implements ViewZone {
	private Instant startTime;
	private Instant endTime;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(ClearFilter.class);
	
	public ClearFilter(WebDriver driver,String jsonString) {
//		super(driver);
		shadow = new Shadow(driver);
	}
	
	private String filteredByText = "mz-viewzone[active] mz-viewzone-stats div.vz-grid-stats div.grid-stats__filterby";
	private String clearFilter = "mz-viewzone[active] div.vz-grid-stats__clearall span.content-link";

	@Override
	public Date execute() {
		try {
			startTime = Instant.now();
			clickClearFilter();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
			return null;
		}
	}

	@Override
	public Date validate() {
		try {
			TimeUnit.SECONDS.sleep(3);
			validateClearFilter();
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
			return null;
		}
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
	 * @summary This method is responsible to click on CLEAR FILTER icon
 	 * @return
	 */
	@Step("Click CLEAR FILTER icon")
	private void clickClearFilter() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		WebElement filter = shadow.findElement(clearFilter);
		PageHelper.click(shadow, filter);
		logger.info("Clear filter icon is active and clicked");
	}
	
	/**
	 * @summary This method is responsible to validate whether CLEAR FILTER working fine or not  
	 */
	@Step("Validate CLEAR FILTER icon")
	private void validateClearFilter() throws InterruptedException {
		if(shadow.findElements(filteredByText).size()>0)
		{
			logger.info("Clear filter either not working or 'Filtered By' text not dissappeared");
		}
		else
		{
			logger.info("Clear filter icon become inactive");
		}
	}
}

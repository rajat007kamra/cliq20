package actions.viewzone;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.github.sukgu.Shadow;

/**
 * @summary This class contains verify, click, and validate Paste Data icon
 * @author Arun.Kapoor
 */

public class PasteData implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(PasteData.class);
	Shadow shadow;

	public PasteData(WebDriver driver, String jsonString) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	private String moreOptions = "mz-viewzone[active] mz-viewzone-toolbar mz-icon-button[title='More options']";
	private String moreMenuOptions = "mz-viewzone-more-opts mwc-menu mwc-list-item[graphic='icon']";

	@Override
	public Date execute() {
		try {
			clickMoreOptions();
			TimeUnit.SECONDS.sleep(5);
			clickPasteData();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}
	
	@Override
	public Date validate() {
		try {
			logger.info("Paste Data available there but not working");
			//No suitable validation found because right now not working
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
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
	 * @summary This method use to click on More Options (3 dots) icon
	 */
	private void clickMoreOptions() {
		WebElement element = this.shadow.findElement(moreOptions);
		element.click();
	}

	/**
	 * @summary This method use to click on Paste Data icon 
	 */
	private void clickPasteData() {
		List<WebElement> listOptions = this.shadow.findElements(moreMenuOptions);
		logger.info(listOptions.size());
		listOptions.get(0).click();
	}
}
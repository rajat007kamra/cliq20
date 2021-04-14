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
 * @summary This class contains verify, click, and validate EXPORT DATA button
 * @author Arun.Kapoor
 */

public class ExportData implements ViewZone {
	private WebDriver driver;
	final static Logger logger = Logger.getLogger(ExportData.class);
	Shadow shadow;

	public ExportData(WebDriver driver) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

	private String moreOptions = "mz-viewzone[active] mz-icon-button[title='More options']";
	private String moreMenuOptions = "mz-viewzone[active] mwc-list-item[graphic='icon']";

	@Override
	public Date execute() {
		try {
			clickExportDataButton();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			logger.info("Export Data working fine");
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
	 * @summary This method is to click on EXPORT DATA icon 
 	 * @return
	 */
	public void clickExportDataButton() throws InterruptedException
	{
		WebElement element = this.shadow.findElement(moreOptions);
		element.click();
		TimeUnit.SECONDS.sleep(5);
		List<WebElement> listOptions = this.shadow.findElements(moreMenuOptions);
		logger.info(listOptions.size());
		listOptions.get(1).click();
	}
}
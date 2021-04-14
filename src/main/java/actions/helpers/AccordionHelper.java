package actions.helpers;

import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import actions.PageHelper;
import io.github.sukgu.Shadow;

public class AccordionHelper {
	private WebDriver driver;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(AccordionHelper.class);

	private String leftMenuExpand = "mz-standard-realm[active] div.hght-wdth-100pct mz-my-zone";
	private String rightMenuExpand = "mz-standard-realm[active] mz-editzone-content-swapper div.editzone.flx-ver-ctr";

	public AccordionHelper(WebDriver driver) {
		this.driver = driver;
		shadow = new Shadow(driver);
	}

    //Method to expand the Accordion using Mouse Hover 
	public void changeAccordionState(String option) throws InterruptedException {
		Thread.sleep(5000);
		WebElement myZone = null;
		if(option.equals("LEFT"))
		{
			myZone = this.shadow.findElement(leftMenuExpand);
		}
		else if(option.equals("RIGHT"))
		{
			myZone = this.shadow.findElement(rightMenuExpand);
		}
		else {
			Assert.assertTrue(false, "Accordion value must be LEFT/RIGHT");
		}		
		Actions tooltip = new Actions(driver);
		tooltip.moveToElement(myZone).build().perform();
		if (myZone.getAttribute("class").contains("expanded")) {
			logger.info(option +" accordion expanded successfully");
			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			this.driver.findElement(By.cssSelector("body")).click();
		} else {
			Assert.assertTrue(false, option +" accordion not expanded. Please check");
		}
	}
}
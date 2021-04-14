package actions.myzone;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.google.gson.Gson;

import actions.Actions;
import actions.helpers.AccordionHelper;
import actions.model.AccordionModel;
import selenium.driver.Driver;

public class Accordion extends Driver implements Actions {
	private WebDriver driver;
	private AccordionModel accordionModel;
	private AccordionHelper accordionHelper;
	final static Logger logger = Logger.getLogger(Accordion.class);

	/**
	 *
	 * @param driver
	 * @param jsonString
	 */
	public Accordion(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		accordionModel = new Gson().fromJson(jsonString, AccordionModel.class);
		accordionHelper = new AccordionHelper(this.driver);
	}

	@Override
	public Date execute() {
		try {
//			PageHelper.waitInSeconds(this.driver, PageHelper.XX_TIMEOUT_SEC);
			Thread.sleep(10000);
			String state = this.accordionModel.getAccordion().trim().toUpperCase();
			accordionHelper.changeAccordionState(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Date validate() {
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
}
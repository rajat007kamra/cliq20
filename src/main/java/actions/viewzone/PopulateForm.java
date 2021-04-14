package actions.viewzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.helpers.PopulateFormHelper;
import actions.model.Column;
import actions.model.PopulateFormModel;
import selenium.driver.Driver;

public class PopulateForm extends Driver implements Actions {
	private Instant startTime;
	private Instant endTime;
	private WebDriver driver;
	private PopulateFormHelper updateFormHelper;
	private PopulateFormModel updateFormModel;

	final static Logger logger = Logger.getLogger(PopulateForm.class);
	private String saveIcon="body mz-dialog mz-form-renderer div.form-container form-container-renderer form-container-footer mz-form-footer div.form-workflow-container div.form-renderer-events div.icon-label-btn[title='Save'] mz-button";

	public PopulateForm(WebDriver driver, String jsonString) {
		super(driver);
		this.driver = driver;
		this.updateFormHelper = new PopulateFormHelper(this.driver);
		this.updateFormModel = new Gson().fromJson(jsonString, PopulateFormModel.class);
	}

	public PopulateForm(WebDriver driver, List<Column> columns) {
		super(driver);
		this.driver = driver;
		this.updateFormHelper = new PopulateFormHelper(this.driver);
		this.updateFormModel = new PopulateFormModel();
		this.updateFormModel.setColumn(columns);
	}

	@Override
	public Date execute() {
		try {
			TimeUnit.SECONDS.sleep(2);
			List<Column> columns = this.updateFormModel.getColumn();
			startTime = Instant.now();
			for (Column column : columns) {
				logger.info("Fetch field number in table for given field " + column.getVariable());
				int columnNumber = updateFormHelper.getFieldNumber(column.getVariable());
				if (columnNumber >= 0) {
					logger.info("Set text " + PageHelper.appendHashCode(this.driver, column.getValue().trim()) + " in field number " + columnNumber);
					updateFormHelper.setFieldValue(column.getVariable(), PageHelper.appendHashCode(this.driver, column.getValue().trim()), columnNumber);
					// Small static wait required to wait for page load initialization
					logger.warn("Static wait introduced");
					PageHelper.waitInSeconds(this.driver, PageHelper.X_TIMEOUT_SEC);
					logger.info("Wait for page to load");
					PageHelper.waitForPageLoad(this.driver);
					
				} else {
					logger.error(column.getVariable() + " does not exists");
				}
				
			}
			if(driver.findElements(By.cssSelector(saveIcon)).size()>0)
			{
			    logger.info("Save Found");
			    driver.findElement(By.cssSelector(saveIcon)).click();
			}
			else {
				logger.info("Save Not Found");
			}
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public Date validate() {
		endTime = Instant.now();
		logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
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

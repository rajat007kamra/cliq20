package actions.viewzone;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.model.Column;
import actions.model.FilterModel;
import io.qameta.allure.Step;
import junit.framework.Assert;
import selenium.driver.Driver;
import io.github.sukgu.Shadow;

/**
 * @summary This class for filter data in viewzone
 * @author Arun.Kapoor
 */

public class Filter extends Driver implements Actions {
	private Instant startTime;
	private Instant endTime;
	private FilterModel filterModel;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(FilterModel.class);

	public Filter(WebDriver driver, String jsonString) {
		super(driver);
		this.shadow = new Shadow(driver);
		this.filterModel = new Gson().fromJson(jsonString, FilterModel.class);
	}

	private String columnsHeaderLocator = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-header.ag-focus-managed.ag-pivot-off div.ag-header-viewport div.ag-header-container div.ag-header-row div.ag-header-cell div.ag-cell-label-container div.ag-header-cell-label span.ag-header-cell-text";
	private String columnHeaderFilterIcon = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-header.ag-focus-managed.ag-pivot-off span.ag-header-icon.ag-header-cell-menu-button mwc-icon";
	private String filtertext = "div.ag-filter-body[ref='eCondition1Body'] input.ag-input-field-input.ag-text-field-input";
	public static String filteredByText = "mz-viewzone[active] mz-viewzone-stats div.vz-grid-stats div.grid-stats__filterby";
	private String tableRows = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-pinned-left-cols-container[ref='eLeftContainer'] div.ag-row.ag-row-level-0";

	@Override
	public Date execute() {
		try {
			int columnNumber = 0;
			startTime = Instant.now();
			if (this.filterModel.getColumn() != null && !this.filterModel.getColumn().isEmpty()) {
				List<Column> columns = this.filterModel.getColumn();
				for (Column column : columns) {
					TimeUnit.SECONDS.sleep(3);
					if (column.getTitle() != null && !column.getTitle().isEmpty()) {
						logger.info("Fetch column number in table for given column "
								+ column.getTitle().trim().toUpperCase());
						columnNumber = getColumnNumber(column.getTitle().toUpperCase());
						if (columnNumber >= 0) {
							clickFilter(columnNumber);
							logger.info("Filter icon clicked successfully for column " + column.getTitle());
							if (column.getText() != null && !column.getText().isEmpty()) {
								WebElement mo = this.shadow.findElement(filtertext);
								mo.sendKeys(column.getText());
								TimeUnit.SECONDS.sleep(5);
								this.shadow.findElement("body").click();
							}
						} else {
							Assert.assertTrue(column.getTitle() + " column does not exists", false);
						}
					} else {
						Assert.assertTrue("Column name is mandatory ", false);
					}
					if (column.getCondition() != null && !column.getCondition().isEmpty()) {
						logger.info("CONDITION");
					}

				}
			}
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage(), false);
			return null;
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			TimeUnit.SECONDS.sleep(5);
			validateFilter();
			selectViewZoneRow();
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e.getMessage(), false);
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
	 * @summary This method find the index of column name present in viewzone   
	 * @param colname
 	 * @return counter
	 */
	@Step("Fetch the column number of column {colname}")
	public Integer getColumnNumber(String colname) throws InterruptedException {
		int counter = -1;
		List<WebElement> columnHeaders = shadow.findElements(columnsHeaderLocator);
		for (int i = 0; i < columnHeaders.size(); i++) {
			String columnName = PageHelper.getText(shadow, columnHeaders.get(i));
			if ((columnName.trim()).equals(colname)) {
				counter = i;
				break;
			}
		}
		return counter;
	}

	/**
	 * @summary This method click filter icon given with column name   
	 * @param index
 	 * @return
	 */
	@Step("Set order on column")
	public void clickFilter(int index) throws InterruptedException {
		int i;
		List<WebElement> filtersList = shadow.findElements(columnHeaderFilterIcon);
		for (i = 0; i < filtersList.size(); i++) {
			if (i == index) {
				PageHelper.click(this.shadow, filtersList.get(i));
				break;
			}
		}
	}

	/**
	 * @summary This method use to select row number after filteration  
	 * @param 
 	 * @return
	 */
	public void selectViewZoneRow() throws InterruptedException {
		Thread.sleep(5000);
		if (this.filterModel.getSelectRow() != null && !this.filterModel.getSelectRow().isEmpty()) {
			// Small static wait required to wait for page load initialization
			logger.warn("Static wait introduced");
			Thread.sleep(5000);
			logger.info("Wait for page to load");
//			PageHelper.waitForPageLoad(this.driver);
			selectRow(this.filterModel.getSelectRow());
			logger.info(this.filterModel.getSelectRow() + " row selected successfully");
		}
	}

	/**
	 * @summary This method select row number after filtration
	 * @param
 	 * @return
	 */
	@Step("Select row number {rowNumber}")
	public void selectRow(String rowNumber) throws InterruptedException {
		List<WebElement> tableRowElements = shadow.findElements(tableRows);
		for (int i = 0; i < tableRowElements.size(); i++) {
			int rowNum = Integer.parseInt(rowNumber) - 1;
			if (i == rowNum) {
				TimeUnit.SECONDS.sleep(5);
				WebElement rowNuberElement = tableRowElements.get(rowNum);
				PageHelper.clickUsingJs(shadow, rowNuberElement);
			}
		}
	}

	/**
	 * @summary This method validate whether filter working fine or not  
	 * @param
 	 * @return
	 */
	@Step("Validate FILTER")
	private void validateFilter() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		WebElement SubTenant = shadow.findElement(filteredByText);
		Assert.assertEquals(PageHelper.isElementDisplayed(SubTenant), true);
		logger.info("Rows filtered successfully");
	}
}
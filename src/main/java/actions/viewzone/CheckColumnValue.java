package actions.viewzone;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.google.gson.Gson;

import actions.Actions;
import actions.PageHelper;
import actions.model.Column;
import actions.model.FormViewModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import selenium.driver.Driver;

/**
 *
 * @summary This class verifying the Column value of viewzone table 
 * @author Arun.Kapoor
 * @Date 15-02-2021
 *
 */

public class CheckColumnValue extends Driver implements Actions {
	private WebDriver driver;
	private FormViewModel checkViewzoneModel;
	private Shadow shadow;

	final static Logger logger = Logger.getLogger(CheckColumnValue.class);

	public CheckColumnValue(WebDriver driver, String jsonString) {
		super(driver);
		this.shadow = new Shadow(driver);
		this.checkViewzoneModel = new Gson().fromJson(jsonString, FormViewModel.class);
	}

	public String tableRows = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-pinned-left-cols-container[ref='eLeftContainer'] div.ag-row.ag-row-level-0[row-index='%s']";
	private String columnsHeaderLocator = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-header.ag-focus-managed.ag-pivot-off div.ag-header-viewport div.ag-header-container div.ag-header-row div.ag-header-cell div.ag-cell-label-container div.ag-header-cell-label span.ag-header-cell-text";
	private String columnsValueLocator = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-body-viewport div.ag-center-cols-clipper div.ag-center-cols-viewport div.ag-center-cols-container div.ag-row.ag-row-focus.ag-row-selected div.ag-cell-value span";

	@Override
	public Date execute() {
		try {
			int columnNumber = 0;
			if (this.checkViewzoneModel.getColumn() != null && !this.checkViewzoneModel.getColumn().isEmpty()) {
				List<Column> columns = this.checkViewzoneModel.getColumn();
				for (Column column : columns) {
					TimeUnit.SECONDS.sleep(2);
					if (column.getTitle() != null && !column.getText().isEmpty()) {
						logger.info("Fetch column number in table for given column "+ column.getTitle().trim().toUpperCase());
						columnNumber = getColumnNumber(column.getTitle().toUpperCase());
						logger.info(column.getTitle().trim().toUpperCase() +" column location is " +columnNumber);
						if (columnNumber >= 0) {
							if(this.checkViewzoneModel.getSelectRow()!=null && !this.checkViewzoneModel.getSelectRow().isEmpty())
							{
								selectRow(this.checkViewzoneModel.getSelectRow());
								getColumnValue(columnNumber, column.getTitle(), column.getText().trim().toUpperCase());
							}
							else {
								selectRow("1");
								getColumnValue(columnNumber, column.getTitle(), column.getText().trim().toUpperCase());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
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

	/**
	 * @summary This method finding the index of Column name given by user in viewzone table 
	 * @param colname i.e. Column Name
 	 * @return counter i.e. index value of column name
	 */
	@Step("Fetch the column number of column {colname}")
	public Integer getColumnNumber(String colname) throws InterruptedException {
		int counter = -1;
		TimeUnit.SECONDS.sleep(5);
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
	 * @summary This method fetching the value against column name entered by user 
	 * @param index i.e. Column Index find by getColumnNumber method
	 * @param columnName, columnValue given by user
 	 * @return colValue i.e. fetched column value from viewzone table
	 */
	@Step("Fetch the value of variable {text}")
	public String getColumnValue(int index, String columnName, String columnValue) throws InterruptedException {
		int i;
		String colValue = null;
		List<WebElement> columnValues = shadow.findElements(columnsValueLocator);
		for (i = 0; i < columnValues.size(); i++) {
			if (i == index) {
				TimeUnit.SECONDS.sleep(2);
				colValue = columnValues.get(i).getText();
				logger.info("Value of " +columnName +" found - " +colValue);
				Assert.assertEquals(columnValue, colValue.trim().toUpperCase());
				break;
			}
		}
		return colValue;
	}

	/**
	 * @summary This method selecting record number given by user
	 * @param rowNumber
	 */
	@Step("Select row number {rowNumber}")
	public void selectRow(String rowNumber) {
		int rowNum = Integer.parseInt(rowNumber) - 1;
		WebElement tableRowElement = shadow.findElement(String.format(tableRows, rowNum));
		PageHelper.click(shadow, tableRowElement);
		logger.info("Row number - " +rowNumber +" selected successfully");
	}
}
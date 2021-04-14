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

import actions.PageHelper;
import actions.model.SortColumnModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;
import junit.framework.Assert;

/**
 * @summary This class is responsible for columns sorting
 * @author Arun.Kapoor
 */

public class SortColumn implements ViewZone {
	private Instant startTime;
	private Instant endTime;
	private Shadow shadow;
	private SortColumnModel sortColumnModel;
	final static Logger logger = Logger.getLogger(SortColumn.class);

	public SortColumn(WebDriver driver, String jsonString) {
//		super(driver);
		this.shadow = new Shadow(driver);
		sortColumnModel = new Gson().fromJson(jsonString, SortColumnModel.class);
		shadow = new Shadow(driver);
	}

	private String columnsHeaderLocator = "mz-standard-realm[active] mz-viewzone[active] mz-grid div.ag-header.ag-focus-managed.ag-pivot-off div.ag-header-viewport div.ag-header-container div.ag-header-row div.ag-header-cell div.ag-cell-label-container div.ag-header-cell-label span.ag-header-cell-text";

	@Override
	public Date execute() {
		try {
			TimeUnit.SECONDS.sleep(5);
			startTime = Instant.now();
			setSorting(this.sortColumnModel.getSortOrder());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Column Name not found", false);
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e.getMessage(), false);
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
	 * @summary This method fetch column index first and then perform sorting
	 */
	@Step("Fetch the column number of column {filterColumnName}")
	public void setSorting(String sortType) {
		List<WebElement> selectorElements = this.shadow.findElements(columnsHeaderLocator);
		for (int i = 0; i < selectorElements.size(); i++) {
			String columnName = PageHelper.getText(shadow, selectorElements.get(i));
			logger.info(columnName);
			String updatedColumnName = columnName.replace("*", "");
			if ((updatedColumnName.trim()).equals(this.sortColumnModel.getColumnName().trim())) {
				if (sortType.equals("ASCENDING")) {
					selectorElements.get(i).click();
				} else if (sortType.equals("ASCENDING")) {
					selectorElements.get(i).click();
				} else {
					Assert.assertTrue(sortType + " is not Valid ", false);
				}
				break;
			}
		}
	}
}
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
import actions.model.ListsModel;
import io.github.sukgu.Shadow;

public class Lists implements ViewZone {
	private WebDriver driver;
	private Instant startTime;
	private Instant endTime;
	private ListsModel listsModel;
	private Shadow shadow;
	final static Logger logger = Logger.getLogger(Lists.class);

	public Lists(WebDriver driver, String jsonString) {
		super();
		shadow = new Shadow(this.driver);
		this.listsModel = new Gson().fromJson(jsonString, ListsModel.class);
	}

	private String regularGroupButtons = "mz-lists-builder div.body div.grid-container div.control_widget div.regular-grouped label.btn";
	private String findStaticListValue = "mz-lists-builder div.body div.widget-list div[id='mz-list-grid-row'] div.ag-root div.ag-body-viewport div.ag-row div.ag-cell-value";

	@Override
	public Date execute() {
		try {
			Thread.sleep(5000);
			startTime = Instant.now();
			clickButton();
			List<String> values = this.listsModel.getOptions();
			for (String value : values) {
				logger.info("Check " + value + " in list");
				findValueInStaticList(value);
			}
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
			endTime = Instant.now();
			logger.info("[RESPTIME] " + Duration.between(startTime, endTime).toMillis());
			return null;
		} catch (Exception e) {
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

	// Click Regular button
	private void clickButton() {
		List<WebElement> buttons = shadow.findElements(regularGroupButtons);
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(0).click();
		}
	}

	// Method to find value in static list
	private void findValueInStaticList(String value) {
		System.out.println("abc");
		List<WebElement> staticValues = shadow.findElements(findStaticListValue);
		for (int i = 0; i < staticValues.size(); i++) {
			String caption = staticValues.get(i).getText();
			if (caption.trim().toUpperCase().equals(value)) {
				logger.info(value + " exists in list");
				break;
			} else {
				Assert.assertTrue(false, value + " not found or not exists");
			}
		}
	}
}

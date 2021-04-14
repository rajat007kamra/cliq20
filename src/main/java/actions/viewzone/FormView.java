package actions.viewzone;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.google.gson.Gson;
import actions.Actions;
import actions.model.Column;
import actions.model.FormViewModel;
import io.github.sukgu.Shadow;
import selenium.driver.Driver;

public class FormView extends Driver implements Actions {
	private WebDriver driver;
	private FormViewModel updateFormModel;
	private Shadow shadow;

	final static Logger logger = Logger.getLogger(FormView.class);

	public FormView(WebDriver driver, String jsonString) {
		super(driver);
		shadow = new Shadow(this.driver);
		this.updateFormModel = new Gson().fromJson(jsonString, FormViewModel.class);
	}

	public FormView(WebDriver driver, List<Column> columns) {
		super(driver);
		shadow = new Shadow(this.driver);
		this.updateFormModel = new FormViewModel();
		this.updateFormModel.setColumn(columns);
	}

	@Override
	public Date execute() {
		List<Column> columns = this.updateFormModel.getColumn();
		for (Column column : columns) {
			try {
				String input = column.getVariable();
				FormViewTemplate formView = new FormViewTemplate(input);
				Map<String, String> xpathMap = formView.counstructXpath("put", column.getValue(), column.getVariable());

			} catch (Exception e) {
				e.printStackTrace();
			}
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
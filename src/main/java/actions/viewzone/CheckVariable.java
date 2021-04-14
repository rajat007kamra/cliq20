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

/**
 * 
 * @summary This is a CheckVariable class and responsible to check the populated value of variable 
 * @author Arun.Kapoor
 * @Date 14-02-2021
 *
 */

public class CheckVariable extends Driver implements Actions {
	private WebDriver driver;
	private FormViewModel checkModel;
	private Shadow shadow;

	final static Logger logger = Logger.getLogger(FormView.class);

	public CheckVariable(WebDriver driver, String jsonString) {
		super(driver);
		shadow = new Shadow(this.driver);
		this.checkModel = new Gson().fromJson(jsonString, FormViewModel.class);
	}

	public CheckVariable(WebDriver driver, List<Column> columns) {
		super(driver);
		shadow = new Shadow(this.driver);
		this.checkModel = new FormViewModel();
		this.checkModel.setColumn(columns);
	}

	@Override
	public Date execute() {
		List<Column> columns = this.checkModel.getColumn();
		for (Column column : columns) {
			try {
				String input = column.getVariable();
				FormViewTemplate formView = new FormViewTemplate(input);
				Map<String, String> xpathMap = formView.counstructXpath("get", column.getValue(), column.getVariable());
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
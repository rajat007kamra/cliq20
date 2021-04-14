package actions.mappers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.google.gson.Gson;
import actions.model.Column;
import actions.model.FormViewModel;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;
import core.cliqdb.model.TestcaseActionJsonData;

public class InputProfileMapper implements Mapper {

	static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String alphaNumericCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String numericCharacters = "0123456789";

	@Override
	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext) {
		sectionData.getName();
		List<String> actionList = new ArrayList<String>();
		List<FormViewModel> contextList = new ArrayList<FormViewModel>();
		List<Map<String, String>> context = sectionData.getSectionContext();
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("POPULATE-FORM");
		actionList.add(new Gson().toJson(openFormView()));
		stepContext.setFormOpen(true);
		FormViewModel verticalFlyModel = new FormViewModel();

		List<Column> lists = new ArrayList<Column>();
		for (Map<String, String> map : context) {
			Column column = new Column();
			// column.setVariable(sectionData.getSimpleName().get(map.get("VARIABLE")) +
			// "###" + map.get("UI FIELD"));
			column.setVariable(map.get("UI FIELD"));
			String value = map.get("VALUE");
			if (value.toUpperCase().endsWith("[ALPHA]") | value.toUpperCase().endsWith("[ ALPHA ]")
					| value.toUpperCase().endsWith("[ALPHA ]") | value.toUpperCase().endsWith("[ ALPHA]")) {
				String randomString = getRandomString();
				value = value + randomString;
				String removeAlpha = value.toUpperCase().replace("[ALPHA]", "").replace("[ ALPHA ]", "")
						.replace("[ALPHA ]", "").replace("[ ALPHA]", "");
				column.setValue(removeAlpha);
			} else {
				column.setValue(value);
			}
			if (value.toUpperCase().endsWith("[ALPHANUM]") | value.toUpperCase().endsWith("[ ALPHANUM ]")
					| value.toUpperCase().endsWith("[ALPHANUM ]") | value.toUpperCase().endsWith("[ ALPHANUM]")) {
				String randomNum = getRandomAplhaNumeric(alphaNumericCharacters);
				value = value + randomNum;
				String removeAlphaNum = value.toUpperCase().replace("[ALPHANUM]", "").replace("[ ALPHANUM ]", "")
						.replace("[ALPHANUM ]", "").replace("[ ALPHANUM]", "");
				column.setValue(removeAlphaNum);
			} else {
				column.setValue(value);
			}
			if (value.toUpperCase().endsWith("[NUM]") | value.toUpperCase().endsWith("[ NUM ]")
					| value.toUpperCase().endsWith("[NUM ]") | value.toUpperCase().endsWith("[ NUM]")) {
				String randomNum = getRandomAplhaNumeric(numericCharacters);
				value = value + randomNum;
				String removeNum = value.toUpperCase().replace("[NUM]", "").replace("[ NUM ]", "").replace("[NUM ]", "")
						.replace("[ NUM]", "");
				column.setValue(removeNum);
			} else {
				column.setValue(value);
			}
			if (value.toUpperCase().contains("SYSDATE")) {
				if (value.toUpperCase().endsWith("[SYSDATE]") | value.toUpperCase().endsWith("[ SYSDATE ]")
						| value.toUpperCase().endsWith("[SYSDATE ]") | value.toUpperCase().endsWith("[ SYSDATE]")) {
					String date = getDate();
					value = value + date;
					String removeSysDate = value.toUpperCase().replace("[SYSDATE]", "").replace("[ SYSDATE ]", "")
							.replace("[SYSDATE ]", "").replace("[ SYSDATE]", "");
					column.setValue(removeSysDate);
				} else if (value.toUpperCase().contains("SYSDATE+")) {
					String newValue = value.replace("+", "=");
					String[] strings = newValue.split("=");
					String[] number = strings[1].split("]");
					String addDate = getSysDateAndAddDays(number[0]);	
					value = newValue + addDate;
					String removeAddSysDate = value.toUpperCase().replace(("[SYSDATE+"+number[0]+"]"), "").replace(("[ SYSDATE+"+number[0]+" ]"), "").
							replace(("[ SYSDATE+"+number[0]+"]"), "").replace(("[SYSDATE+"+number[0]+" ]"), "");
					column.setValue(removeAddSysDate);
				} else {
					column.setValue(value);
				}
			}
			lists.add(column);
		}

		verticalFlyModel.setColumn(lists);
		testActionData.setContext(new Gson().toJson(contextList));
		testActionData.setContext(verticalFlyModel);
		actionList.add(new Gson().toJson(testActionData));
		return actionList;
	}

	// Method to get random Alphabets
	private String getRandomString() {
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() <= 5) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	// Method to get random Alphanumeric or Numeric values as per provided value in
	// line number 49 & 57
	public static String getRandomAplhaNumeric(String characters) {
		SecureRandom secureRnd = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++)
			sb.append(characters.charAt(secureRnd.nextInt(characters.length())));
		return sb.toString();
	}

	// Method to get System Date
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMYYYY");
		String strDate = formatter.format(date);
		return strDate;
	}

	// Method to add date in System Date as per the numeric value given
	public static String getSysDateAndAddDays(String number) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYY");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, Integer.parseInt(number));
		String date = simpleDateFormat.format(c.getTime());
		return date;
	}
	
	private TestcaseActionJsonData openFormView() {
		TestcaseActionJsonData testActionData = new TestcaseActionJsonData();
		testActionData.setName("OPEN-FORM");

		return testActionData;
	}
}
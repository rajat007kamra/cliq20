package actions;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.sukgu.Shadow;
import selenium.driver.Driver;

/**
 * @summary This is a wrapper class for all Selenium methods.
 * Please don't use any Selenium method directly and if any method is missing then it can be created here first.
 * @author Surendra.Shekhawat
 */
public class PageHelper {
	public static int DEFAULT_TIMEOUT_SEC = 60;
	public static int X_TIMEOUT_SEC = 5;
	public static int XX_TIMEOUT_SEC = 10;
	public static int XXX_TIMEOUT_SEC = 20;
	public static int DEFAULT_POLLINNG_TIME = 1;
	
	
	//WebDriverWait wait = new WebDriverWait(driver, 5);

	public static void maximizeWindow() {
		Driver.getDriver().manage().window().maximize();
	}

	public static void click(Shadow driver, WebElement element) {
		waitForElementToBeClickable(driver, element);
		element.click();
	}

	public static void locateElement(Shadow driver, WebElement element) {
		PageHelper.waitForElementToBeDisplayed(driver, element);
		boolean isVisible = PageHelper.isElementDisplayed(element);
		Assert.assertTrue(isVisible, "Element is Present");
	}

	public static void clickUsingJs(Shadow driver, WebElement element) {
		waitForElementToBeClickable(driver, element);
		//JavascriptExecutor executor = (JavascriptExecutor) driver;
		//executor.executeScript("arguments[0].click();", element);
		element.click(); 
	}

	public static void clickUsingJsNoWait(Shadow driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public static void clickNoWait(Shadow driver, WebElement element) {
		element.click();
	}

	public static void clickRadioButton(Shadow driver, WebElement element) {
		waitForElementToBeDisplayed(driver, element);
		element.click();
	}

	public static String getText(Shadow driver, WebElement element) {
		//waitForElementToBePresent(driver, element);
		return element.getText();
	}

	public static void clearText(Shadow driver, WebElement element) {
		//waitForElementToBePresent(driver, element);
		driver.setImplicitWait(3);
		element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
	}
	
	public static String appendHashCode(WebDriver driver, String value) {
		String updatedvalue = value;
		if (value.contains("HASHCODE")) {
			updatedvalue = value.replace("HASHCODE", String.valueOf(driver.hashCode()));
		}
		return updatedvalue;
	}

	public static void sendKeys(Shadow driver, WebElement element, String text) {
		//waitForElementToBePresent(driver, element);
		waitForElementToBeClickable(driver, element);
		clearText(driver, element);
		element.sendKeys(text);
	}

	public static WebElement waitForElement(Shadow driver, WebElement element, int timoutSec, int pollingSec,
			ExpectedCondition ec) {

		FluentWait<Shadow> fWait = new FluentWait<Shadow>(driver).withTimeout(Duration.ofSeconds(timoutSec))
				.pollingEvery(Duration.ofSeconds(pollingSec))
				.ignoring(NoSuchElementException.class, TimeoutException.class)
				.ignoring(StaleElementReferenceException.class);

		try {
			fWait.until(ec);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return element;
	}

	public static void waitForElementToBeDisplayed(Shadow driver, WebElement element) {
		waitForElement(driver, element, DEFAULT_TIMEOUT_SEC, DEFAULT_POLLINNG_TIME,
				ExpectedConditions.visibilityOf(element));
	}

	public static void waitForElementToBePresent(WebDriver driver, WebElement root, String class_name) {
		//waitForElement(driver, element, DEFAULT_TIMEOUT_SEC, DEFAULT_POLLINNG_TIME,
			//	ExpectedConditions.visibilityOf(element));
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SEC);
		WebElement shadowRoot = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", root); //look for root
		wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(By.className(class_name))));
	}

	public static void waitForElementToBeClickable(Shadow driver, WebElement element) {
		waitForElement(driver, element, DEFAULT_TIMEOUT_SEC, DEFAULT_POLLINNG_TIME,
				ExpectedConditions.elementToBeClickable(element));
	}

	public static boolean isElementDisplayed(WebElement element) {
		return element.isDisplayed();
	}

	public static boolean isElementEnabled(WebElement element) {
		return element.isEnabled();
	}

	public static void selectByValue(Shadow driver, WebElement element, String value) {
		Select select = new Select(element);
		select.selectByValue(value);
	}

	public static void selectByVisibleText(Shadow driver, WebElement element, String value) {
		Select select = new Select(element);
		select.selectByVisibleText(value);
	}

	public static void waitForPageLoad(WebDriver driver) {
		new WebDriverWait(driver, DEFAULT_TIMEOUT_SEC).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				return (Boolean) js.executeScript("return document.readyState").equals("complete");
			}
		});
	}

	public static void mouseMoveToElement(WebDriver driver, WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}

	public static String getRandomeString(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	 public static void moveSlider(WebDriver driver, WebElement source, WebElement target) {
	    	Actions action = new Actions(driver);
			action.dragAndDrop(source, target).build().perform();
	    }
	    
//	    public static void moveSlider(WebDriver driver, WebElement element, int x, int y) {
//	        Actions move = new Actions(driver);
//	        Action action = (Action) move.dragAndDropBy(element, x, y).build();
//	        action.perform();
//	    }

	public static boolean waitForInvisiblity(Shadow driver, WebElement element) {
		try {
			waitForElement(driver, element, X_TIMEOUT_SEC, DEFAULT_POLLINNG_TIME,
					ExpectedConditions.invisibilityOf(element));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void sendKeys(Shadow driver, WebElement element, Keys keys, boolean clearText) {
		//waitForElementToBePresent(driver, element);
		waitForElementToBeClickable(driver, element);
		if (clearText) {
			clearText(driver, element);
		}
		element.sendKeys(keys);
	}

	public static void waitInSeconds(WebDriver driver, int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	public static void waitForElementVisibility(WebDriver driver, By by, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public static void waitForElementStaleness(WebDriver driver, WebElement elem, int timeout) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(elem)));
	}

	public static void waitForShadowElementStaleness(Shadow driver, WebElement elem, int timeout) throws Exception {
		driver.setExplicitWait(10, 2);
	}
	
	public static void waitForTextToPresent( Shadow driver, WebElement element, String expectedString,
			int specifiedTimeout) {
		//WebDriverWait wait = new WebDriverWait((WebDriver) driver, specifiedTimeout);
		driver.setImplicitWait(specifiedTimeout);
		ExpectedCondition<Boolean> elementTextEqualsString = arg0 -> element.getText().equals(expectedString);
		//wait.until(elementTextEqualsString);
	}
}

package selenium.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import selenium.Application;
import selenium.listeners.WebDriverListener;
import selenium.testdata.file.FileLoaderService;

/**
 * @summary Base class for testcases and any common property can be defined here
 * @author Manoj.Jain
 */
@Listeners(WebDriverListener.class)
@SpringApplicationConfiguration(Application.class)
public abstract class Base extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("appUrl")
	protected String appUrl;

	@Autowired
	@Qualifier("browser")
	protected String browser;

	@Autowired
	protected FileLoaderService fileLoaderService;

	public String getAppUrl() {
		return appUrl;
	}
	
	public String getBrowser() {
		return browser;
	}
}

package actions.editzone;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.google.gson.Gson;
import actions.PageHelper;
import actions.model.AttachmentModel;
import io.github.sukgu.Shadow;
import io.qameta.allure.Step;

/**
 * @summary Handles file upload
 * @author Rajat.Kamra
 */

public class Attachment implements EditZone {
	private WebDriver driver;
	private Shadow shadow;
	private AttachmentModel attachmentModel;
	final static Logger logger = Logger.getLogger(Attachment.class);

	public Attachment(WebDriver driver, String jsonString) {
		super();
		this.driver = driver;
		shadow = new Shadow(driver);
		attachmentModel = new Gson().fromJson(jsonString, AttachmentModel.class);
	}

	private String disabledIcons = "mz-standard-realm[active] mz-icon-label-button[text='Attachment'] div.icon-label-button--disabled";
	private String attachmentIcon = "mz-standard-realm[active] mz-icon-label-button[text='Attachment'] div.icon-label-button";
	private String pinIcon = "mz-standard-realm[active] mz-editzone-content-swapper div.editzone.flx-ver-ctr div.buttons__pin-button mwc-icon[title='pin']";
	private String editzoneHeader = "mz-standard-realm[active] mz-editzone-content-swapper mz-editzone-attachment mz-attachment mz-attachment-header div.editzone-header__el h3.editzone-header__text";
	private String uploadInput = "mz-standard-realm[active] div.hght-wdth-100pct.pos-rel mz-editzone-content-swapper mz-editzone-attachment mz-attachment div.editzone__add-attachments vaadin-upload#upload input#fileInput";
	private String attachFilesIcon = "mz-standard-realm[active] mz-editzone-attachment div.editzone__add-attachments mwc-icon[title='Attach files']";
	private String uploadAlert = "body mz-dialog div.mdc-dialog.mdc-dialog--open div.mdc-dialog__container div.mdc-dialog__surface";
	private String uploadAlertButtons = "body mz-dialog div.dialog-body mz-button";

	@Override
	public Date execute() {
		try {
			clickAttachmentsWidget();
			clickPinIcon();
			if (attachmentModel.getPath() != null && !attachmentModel.getPath().isEmpty()) {
				uploadDocument(attachmentModel.getPath());
			} else {
				logger.info("File name and path not exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());
		}
		return null;
	}

	@Override
	public Date validate() {
		try {
			validateAttachment("ATTACHMENT");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Date setup() {
		return null;
		// TODO Auto-generated method stub
	}

	@Override
	public Date tearDown() {
		return null;
		// TODO Auto-generated method stub
	}

	/**
	 * @summary Check if icon is enable
	 * @param driver
	 * @param button
	 * @return
	 */
	public boolean checkIfIconIsDisabled(WebDriver driver, String button) {
		boolean isActive = false;
		logger.info("Verify " + button + " icon");
		PageHelper.waitInSeconds(driver, PageHelper.X_TIMEOUT_SEC);
		try {
			if (driver.findElements(By.cssSelector(String.format(disabledIcons, button))).size() > 0) {
				isActive = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isActive;
	}

	/**
	 * @summary Click attachment widget
	 */
	@Step("Click attachment widget")
	public void clickAttachmentsWidget() {
		WebElement element = this.shadow.findElement(attachmentIcon);
		element.click();
		logger.info("Attachment widget clicked to open");
	}

	/**
	 * @summary Method to pin attachment flyout
	 */
	@Step("Pin attachment widget")
	public void clickPinIcon() {
		if (shadow.findElements(pinIcon).size() > 0) {
			shadow.findElement(pinIcon).click();
			logger.info("Pin icon clicked");
		} else {
			Assert.assertFalse(true, "Pin Icon not found");
		}
	}

	/**
	 * @summary validate attachment flyout
	 * @param flyoutName
	 * @throws InterruptedException
	 */
	@Step("Header found in attachment widget is {flyoutName}")
	public void validateAttachment(String flyoutName) throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		WebElement header = shadow.findElement(editzoneHeader);
		String headerText = header.getText();
		logger.info("Header found for opened editzone " + flyoutName + " is - " + headerText);
		if (flyoutName.trim().toUpperCase().equals(headerText.trim().toUpperCase())) {
			logger.info("Flyout opened successfully");
		} else {
			Assert.assertTrue(false, flyoutName + "Not Exists");
		}
	}

	/**
	 * @summary Method to click on PLUS/ADD icon
	 */
	public void clickAttachFilesIcon() {
		WebElement addAttachment = this.shadow.findElement(attachFilesIcon);
		addAttachment.click();
	}

	/**
	 * @summary Method to upload document
	 * @param docPath
	 */
	@Step("Upload document {docPath}")
	public void uploadDocument(String docPath) {
		try {
			File file = new File(docPath);
			String path = file.getAbsolutePath();
			logger.info("FILE PATH = " + path);
			TimeUnit.SECONDS.sleep(5);
			if (this.shadow.findElements(uploadInput).size() > 0) {
				logger.info("UPLOAD INPUT FOUND");
				List<WebElement> element = this.shadow.findElements(uploadInput);
				element.get(0).sendKeys(path);
				TimeUnit.SECONDS.sleep(20);
				if (this.shadow.findElements(uploadAlert).size() > 0) {
					logger.info("File already exists and now current file replacing the existing one");
					TimeUnit.SECONDS.sleep(5);
					List<WebElement> alertButtons = this.shadow.findElements(uploadAlertButtons);
					alertButtons.get(0).click();
				}
				TimeUnit.SECONDS.sleep(PageHelper.XX_TIMEOUT_SEC);
			} else {
				logger.info("UPLOAD INPUT NOT FOUND");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
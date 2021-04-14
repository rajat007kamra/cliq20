package actions.json.mapper;

import java.util.HashMap;
import java.util.Map;
import actions.editzone.Approve;
import actions.editzone.Attachment;
import actions.editzone.Delete;
import actions.editzone.Deny;
import actions.editzone.Discard;
import actions.editzone.Notes;
import actions.editzone.QuickList;
import actions.editzone.Revise;
import actions.editzone.Save;
import actions.editzone.Submit;
import actions.editzone.Summary;
import actions.editzone.TimeLine;
import actions.editzone.Withdraw;
import actions.editzone.Workflow;
import actions.forgetpassword.ForgetPassword;
import actions.login.BrowserNavigation;
import actions.login.Login;
import actions.login.SwitchBrowserTab;
import actions.logout.Logout;
import actions.myzone.Accordion;
import actions.myzone.NavigateScreen;
import actions.viewzone.Add;
import actions.viewzone.CheckColumnValue;
import actions.viewzone.CheckMessage;
import actions.viewzone.CheckVariable;
import actions.viewzone.ClearFilter;
import actions.viewzone.ColumnChooser;
import actions.viewzone.ExportData;
import actions.viewzone.Filter;
import actions.viewzone.GlobalSearch;
import actions.viewzone.IntegratedView;
import actions.viewzone.Lists;
import actions.viewzone.PasteData;
import actions.viewzone.PopulateForm;
import actions.viewzone.Preview;
import actions.viewzone.RecordDetails;
import actions.viewzone.RefreshData;
import actions.viewzone.SortColumn;
import actions.viewzone.TreeView;
import actions.viewzone.Upload;
import core.api.utility.DeleteUtility;
import core.api.utility.GetUtility;
import core.api.utility.PostUtility;
import core.api.utility.PutUtility;

public class FindActions {

	private Map<String, Class<?>> getLoginAction() {
		Map<String, Class<?>> loginAction = new HashMap<String, Class<?>>();
		loginAction.put("LOGIN", Login.class);
		loginAction.put("SWITCH-TAB", SwitchBrowserTab.class);
		loginAction.put("BROWSER-NAVIGATION", BrowserNavigation.class);
		loginAction.put("FORGOT-PASSWORD", ForgetPassword.class);
		return loginAction;
	}

	private Map<String, Class<?>> getLogoutAction() {
		Map<String, Class<?>> logoutAction = new HashMap<String, Class<?>>();
		logoutAction.put("LOGOUT", Logout.class);

		return logoutAction;
	}

	private Map<String, Class<?>> getViewZoneActions() {
		Map<String, Class<?>> viewZoneAction = new HashMap<String, Class<?>>();
		viewZoneAction.put("ADD", Add.class);
		viewZoneAction.put("CREATE", Add.class);
		viewZoneAction.put("ADVANCED-FILTER", Filter.class);
		viewZoneAction.put("CHECK-COLUMN-VALUE", CheckColumnValue.class);
		viewZoneAction.put("CLEAR-FILTER", ClearFilter.class);
		viewZoneAction.put("COLUMN-CHOOSER", ColumnChooser.class);
		viewZoneAction.put("EXPORT-DATA", ExportData.class);
		viewZoneAction.put("PERFORM-VZFILTER", Filter.class);
		viewZoneAction.put("VZ-FILTER", Filter.class);
		viewZoneAction.put("INTEGRATED-VIEW", IntegratedView.class);
		viewZoneAction.put("PASTE-DATA", PasteData.class);
		viewZoneAction.put("PREVIEW", Preview.class);
		viewZoneAction.put("RECORD-DETAILS", RecordDetails.class);
		viewZoneAction.put("REFRESH-DATA", RefreshData.class);
		viewZoneAction.put("TREE-VIEW", TreeView.class);
		viewZoneAction.put("UPLOAD", Upload.class);
		viewZoneAction.put("CHECK-VARIABLE", CheckVariable.class);
		viewZoneAction.put("GLOBAL-SEARCH", GlobalSearch.class);
		viewZoneAction.put("VZ-SORT", SortColumn.class);
		viewZoneAction.put("CHECK-MESSAGE", CheckMessage.class);
		viewZoneAction.put("POPULATE-FORM", PopulateForm.class);
		viewZoneAction.put("FORM-VIEW", PopulateForm.class);
		viewZoneAction.put("CHECK-LIST", Lists.class);
		return viewZoneAction;
	}

	public Map<String, Class<?>> getEditZoneActions() {
		Map<String, Class<?>> editZoneAction = new HashMap<String, Class<?>>();
		editZoneAction.put("APPROVE", Approve.class);
		editZoneAction.put("ATTACHMENT", Attachment.class);
		editZoneAction.put("OPEN-WIDGET", Attachment.class);
		editZoneAction.put("DELETE-BUTTON", Delete.class);
		editZoneAction.put("DENY", Deny.class);
		editZoneAction.put("DISCARD", Discard.class);
		editZoneAction.put("NOTES", Notes.class);
		editZoneAction.put("QUICKLIST", QuickList.class);
		editZoneAction.put("REVISE-RECORD", Revise.class);
		editZoneAction.put("REVISE", Revise.class);
		editZoneAction.put("SAVE", Save.class);
		editZoneAction.put("SUBMIT", Submit.class);
		editZoneAction.put("SUMMARY", Summary.class);
		editZoneAction.put("TIMELINE", TimeLine.class);
		editZoneAction.put("WITHDRAW", Withdraw.class);
		editZoneAction.put("WORKFLOW", Workflow.class);

		return editZoneAction;
	}

	private Map<String, Class<?>> getMyZoneActions() {
		Map<String, Class<?>> myZoneAction = new HashMap<String, Class<?>>();
		myZoneAction.put("NAVIGATE-SCREEN", NavigateScreen.class);
		myZoneAction.put("CHECK-ACCORDION", Accordion.class);
		return myZoneAction;
	}

	public Map<String, Class<?>> getFormView() {
		Map<String, Class<?>> formViewAction = new HashMap<String, Class<?>>();
		formViewAction.put("OPEN-FORM", RecordDetails.class);
		return formViewAction;
	}
	public Map<String, Class<?>> getAPI() {
		Map<String, Class<?>> formViewAction = new HashMap<String, Class<?>>();
		formViewAction.put("GET", GetUtility.class);
		formViewAction.put("POST", PostUtility.class);
		formViewAction.put("DELETE", DeleteUtility.class);
		formViewAction.put("PUT", PutUtility.class);
		return formViewAction;
	}

	public Map<String, Class<?>> findAction() {
		Map<String, Class<?>> actions = new HashMap<String, Class<?>>();
		actions.putAll(getLoginAction());
		actions.putAll(getLogoutAction());
		actions.putAll(getEditZoneActions());
		actions.putAll(getViewZoneActions());
		actions.putAll(getMyZoneActions());
		actions.putAll(getFormView());
		actions.putAll(getAPI());

		return actions;
	}
}

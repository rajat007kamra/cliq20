package actions.mappers;

import java.util.List;

import org.apache.log4j.Logger;

import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;

public class JsonActionMapper {
	final static Logger logger = Logger.getLogger(JsonActionMapper.class);
	public static List<String> generateJsonActionforSection(SectionData sectionData, StepContext stepContext) 
	{
		String sectionName = sectionData.getName();
		switch (sectionName) 
		{
			case "PARAMETERS":
				return new LoginMapper().getMappedAction(sectionData, stepContext);
			case "INPUT":
				return new InputMapper().getMappedAction(sectionData, stepContext);
			case "INPUT-PROFILE":
				return new InputProfileMapper().getMappedAction(sectionData, stepContext);
			case "CLICK":
				return new ClickMapper().getMappedAction(sectionData, stepContext);
			case "FILTER":
				return new FilterMapper().getMappedAction(sectionData, stepContext);
			case "CHECK-MESSAGE":
				return new CheckMessageMapper().getMappedAction(sectionData, stepContext);
			case "CHECK-VARIABLE":
				return new CheckVariableMapper().getMappedAction(sectionData, stepContext);
			case "LOGOUT":
				return new LogoutMapper().getMappedAction(sectionData, stepContext);
			default:
				logger.info("Action Name not found " + sectionName);
				break;
		}
		return null;
	}
	
	static boolean isAnchorType(StepContext stepContext) 
	{
		return "ANCHOR".equals(stepContext.getStepData().getStepType());
	}
	
	static boolean isReviseorWithdrawMode(StepContext stepContext) 
	{
		return "WITHDRAWAL".equals(stepContext.getProfileData().getMode()) || "REVISION".equals(stepContext.getProfileData().getMode());
	}
}

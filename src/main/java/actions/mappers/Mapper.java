package actions.mappers;

import java.util.List;

import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepContext;

/**	
 * 
 * @author Surendra.Shekhawat
 *
 */
public interface Mapper {

	public List<String> getMappedAction(SectionData sectionData, StepContext stepContext);
}

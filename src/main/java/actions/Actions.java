package actions;

import java.util.Date;

/**
 * @summary Contract for creating action classes and every method will return difference between start and end period
 * @author Manoj.Jain
 */
public interface Actions {

	public Date execute();

	public Date validate();

	public Date setup();

	public Date tearDown();

}

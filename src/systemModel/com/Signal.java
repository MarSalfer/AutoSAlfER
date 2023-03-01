/**
 * 
 */
package systemModel.com;

import systemModel.CommunicationMedium;
import systemModel.ParameterType;

/**
 * Represents a signal in the on-board network.
 * @author Martin Salfer
 * 2015-02-17
 *
 */
public class Signal extends CommunicationMedium {

	public final ParameterType type;
	
	/**
	 * 
	 */
	public Signal() {
		// TODO Auto-generated constructor stub
		type = ParameterType.CHECKED;
	}

	/**
	 * @param string
	 */
	public Signal(String string) {
		super(string);
		type = ParameterType.CHECKED;
	}

	public Signal(String string, ParameterType checked) {
		// TODO Auto-generated constructor stub
		super(string);
		type = checked;
	}

}

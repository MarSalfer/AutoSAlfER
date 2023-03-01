package systemModel;

import systemModel.subTypes.InputType;

public final class JobParam extends IncomingData {

	
	
	public JobParam() {
		this("unknown");
	}
	
	public JobParam(String name) {
		this(name, InputType.UNKNOWN, -1);
	}

	public JobParam(String jobParName, InputType type, int authLevel) {
		super(jobParName, type, authLevel);
	}
	
	
	public String toString() {
		return name;
	}
	
}

package systemModel;

import systemModel.subTypes.InputType;

public abstract class IncomingData extends AttackSurfaceSpot {

	public final InputType type; 
	public final int authLevel;
	
	public IncomingData() {
		this("unknown");
	}
	
	public IncomingData(String name) {
		this(name, InputType.UNKNOWN, -1);
	}

	public IncomingData(String name, InputType type, int authLevel) {
		super(name);
		this.type = type;
		this.authLevel = authLevel;
	}

	
	
}

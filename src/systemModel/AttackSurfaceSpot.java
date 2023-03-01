package systemModel;

public abstract class AttackSurfaceSpot {

	public final String name;
	
	AttackSurfaceSpot() {
		name = "unnamed";
	}
	
	AttackSurfaceSpot(String name) {
		this.name = name;
	}
	
}

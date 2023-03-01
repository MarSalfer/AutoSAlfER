package systemModel;

@Deprecated
public class JobIncomingParameter {
	
	public final String name;
	public final ParameterType type;
	public final Authentication authentication;
	
	@Deprecated
	public JobIncomingParameter(String name, ParameterType type, Authentication authentication) {
		this.name = name;
		this.type = type;
		this.authentication = authentication;
	}

	
	// TODO overwrite equals method
}

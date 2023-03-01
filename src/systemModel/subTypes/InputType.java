package systemModel.subTypes;

public enum InputType {
	PRIMITIVE, COMPLEX, UNKNOWN;

	public static InputType getTypeFor(String str) {
		if (str.equals("Primitiv")) {
			return InputType.PRIMITIVE;
		} else if (str.equals("komplex")) {
			return InputType.COMPLEX;
		} else {
			return InputType.UNKNOWN;
		}
	}
}

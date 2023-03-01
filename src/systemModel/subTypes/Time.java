package systemModel.subTypes;



/**
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public class Time {

	public final int seconds;

	public Time(){
		seconds = 0;
	}

	public Time(int number, String type) {
		int s = 0;
		if (type.equals("a")) // Year.
				s = number * 365 * 24 * 3600;
		else
				throw new UnsupportedOperationException("Not implemented, yet.");
		this.seconds = s;
	}

	public void finalize() throws Throwable {

	}
	/**
	 * Returns the time in day unit.
	 */
	public float inDays(){
		return 0f; // TODO
	}

	/**
	 * Returns the time in hour unit.
	 */
	public float inHours(){
		return 0f; // TODO
	}

	/**
	 * Returns the time in minute unit.
	 */
	public float inMinutes(){
		return 0f; // TODO
	}
}//end Time
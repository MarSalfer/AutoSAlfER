package types;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;

import systemModel.subTypes.Time;

/**
 * Resources model available money and time in a Gaussian distribution. In case of
 * modeling exact values, the standard deviance attribute can be set 0.This type
 * is so basic and can be used so widely, it is grouped into an extra package.
 * 
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public class Resources {

	/**
	 * Amount of money available.
	 */
	@XmlElement
	private final double moneyExpectedValue;
	@XmlElement
	private final double moneyStandardDeviation;  // TODO change to variance
	/**
	 * Amount of time available.
	 */
	@Deprecated
	private final Time timeExpectedValue;
	@Deprecated
	private final Time timeStandardDeviation;

	public Resources(){
		this(0,0,null,null);
	}
	
	public Resources(double exp, double dev) {
		this(exp,dev, new Time(0,"a"), new Time(0,"a"));
	}
	
	@Deprecated
	public Resources(float exp, float dev) {
		this(exp,dev, new Time(0,"a"), new Time(0,"a"));
	}
	
	public Resources(Resources other) {
		this(other.moneyExpectedValue, other.moneyStandardDeviation, other.timeExpectedValue, other.timeStandardDeviation);
	}

	@Deprecated
	public Resources(float moneyExpectedValue, float moneyStandardDeviation, Time timeExpectedValue, Time timeStandardDeviation) {
		this.moneyExpectedValue = moneyExpectedValue;
		this.moneyStandardDeviation = moneyStandardDeviation;
		this.timeExpectedValue = timeExpectedValue;
		this.timeStandardDeviation = timeStandardDeviation;
	}

	public Resources(double moneyExpectedValue, double moneyStandardDeviation, Time timeExpectedValue, Time timeStandardDeviation) {
		this.moneyExpectedValue = moneyExpectedValue;
		this.moneyStandardDeviation = moneyStandardDeviation;
		this.timeExpectedValue = timeExpectedValue;
		this.timeStandardDeviation = timeStandardDeviation;
	}


	@Override
	public String toString() {
		return "Resources [moneyExpectedValue=" + moneyExpectedValue
				+ ", moneyStandardDeviation=" + moneyStandardDeviation
				+ ", timeExpectedValue=" + timeExpectedValue
				+ ", timeStandardDeviation=" + timeStandardDeviation + "]";
	}

	public void finalize() throws Throwable {
	}

	/**
	 * Multiply the resources with a certain factor.
	 * 
	 * Note: The standard distribution does not implement a covariance matrix, so use only uncorrelated factors.
	 * @param factor - The multiplication factor.
	 * @return The product of the multiplication as resources object.
	 */
	public Resources multiplyWith(double factor) {
		return new Resources(moneyExpectedValue * factor,  Math.sqrt((moneyStandardDeviation * moneyStandardDeviation) * factor));
	}

	/** 
	 * Add two resources objects.
	 * 
	 * Note: The standard distribution does not implement a covariance correction, so use only uncorrelated factors.
	 * @param r - The other resources object.
	 * @return A new resources object having the value of both resources combined.
	 */
	public Resources addWith(Resources r) {
		final double moneyExpected = (moneyExpectedValue+r.moneyExpectedValue);
		final double moneyStdDev = Math.sqrt(moneyStandardDeviation*moneyStandardDeviation + r.moneyStandardDeviation*r.moneyStandardDeviation);
		return new Resources(moneyExpected, moneyStdDev);
	}

	/**
	 * Substract a resources object.
	 * 
	 * Note: The standard distribution does not implement a covariance correction, so use only uncorrelated factors.
	 * @param subtrahend
	 * @return The difference between the two resources objects.
	 */
	public Resources substractBy(Resources subtrahend) {
		double differenceExpVal = this.moneyExpectedValue - subtrahend.moneyExpectedValue;
		double differenceStdDev = Math.sqrt((this.moneyStandardDeviation*this.moneyStandardDeviation) + (subtrahend.moneyStandardDeviation*subtrahend.moneyStandardDeviation));
		return new Resources(differenceExpVal, differenceStdDev);
	}
	
	@Deprecated
	public float getMoneyExpectedValueFloat() {
		return (float) moneyExpectedValue;
	}

	@Deprecated
	public float getMoneyStandardDeviationFloat() {
		return (float) moneyStandardDeviation;
	}
	
	public double getMoneyExpectedValue() {
		return moneyExpectedValue;
	}

	public double getMoneyStandardDeviation() {
		return moneyStandardDeviation;
	}
	
	
	/**
	 * Add all resources of one collection together to one resources object.
	 * (The standard deviation is calculated as a whole.)
	 * @param resources
	 * @return
	 */
	public static Resources addUpCollection(Collection<Resources> resources) {
		double moneyExpected = 0;
		for (Resources r : resources) {
			moneyExpected += r.moneyExpectedValue;
		}
		double moneyStandardDeviation = 0;
		for (Resources r : resources) {
			moneyStandardDeviation += r.moneyStandardDeviation * r.moneyStandardDeviation;
		}
		moneyStandardDeviation = Math.sqrt(moneyStandardDeviation);
		return new Resources(moneyExpected, moneyStandardDeviation);
	}

	/**
	 * Divide the resources by the right resources.
	 * Note: Only uncorrelated costs are allowed as the standard distribution
	 * here does not implement a covariance correction.
	 * 
	 * @param divisor The divisor of this division.
	 * @return A new resources object divided by r
	 */
	public Resources divideBy(final Resources divisor) {
		return divideBy(divisor.moneyExpectedValue);
	}

	public Resources divideBy(final double divisor) {
		double exp = this.moneyExpectedValue / divisor;
		double dev = Math.sqrt(this.moneyStandardDeviation*this.moneyStandardDeviation / divisor);
		return new Resources(exp, dev);
	}

	/**
	 * @param other
	 * @return
	 */
	public boolean isCheaperThan(Resources other) {
		return getMoneyExpectedValue() < other.getMoneyExpectedValue(); 
	}

	
}//end Resources
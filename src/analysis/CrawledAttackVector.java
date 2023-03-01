package analysis;

import exploit.Exploit;

/**
 * A vertice combining two nodes with attack information.
 * A target node can be attacked from an originating node via several attack vectors, i.e. ways of exploiting the target.
 * 
 * @author Martin Salfer
 * @created 2016-04-02
 * 2016-04-09 Kosten raus faktorisiert.
 */
@Deprecated
public class CrawledAttackVector implements Comparable<CrawledAttackVector>{
	/** 
	 * What exploit was used.
	 */
	public final Exploit exploitedWith;
	/**
	 * What node is the attack coming from.
	 */
	public final CrawlableInterface attackPredecessor;

	public CrawledAttackVector(CrawlableInterface origin, Exploit usedExploit) {
		this.exploitedWith = usedExploit;
		this.attackPredecessor = origin;
	}

//	/**
//	 * Compare two attack vectors and decide of the cheaper one.
//	 * In case costs are equal, also origin and exploit are compared with each other.
//	 * @param o
//	 * @return
//	 */
//	@Override
//	public int compareTo(CrawledAttackVector o) {
//		if (o == null) {
//			throw new NullPointerException("AttackVectors must compare with AttackVectors, not a null reference.");
//		}
//		if (this.equals(o)) {
//			return 0;
//		}
//		double rel = this.costForPathSoFar - o.costForPathSoFar;
//		if (Math.abs(rel) > 1.0d) {
//			return (int)rel;
//		} else {
//			final int originDiff = this.attackPredecessor.toString().compareTo(o.attackPredecessor.toString());
//			if (originDiff == 0) {
//				return originDiff;
//			} else {
//				return this.exploitedWith.toString().compareTo(o.exploitedWith.toString());
//			}
//		}
//	}
	
	/**
	 * Compare two attack vectors and decide of the cheaper one.
	 * In case costs are equal, also origin and exploit are compared with each other.
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(CrawledAttackVector o) {
		if (o == null) {
			throw new NullPointerException("AttackVectors must compare with AttackVectors, not a null reference.");
		}
		if (this.equals(o)) {
			return 0;
		} else {
			final int originDiff = this.attackPredecessor.toString().compareTo(o.attackPredecessor.toString());
			if (originDiff != 0) {
				return originDiff;
			} else {
				return this.exploitedWith.toString().compareTo(o.exploitedWith.toString());
			}
		}
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof CrawledAttackVector && this.hashCode() == other.hashCode()) {
			CrawledAttackVector o = (CrawledAttackVector) other;
			return attackPredecessor.equals(o.attackPredecessor) && exploitedWith.equals(o.exploitedWith) ; // && costForPathSoFar==o.costForPathSoFar;
		} else {
			return false;
		}
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return attackPredecessor.hashCode() + exploitedWith.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
//		return "CrawledAttackVector [exploitedWith=" + exploitedWith + ", attackPredecessor=" + attackPredecessor + "]";
		return "CrawledAttackVector [from " + attackPredecessor + "]";
	}
	
}
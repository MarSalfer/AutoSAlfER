package analysis;

import java.util.HashSet;
import java.util.Set;

import systemModel.Asset;
import systemModel.Exploitable;
import systemModel.Software;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import exploit.Exploit;

/**
 * @author Martin Salfer
 * @created 2013
 *
 */
public class CrawledAccess extends CrawlableGraphNode {

		private final Access access;
		
		private final int hopDistanceToAttractors; 
		
		
		public CrawledAccess(AttackScenario scenario, Access access) {
			super(scenario);
			this.access = access;
			
			/* Define hop distance to attractors. */
			Integer lowestHopDistance = Integer.MAX_VALUE; 
			for (Software sw : access.getAccessibleSoftware()) { // collect software nodes ...
				int thisDistance = sw.getHopsToAssets();
				if (thisDistance < lowestHopDistance) {
					lowestHopDistance = thisDistance; // ... define the lowest hop count among those...
				}
			}
			hopDistanceToAttractors = lowestHopDistance; // ... and assign the own hop count as the lowest + 1.
		}

		
		@Override
		public Software getSoftware() {
			throw new UnsupportedOperationException("getTask() not supported for CrawledAccess! Maybe intended CrawledSoftware.getTask().");
		}

		/* (non-Javadoc)
		 * @see attackGraphConstruction.Crawlable#getHopDistanceToAttractors()
		 */
		@Override
		public int getHopDistanceToAttractors() {
			return hopDistanceToAttractors;
		}


		@Override
		public Set<Software> getDirectlyReachableSoftwareNodes() {
			return access.getAccessibleSoftware();
		}
		
		@Override
		public Set<Software> getIndirectlyReachableSoftwareNodes() {
			return getDirectlyReachableSoftwareNodes();
		}
		
		

		/**
		 * Query for attractors.
		 * @see attackGraphConstruction.Crawlable#hasAttractors()
		 * @return false - an access node has never attractors.
		 */
		@Override
		public boolean hasAttractors() {
			return false;
		}

		/**
		 * @see attackGraphConstruction.Crawlable#getAttractors()
		 * @return an empty set; allows Collection copy constructors without exception handling.
		 */
		@Override
		public Set<Asset> getAttractors() {
			return new HashSet<Asset>();
		}
		/** 
		 * Query for an incorporated attractor.
		 * @see attackGraphConstruction.Crawlable#getAsset()
		 * @return null - this object never incorporates an attractor.
		 */
		@Override
		public Asset getAsset() {
			throw new UnsupportedOperationException("Not supported by CrawledAccess! Maybe CrawledAttractor or getAttractors() intended.");
		}

		/** 
		 * Query for being an attractor.
		 * @see attackGraphConstruction.Crawlable#isAttractor()
		 * @return false - an access node is not an attractor.
		 */
		@Override
		public boolean isAttractor() {
			return false;
		}

		/**
		 * Query for the incorporated exploitable.
		 * @return null - an Access incorporates no exploitable, but access 
		 */
		@Override
		public Exploitable getExploitable() {
			return null; //throw new UnsupportedOperationException("Not supported by CrawledAccess! Should not be called.");
		}

		@Override
		public String toString() {
			return "CrawlableAccess[" + access.toString() + "]";
		}

		/**
		 * Get the cost for the path so far.
		 * In this case, it is the start of a path, so the cost must be the access cost.
		 */
		@Override
		public double getMinCostMeanTilHere() {
			return access.getAccessCost().getMoneyExpectedValue();
		}


		public Access getAccess() {
			return access;
		}


		@Override
		public Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target) {
			return scenario.getAllExploitsAgainstTask(target.getExploitable());
		}
		
		/* (non-Javadoc)
		 * @see analysis.Crawlable#getName()
		 */
		@Override
		public String getName() {
			return access.getName();
		}

}

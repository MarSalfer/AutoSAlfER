package analysis;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import exploit.Exploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Exploitable;
import systemModel.Software;

/**
 * Crawlable that is being used in Phase 3.
 * 
 * Abstract class that can contain Software, Accesses or Assets.
 * 
 * @author Martin Salfer
 * @created 2016-04-04
 *
 */
public interface CrawlableInterface {

	public abstract Software getSoftware();

	public abstract int getHopDistanceToAttractors();

	public abstract Set<Software> getDirectlyReachableSoftwareNodes();

	public abstract boolean hasAttractors();

	public abstract Set<Asset> getAttractors();

	public abstract boolean isAttractor();

	public abstract Asset getAsset();

	public double getMinCostMeanTilHere();

	public void addNewExploitOrigin(CrawlableInterface origin, Exploit exploit, double newPathCostToHere);

	public CrawlableInterface getAttackPredecessor();

	public Exploit getUsedExploit();

	/**
	 * Query for exploitable.
	 */
	public abstract Exploitable getExploitable();

	/**
	 * Query if this node is already closed by the search algorithm, i.e. has
	 * been already expanded.
	 * 
	 * Complexity: O(1)
	 * 
	 * @return true, if this node has been already expanded.
	 */
	public boolean isClosed();

	/**
	 * Close a Crawlable, i.e. mark it as expanded. Note: There is no way to
	 * reset or open a Crawlable again. It's only used once.
	 */
	public void close();

	/**
	 * Get all attached Communication Medium nodes. This method is overloaded
	 * CrawledSoftware.
	 * 
	 * @return null.
	 * @deprecated Consider the attack direction with
	 *             {@link #getAttackableComMedia()}.
	 */
	public Set<CommunicationMedium> getReachableComMedia();

	/**
	 * Gett attach Communicaton Media someone can write to.
	 * 
	 * @return null
	 */
	public Set<CommunicationMedium> getAttackableComMedia();

	/**
	 * Getter
	 * 
	 * @return Cost for reaching this node so far + the heuristically predicted
	 *         cost.
	 */
	public double getCostCapitalH();

	/**
	 * @return all exploits that this Crawable can launch against the target.
	 * @param target
	 */
	public abstract Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target);

	/**
	 * @return all attack vectors this Crawlable has already been found with.
	 */
	public Map<CrawledAttackVector, Double> getAttackVectorsAndCosts();

	/**
	 * @return
	 */
	public abstract String getName();


	/**
	 * @param origin
	 * @return
	 */
	public abstract boolean hasPredecessor(CrawlableInterface origin);
	
	
	public abstract int getCreationNumber();

	/**
	 * @return
	 */
	public abstract Set<CrawlableInterface> getAttackOrigins();

	/**
	 * @param origin
	 * @return
	 */
	public abstract double getExploitationLikelihoodFrom(CrawlableInterface origin);
}

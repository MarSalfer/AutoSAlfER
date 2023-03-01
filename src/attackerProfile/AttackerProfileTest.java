package attackerProfile;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import systemModel.Software;
import systemModel.subTypes.Time;
import types.Resources;

public class AttackerProfileTest {

	private AttackerProfile ap;
	
	@Before
	public void setUp() throws Exception {
		ap = new AttackerProfile("Internet Harvest Group", 5, new Resources(300000, 100000, new Time(3,"a"), new Time(1,"a")));
		
		/*
		 * Motivation
		 */
		ap.addMotivation(new Motive("Finances", 1.0f));
		ap.addMotivation(new Motive("Reputation", 0.01f));

		/*
		 * Skills
		 */
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		
		/*
		 * Access.
		 */
		ap.addAccess(new Access("Phishing Website", new Software("Browser"), 10000));
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAttackerProfile() {
		System.out.println(ap);
		assertNotNull(ap);
	}

}

//package analysis;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Stress test of the crawler with a huge system model, which contains much dead end software.
// * The topology is a big star with long arms that are dead ends.
// */
//public class CrawlerBenchmarkFatStar {
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//
//
//
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_02_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 1);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_04_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 2);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_06_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 3);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_08_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 4);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_10_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 5);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_12_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 6);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_14_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 7);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_16_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 8);
//	}
//
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_18_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 9);
//	}
//
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_20_000() {
//		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 10);
//	}
//
//	
//	
//		
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_01_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_02_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(1_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_03_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(1_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_04_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_05_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(2_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_06_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(3_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_07_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(3_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_08_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(4_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_09_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(4_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_10_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(5_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_11_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(5_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_12_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(6_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_13_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(6_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_14_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(7_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_15_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(7_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_16_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(8_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_17_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(8_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_18_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(9_000, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_19_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(9_500, 2);
////	}
////
////	@Test
////	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_20_000() {
////		CrawlerTest.testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(10_000, 2);
////	}
//
//
//
//
//
//}

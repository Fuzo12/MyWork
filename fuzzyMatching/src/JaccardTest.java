//package aed_TP1;

import static org.junit.Assert.*;

import org.junit.Test;

public class JaccardTest {

	@Test
	public void testJaccardSimilarity() {
		String s1 = "England";
		String s2 = "England UK";
		String s3 = "Scotland";
		assertEquals(Jaccard.jaccardSimilarity(s1, s1),100);
		assertEquals(Jaccard.jaccardSimilarity(s1, s2),50);
		assertEquals(Jaccard.jaccardSimilarity(s1, s3),0);
	}

}

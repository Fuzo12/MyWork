//package aed_TP1;

import org.junit.Test;

import static org.junit.Assert.*;

public class CosineTest {

	@Test
	public void testCosineSimilarity() {
		String s1 = "England";
		String s2 = "England UK";
		String s3 = "Scotland";
		String s4 = "England UK England";
		assertEquals(Cosine.cosineSimilarity(s1, s1),100);
		assertEquals(Cosine.cosineSimilarity(s1, s3),0);
		assertEquals(Cosine.cosineSimilarity(s1, s2),70);
		assertEquals(Cosine.cosineSimilarity(s1, s4),89);
		assertEquals(Cosine.cosineSimilarity(s1, ""),0);
	}

}

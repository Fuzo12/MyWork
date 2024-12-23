//package aed_TP1;

import static org.junit.Assert.*;

import org.junit.Test;

public class LevenshteinTest {

	@Test
	public void testLevenshteinDistance() {
		String s1 = "England";
		String s2 = "Englnd";
		String s3 = "";
		assertEquals(Levenshtein.levenshteinDistance(s1, s1),0);
		assertEquals(Levenshtein.levenshteinDistance(s1, s2),1);
		assertEquals(Levenshtein.levenshteinDistance(s1, s3),7);
		assertEquals(Levenshtein.levenshteinDistance(s3, s1),7);
	}

	@Test
	public void testCostOfSubstitution() {
		assertEquals(Levenshtein.costOfSubstitution('s', 's'),0);
		assertEquals(Levenshtein.costOfSubstitution('s', 'c'),1);
	}

	@Test
	public void testMin() {
		int[] numbers = {10,13,11,8,12};
		assertEquals(Levenshtein.min(numbers),8);
	}

	@Test
	public void testRation() {
		String s1 = "England";
		String s2 = "Englnd";
		String s3 = "";
		int result1 = Levenshtein.levenshteinDistance(s1, s1);
		int result2 = Levenshtein.levenshteinDistance(s1, s2);
		int result3 = Levenshtein.levenshteinDistance(s1, s3);
		int result4 = Levenshtein.levenshteinDistance(s3, s1);
		assertEquals(Levenshtein.ration(s1, s1),100);
		assertEquals(Levenshtein.ration(s1, s2),92);
		assertEquals(Levenshtein.ration(s1, s3),0);
		assertEquals(Levenshtein.ration(s3, s1),0);
	}

}

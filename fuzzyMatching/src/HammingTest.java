//package aed_TP1;

import static org.junit.Assert.*;

import org.junit.Test;

public class HammingTest {

	@Test
	public void testHammingDistance() throws HammingException {
		String s1 = "Teste";
		String s2 = "Testi";
		String s3 = "eTest";
		assertEquals(Hamming.hammingDistance(s1, s1),0);
		assertEquals(Hamming.hammingDistance(s1, s2),1);
		assertEquals(Hamming.hammingDistance(s1, s3),5);
	}
	
	@Test
	public void testHammingDistanceException() {
		String s1 = "Teste";
		String s2 = "Test";
		try {
			Hamming.hammingDistance(s1, s2);
		} catch (HammingException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRation() throws HammingException {
		String s1 = "Teste";
		String s2 = "Testi";
		String s3 = "eTest";
		assertEquals(Hamming.ration(s1, s1),100);
		assertEquals(Hamming.ration(s1, s2),80);
		assertEquals(Hamming.ration(s1, s3),0);
	}

}

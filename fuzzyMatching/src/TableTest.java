//package aed_TP1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class TableTest {

	@Test
	public void testFillTable() throws IOException {
		ArrayList<HashMap> table1 = new ArrayList<HashMap>();
		table1 = Table.fillTable("LevenshteinTeste1.csv");
		assertEquals(Table.convertTable(table1),"country.LevenshteinTeste1,variable1.LevenshteinTeste1,variable2.LevenshteinTeste1\n"
											  + "England,empty,Large\n"
											  + "Portugal,17,Small\n"
											  + "France,12,Large");
	}

	@Test
	public void testConvertTable() throws IOException {
		ArrayList<HashMap> table1 = new ArrayList<HashMap>();
		table1 = Table.fillTable("LevenshteinTeste2.csv");
		assertEquals(Table.convertTable(table1),"country.LevenshteinTeste2,variable1.LevenshteinTeste2,variable2.LevenshteinTeste2\n"
												+ "Englnd,1,Underpopulated\n"
												+ "Tugal,13,Overpopulated\n"
												+ "Frani,19,Reasonably Populated");
	}

	@Test
	public void testCombinations() throws IOException {
		ArrayList<HashMap> table1 = new ArrayList<HashMap>();
		table1 = Table.fillTable("LevenshteinTeste1.csv");
		ArrayList<HashMap> table2 = new ArrayList<HashMap>();
		table2 = Table.fillTable("LevenshteinTeste2.csv");
		ArrayList<HashMap> table3 = new ArrayList<HashMap>();
		try {
			table3 = Table.combinations(table1, "country", table2, "country", "levenshtein");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"country.LevenshteinTeste1,country.LevenshteinTeste2,Levenshtein\n"
												+ "England,Englnd,92\n"
												+ "England,Tugal,58\n"
												+ "England,Frani,58\n"
												+ "Portugal,Englnd,42\n"
												+ "Portugal,Tugal,76\n"
												+ "Portugal,Frani,46\n"
												+ "France,Englnd,50\n"
												+ "France,Tugal,45\n"
												+ "France,Frani,81");
		
		table1 = Table.fillTable("HammingTeste1.csv");
		table2 = Table.fillTable("HammingTeste2.csv");
		try {
			table3 = Table.combinations(table1, "country", table2, "country", "hamming");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"country.HammingTeste1,country.HammingTeste2,Hamming\n"
												+ "England,Englond,85\n"
												+ "England,Portugo,0\n"
												+ "England,Francis,0\n"
												+ "Portuga,Englond,0\n"
												+ "Portuga,Portugo,85\n"
												+ "Portuga,Francis,0\n"
												+ "Francos,Englond,0\n"
												+ "Francos,Portugo,0\n"
												+ "Francos,Francis,85");
		
		table1 = Table.fillTable("JaccardTeste1.csv");
		table2 = Table.fillTable("JaccardTeste2.csv");
		try {
			table3 = Table.combinations(table1, "location", table2, "location", "jaccard");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"location.JaccardTeste1,location.JaccardTeste2,Jaccard\n"
												+ "Nova York,Velha York,33\n"
												+ "Nova York,Velha Guiné,0\n"
												+ "Nova York,Velha Emphshire,0\n"
												+ "Nova York,Guiné Bissau,0\n"
												+ "Nova Guiné,Velha York,0\n"
												+ "Nova Guiné,Velha Guiné,33\n"
												+ "Nova Guiné,Velha Emphshire,0\n"
												+ "Nova Guiné,Guiné Bissau,33\n"
												+ "Nova Emphshire,Velha York,0\n"
												+ "Nova Emphshire,Velha Guiné,0\n"
												+ "Nova Emphshire,Velha Emphshire,33\n"
												+ "Nova Emphshire,Guiné Bissau,0");
		
		table1 = Table.fillTable("JaccardTeste1.csv");
		table2 = Table.fillTable("JaccardTeste2.csv");
		try {
			table3 = Table.combinations(table1, "location", table2, "location", "cosine");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"location.JaccardTeste1,location.JaccardTeste2,Cosine\n"
												+ "Nova York,Velha York,49\n"
												+ "Nova York,Velha Guiné,0\n"
												+ "Nova York,Velha Emphshire,0\n"
												+ "Nova York,Guiné Bissau,0\n"
												+ "Nova Guiné,Velha York,0\n"
												+ "Nova Guiné,Velha Guiné,49\n"
												+ "Nova Guiné,Velha Emphshire,0\n"
												+ "Nova Guiné,Guiné Bissau,49\n"
												+ "Nova Emphshire,Velha York,0\n"
												+ "Nova Emphshire,Velha Guiné,0\n"
												+ "Nova Emphshire,Velha Emphshire,49\n"
												+ "Nova Emphshire,Guiné Bissau,0");
		try {
			table3 = Table.combinations(table1, "location", table2, "location", "wrongMetric");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testMergeTable() throws IOException {
		ArrayList<HashMap> table1 = new ArrayList<HashMap>();
		table1 = Table.fillTable("LevenshteinTeste1.csv");
		ArrayList<HashMap> table2 = new ArrayList<HashMap>();
		table2 = Table.fillTable("LevenshteinTeste2.csv");
		ArrayList<HashMap> table3 = new ArrayList<HashMap>();
		try {
			table3 = Table.mergeTable(table1, "country", table2, "country", "levenshtein");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"country.LevenshteinTeste1,variable1.LevenshteinTeste1,variable2.LevenshteinTeste1,country.LevenshteinTeste2,variable1.LevenshteinTeste2,variable2.LevenshteinTeste2,Levenshtein\n"
												+ "England,empty,Large,Englnd,1,Underpopulated,92\n"
												+ "Portugal,17,Small,Tugal,13,Overpopulated,76\n"
												+ "France,12,Large,Frani,19,Reasonably Populated,81");
		
		table1 = Table.fillTable("HammingTeste1.csv");
		table2 = Table.fillTable("HammingTeste2.csv");
		try {
			table3 = Table.mergeTable(table1, "country", table2, "country", "hamming");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"country.HammingTeste1,variable1.HammingTeste1,variable2.HammingTeste1,country.HammingTeste2,variable1.HammingTeste2,variable2.HammingTeste2,Hamming\n"
												+ "England,10,Large,Englond,10,Large,85\n"
												+ "Portuga,17,Small,Portugo,17,Small,85\n"
												+ "Francos,12,Large,Francis,12,Large,85");
		
		table1 = Table.fillTable("JaccardTeste1.csv");
		table2 = Table.fillTable("JaccardTeste2.csv");
		try {
			table3 = Table.mergeTable(table1, "location", table2, "location", "jaccard");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"location.JaccardTeste1,variable1.JaccardTeste1,variable2.JaccardTeste1,location.JaccardTeste2,variable1.JaccardTeste2,variable2.JaccardTeste2,Jaccard\n"
												+ "Nova York,10,Large,Velha York,C,D,33\n"
												+ "Nova Guiné,17,Small,Velha Guiné,E,F,33\n"
												+ "Nova Guiné,17,Small,Guiné Bissau,A,B,33\n"
												+ "Nova Emphshire,12,Large,Velha Emphshire,G,H,33");
		
		table1 = Table.fillTable("JaccardTeste1.csv");
		table2 = Table.fillTable("JaccardTeste2.csv");
		try {
			table3 = Table.mergeTable(table1, "location", table2, "location", "cosine");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(Table.convertTable(table3),"location.JaccardTeste1,variable1.JaccardTeste1,variable2.JaccardTeste1,location.JaccardTeste2,variable1.JaccardTeste2,variable2.JaccardTeste2,Cosine\n"
												+ "Nova York,10,Large,Velha York,C,D,49\n"
												+ "Nova Guiné,17,Small,Velha Guiné,E,F,49\n"
												+ "Nova Guiné,17,Small,Guiné Bissau,A,B,49\n"
												+ "Nova Emphshire,12,Large,Velha Emphshire,G,H,49");
		try {
			table3 = Table.mergeTable(table1, "location", table2, "location", "wrongMetric");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
}

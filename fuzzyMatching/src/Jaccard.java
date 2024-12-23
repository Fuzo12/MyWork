//package aed_TP1;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Classe responsavel pelas operacoes envolvendo a metrica de Jaccard.
 * Fontes: 
 * 	https://studymachinelearning.com/jaccard-similarity-text-similarity-metric-in-nlp/
 * @author Claudio Coelho a22106474
 */
public class Jaccard {
	/**
	 * Calcula a Similariedade de Jaccard entre duas Strings.
	 * @param s1 Primeira String que vai ser utilizada para calcular a Similariedade de Jaccard.
	 * @param s2 Segunda String que vai ser utilizada para calcular a Similariedade de Jaccard.
	 * @return Percentagem da Similariedade de Jaccard, arredondada as unidades.
	 */
	public static int jaccardSimilarity(String s1,String s2) {
		String[] conj1 = s1.replace(", ", " ").split(" ");
		String[] conj2 = s2.replace(", ", " ").split(" ");
		HashSet<String> elements = new LinkedHashSet<String>();
		for(int i=0;i<conj1.length;i++) {
			elements.add(conj1[i].toLowerCase());
		}
		for(int i=0;i<conj2.length;i++) {
			elements.add(conj2[i].toLowerCase());
		}
		int intersect = 0;
		for(int i=0; i<conj1.length;i++) {
			for(int j=0; j<conj2.length;j++) {
				if(conj1[i].equals(conj2[j])) {intersect++;}
			}
		}
		return (int) ((double) intersect / (double)(elements.size())*100);
	}
}

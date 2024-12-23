//package aed_TP1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Classe responsavel pelas operacoes envolvendo a metrica de Cosine.
 * Fontes:
 * 	https://www.geeksforgeeks.org/cosine-similarity/
 * @author Claudio Coelho
 */
public class Cosine {
	/**
	 * Calcula a Similariedade de Jaccard entre duas Strings.
	 * @param s1 Primeira String que vai ser utilizada para calcular a Similariedade de Jaccard.
	 * @param s2 Segunda String que vai ser utilizada para calcular a Similariedade de Jaccard.
	 * @return Percentagem da Similariedade de Cosine, arredondada as unidades.
	 */
	public static int cosineSimilarity(String s1,String s2){
		String[] conj1 = s1.split(" ");
		String[] conj2 = s2.split(" ");
		HashSet<String> filter = new LinkedHashSet<String>();
		HashMap<String,Double> counter1 = new HashMap<String,Double>();
		HashMap<String,Double> counter2 = new HashMap<String,Double>();
		for(int i=0;i<conj1.length;i++) {
			filter.add(conj1[i].toLowerCase());
		}
		for(int i=0;i<conj2.length;i++) {
			filter.add(conj2[i].toLowerCase());
		}

		for(String i : filter) {
			counter1.put(i, 0.0);
			counter2.put(i, 0.0);
		}
		for(int i=0; i<conj1.length;i++) {
				counter1.put(conj1[i].toLowerCase(),counter1.get(conj1[i].toLowerCase())+1);
		}
		for(int i=0; i<conj2.length;i++) {
				counter2.put(conj2[i].toLowerCase(),counter2.get(conj2[i].toLowerCase())+1);
		}
		double num = 0;
		double den1 = 0;
		double den2 = 0;
		
		for(String i:filter) {
			num += (counter1.get(i)*counter2.get(i));
		}
		for(String i:filter) {
			den1 += Math.pow(counter1.get(i),2);
		}
		den1 = Math.sqrt(den1);
		for(String i:filter) {
			den2 += Math.pow(counter2.get(i),2);
		}
		den2 = Math.sqrt(den2);

		return (int) (((num)/(den1*den2))*100);
	}
}

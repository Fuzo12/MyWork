//package aed_TP1;

/**
 * Classe responsavel pelas operacoes envolvendo a metrica de Hamming.
 * Fontes:
 * 	https://towardsdatascience.com/3-text-distances-that-every-data-scientist-should-know-7fcdf850e510
 * @author Claudio Coelho
 */
public class Hamming {
	/**
	 * Calcula a Distancia de Hamming entre duas Strings.
	 * @param s1 Primeira String que vai ser utilizada para calcular a Distancia de Hamming.
	 * @param s2 Segunda String que vai ser utilizada para calcular a Distancia de Hamming.
	 * @return Distancia de Hamming entre duas Strings.
	 * @throws HammingException : Surge quando as Strings dos parametros s1 e s2 nao tem o mesmo comprimento.
	 */
	public static int hammingDistance(String s1,String s2) throws HammingException {
		String string1 = s1.toLowerCase();
		String string2 = s2.toLowerCase();
		if((string1.length() != string2.length())) {throw new HammingException("As Strings devem ter o mesmo comprimento!");}
		int distance = 0;
		for(int i = 0; i<string1.length();i++) {
			if(string1.toLowerCase().charAt(i)!= string2.toLowerCase().charAt(i)) {distance++;}
		}
		return distance;
	}
	
	/**
	 * Calcula a razao da Distancia de Hamming entre duas Strings, ou seja, a similariedade entre estas.
	 * @param s1 Primeira String que vai ser utilizada para calcular a razao.
	 * @param s2 Segunda String que vai ser utilizada para calcular a razao.
	 * @return A porcentagem da similariedade de duas Strings, obtida segundo a Distancia de Hamming.
	 * @throws HammingException : Surge caso as Strings nao tem o mesmo comprimento.
	 */
	static int ration(String s1, String s2) throws HammingException {
		return (int) ((double) (s1.length()-hammingDistance(s1, s2) ) / (double) (s1.length())*100);
	}
}

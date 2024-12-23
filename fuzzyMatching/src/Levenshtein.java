//package aed_TP1;
import java.util.*;

/**
 * Classe responsavel pelas operacoes envolvendo a metrica de Levenshtein.
 * Fontes:
 * 	https://www.baeldung.com/java-levenshtein-distance
 * @author Claudio Coelho
 */
public class Levenshtein {
	/**
	 * Calcula a Distancia de Levenshtein entre duas Strings.
	 * @param s1 Primeira String que vai ser utilizada para calcular a Distancia de Levenshtein.
	 * @param s2 Segunda String que vai ser utilizada para calcular a Distancia de Levenshtein.
	 * @return Distancia de Levenshtein entre duas Strings.
	 */
	static int levenshteinDistance(String s1,String s2) {
		String string1 = s1.toLowerCase();
		String string2 = s2.toLowerCase();
	    int[][] dp = new int[string1.length() + 1][string2.length() + 1];

	    for (int i = 0; i <= string1.length(); i++) {
	        for (int j = 0; j <= string2.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	            	int[] cost = {dp[i - 1][j - 1] + costOfSubstitution(string1.charAt(i - 1),
	            				  string2.charAt(j - 1)), 
	            				  dp[i - 1][j] + 1, 
	            				  dp[i][j - 1] + 1};
	            	dp[i][j] = min(cost);
	            }
	        }
	    }

	    return dp[string1.length()][string2.length()];
	}
	
	/**
	 * Metodo utilizado pelo metodo Levenshtein Distance, o mesmo e utilizado para determinar se dois carateres sao iguais.
	 * @param ch1 Primeiro caracter a ser comparado.
	 * @param ch2 Segundo caracter a ser comparado.
	 * @return 0 se os caracteres forem iguais e 1 caso sejam diferentes.
	 */
	static int costOfSubstitution(char ch1, char ch2) {
		if(ch1 == ch2) {return 0;}
		return 1;
	}
	
	/**
	 * Determina o menor valor presente num array de numeros inteiros.
	 * @param numbers Array de numeros inteiros, sobre o qual sera aplicado este metodo.
	 * @return O valor minimo presente no array de numeros inteiros.
	 */
	static int min(int[] numbers) {
		int min = (int) Double.POSITIVE_INFINITY;
		for(int i = 0; i<numbers.length;i++) {
			if(numbers[i]<min) {min = numbers[i];}
		}
		return min;
	}

	/**
	 * Calcula a razao da Distancia de Levenshtein entre duas Strings, ou seja, a similariedade entre estas.
	 * @param s1 Primeira String que vai ser utilizada para calcular a razao.
	 * @param s2 Segunda String que vai ser utilizada para calcular a razao.
	 * @return A porcentagem da similariedade de duas Strings, obtida segundo a Distancia de Levenshtein.
	 */
	static int ration(String s1, String s2) {
		return (int) ((double) (s1.length()+s2.length()-levenshteinDistance(s1, s2) ) / (double) (s1.length()+s2.length())*100);
	}	
}

//package aed_TP1;

/**
 * Excecao associada ao nao cumprimento das normas associadas a metrica de Hamming.
 * @author Claudio Coelho
 */
public class HammingException extends Exception{
	/**
	 * Construtor da classe HammingException.
	 */
	HammingException(){super();}
	
	/**
	 * Construtor da classe HammingException.
	 * @param error Mensagem de erro que surge quando ocorre a excecao.
	 */
	HammingException(String error){
		super(error);
	}
}

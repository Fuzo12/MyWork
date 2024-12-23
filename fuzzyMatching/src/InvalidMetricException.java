//package aed_TP1;

/**
 * Excecao associada a selecao de uma metrica que nao e suportada pelo programa, ou que nao existe.
 * @author Claudio Coelho
 */
public class InvalidMetricException extends Exception{
	/**
	 * Consturtor da classe InvalidMetricException.
	 */
	InvalidMetricException(){super();}
	
	/**
	 * Consturtor da classe InvalidMetricException.
	 * @param error Mensagem de erro que surge quando ocorre a excecao.
	 */
	InvalidMetricException(String error){
		super(error);
	}
}

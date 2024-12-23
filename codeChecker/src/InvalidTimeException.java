/**
 * Excecao associada a criacao de horas impossiveis.
 * @author Claudio Coelho
 */
public class InvalidTimeException extends Exception{
	/**
	 * Consturtor da classe InvalidTimeException.
	 */
	InvalidTimeException(){super();}
	/**
	 * Consturtor da classe InvalidTimeException.
	 * @param error Mensagem de erro que surge quando ocorre a excecao.
	 */
	InvalidTimeException(String error){super(error);}
}

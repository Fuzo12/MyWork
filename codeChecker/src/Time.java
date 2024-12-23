/**
 * Classe associada ao tempo, e utilizada para definir horas.
 * @author Claudio Coelho
 */
public class Time {
	/**
	 * Parametro de um objeto da classe Time.
	 */
	private int hour;
	/**
	 * Parametro de um objeto da classe Time.
	 */
	private int min;
	/**
	 * Parametro de um objeto da classe Time.
	 */
	private int sec;
	
	/**
	 * Construtor da classe Time.
	 * @param _hour Representa a hora da hora a ser criada.
	 * @param _min Representa os minutos da hora a ser criada.
	 * @param _sec Segundos Representa os segundos da hora a ser criada.
	 * @throws InvalidTimeException Excecao que surge perante a criacao de uma hora invalida.
	 */
	Time(int _hour,int _min,int _sec) throws InvalidTimeException{
		if(_hour < 0 || _hour > 23) {throw new InvalidTimeException("A hora deve estar entre 0 e 23!");}
		if(_min < 0 || _min > 59) {throw new InvalidTimeException("Os minutos devem estar entre 0 e 59!");}
		if(_sec < 0 || _sec > 59) {throw new InvalidTimeException("Os segundos deve estar entre 0 e 59!");}
		this.hour = _hour;
		this.min = _min;
		this.sec = _sec;
	}
	
	/**
	 * String que converte uma String do formato xx:xx:xx num objeto da classe Time.
	 * @param str String a ser convertida.
	 * @return Objeto da classe time obtido atraves da string.
	 * @throws NumberFormatException Excecao que surge quando a String a ser convertida num valor numerico nao tem o valor correto.
	 * @throws InvalidTimeException Excecao que surge quando o objeto da classe Time possui um formato invalido.
	 */
	public static Time convertString(String str) throws NumberFormatException, InvalidTimeException {
		String[] time = str.split(":");
		return new Time(Integer.valueOf(time[0]),Integer.valueOf(time[1]),Integer.valueOf(time[2]));
	}
	
	/**
	 * Converte um objeto da classe Time para uma String com o formato xx:xx:xx.
	 */
	public String toString() {
		String result = "";
		if(this.hour >= 0 && this.hour <= 9) result += "0"+this.hour+":";
		else result += this.hour+":";
		if(this.min >= 0 && this.min <= 9) result += "0"+this.min+":";
		else result += this.min+":";
		if(this.sec >= 0 && this.sec <= 9) result += "0"+this.sec;
		else result += this.sec;
		return result;
	}
	
	/**
	 * Metodo utilizado para comparar dois objetos da classe Time.
	 * @param a Objeto da classe time a ser comparado.
	 * @return true se b for maior que a e false se b for menor que a.
	 */
	//a.compareTime(b) -> se a>b true
	public boolean compareTime(Time a) {
		if(this.hour > a.hour) {return true;}
		else if(this.hour == a.hour) {
			if(this.min > a.min) {return true;}
			else if(this.min == a.min) {
				if(this.sec > a.sec) {return true;}
				return false;
			}
			return false;
		}
		return false;
		
	}
}

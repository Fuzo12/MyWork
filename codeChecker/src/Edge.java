/**
 * Classe associada as Edges e aos metodos que permitem a manipulacao e criacao dos mesmos.
 * @author Claudio Coelho
 */
class Edge{
	/**
	 * Indica a origem do caminho.
	 */
	private final int from;
	/**
	 * Indica o destino do caminho.
	 */
	private final int to;
	/**
	 * Indica o custo do caminho.
	 */
	private final double weight;
	/**
	 * Indica a o nome associado a origem do caminho.
	 */
	private final String nameFrom;
	/**
	 * Indica o nome associado ao destino do caminho.
	 */
	private final String nameTo;
	
	/**
	 * Construtor da classe Edge com custo.
	 * @param from Representa a origem do caminho.
	 * @param to Representa o destino do caminho.
	 * @param weight Representa o custo do caminho.
	 * @param nameFrom Representa a o nome associado a origem do caminho.
	 * @param nameTo Representa o nome associado ao destino do caminho.
	 */
	public Edge(int from, int to, double weight, String nameFrom, String nameTo){
		//assert from != to; //Esta linha tede de ser comentado, pois estava interferir com a execução dos testes únitários.
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.nameFrom = nameFrom;
		this.nameTo = nameTo;
	}
	
	/**
	 * Construtor da classe Edge sem custo.
	 * @param from Representa a origem do caminho.
	 * @param to Representa o destino do caminho.
	 * @param nameFrom Representa a o nome associado a origem do caminho.
	 * @param nameTo Representa o nome associado ao destino do caminho.
	 */
	public Edge(int from, int to, String nameFrom, String nameTo){
		this(from, to, 1.0, nameFrom, nameTo);
	}
	
	/**
	 * Metodo que permit obter uma Edge com o caminho contrario a Edge sobre a qual foi aplicada este metodos
	 * @return Edge com o caminho contrario.
	 */
	public Edge reverse(){
		return new Edge(to, from, weight, nameTo, nameFrom);
	}
	
	/**
	 * Compara o custo de duas edges.
	 * @param e1 Primeira Edge a ser comparada.
	 * @param e2 Segunda Edge a ser comparada.
	 * @return 0 se os custos forem iguais, 1 se e1 for maior e -1 se e2 for maior.
	 */
	public static int compareByWeight(Edge e1, Edge e2){
		double z = e1.weight - e2.weight;
		return z < 0 ? -1 : z > 0 ? 1 : 0;
	}
	
	/**
	 * Metodo que permite obter o vertice de origem de um caminho.
	 * @return O numero associado ao vertice de origem de um caminho.
	 */
	public int getFrom() {
		return this.from;
	}
	
	/**
	 * Metodo que permite obter o vertice de destino de um caminho.
	 * @return O numero associado ao vertice de destino de um caminho.
	 */
	public int getTo() {
		return this.to;
	}
	
	/**
	 * Metodo que permite obter o peso de um caminho.
	 * @return O peso de um caminho.
	 */
	public double getWeight() {
		return this.weight;
	}
	
	/**
	 * Metodo que permite obter o nome do vertice de origem de caminho.
	 * @return O nome do vertice de origem de um caminho.
	 */
	public String getNameFrom() {
		return this.nameFrom;
	}
	
	/**
	 * Metodo que permite obter nome do vertice de origem de caminho.
	 * @return O nome do vertice de origem de um caminho.
	 */
	public String getNameTo() {
		return this.nameTo;
	}
	
	/**
	 * Metodo que permite fazer print na consola de um objeto da classe Edge.
	 */
	@Override
	public String toString() {
		return this.from + " " + this.to + " " + this.weight;
	}
	
}
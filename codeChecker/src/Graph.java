import java.util.ArrayList;
import java.util.List;

/**
 * Classe associada aos grafos, contem metodos que auxiliam na criacao e manipulacao de grafos.
 * @author Claudio Coelho
 */
public class Graph{
	/**
	 * Representa o numero de vertices do grafo.
	 */
	public final int nVertices; // number of vertices
	/**
	 * Representa as adjacencias de ca vertice do grafo, existe uma Bag para cada vertice.
	 */
	private Bag<Edge>[] adj; // one bag for each vertex
	/**
	 * Contem todas as Edges presentes no grafo, uma edge representa o caminho entre dois vertices do grafo.
	 */
	private Bag<Edge> edges; // bag of all edges
	
	/**
	 * Construtor da classe Graph.
	 * @param nVertices Numero de vertices presentes no grafo.
	 */
	@SuppressWarnings("unchecked")
	public Graph(int nVertices){
		this.nVertices = nVertices;
		this.edges = new Bag<>();
		this.adj = (Bag<Edge>[]) new Bag[nVertices];
		for (int i = 0; i < nVertices; i++){
		this.adj[i] = new Bag<Edge>();
		}
	}
	
	/**
	 * Permite adicionar uma Edge orientada ao grafo.
	 * @param x Edge a adicionar.
	 */
	public void addEdgeDirected(Edge x){
		this.edges.add(x);
		this.adj[x.getFrom()].add(x);
	}
	
	/**
	 * Permite adicionar uma Edge nao orientada ao grafo, como nao e orientado tambem e adicionado uma Edge com o caminho contrario.
	 * @param x Edge a adicionar.
	 */	
	public void addEdge(Edge x){
		addEdgeDirected(x);
		addEdgeDirected(x.reverse());
	}
	
	/**
	 * Permite obter as Edges de um grafo.
	 * @return Bag de Edges do grafo.
	 */
	public Iterable<Edge> edges(){
		return this.edges;
	}
		
	/**
	 * Permite obter os vertices adjacentes a um determinado vertice do grafo.
	 * @param v Vertice que se pretende verificar as adjacencias.
	 * @return Bag com as adjacencias de um determinado vertice.
	 */
	public Iterable<Edge> adj(int v){
		return this.adj[v];
	}
	
	/**
	 * Permite obter o numero de Edges que compoem o grafo.
	 * @return Numero de Edges de um grafo.
	 */
	public int numOfEdges() {return this.edges.size();}
	
	/**
	 * Metodo que permite obter os vertices adjacentes a um vertice do grafo.
	 * @return Bag que contem os vertices adjacentes a um determinado vertice do grafo.
	 */
	public Bag<Edge>[] getAdj(){
		return this.adj;
	}
}

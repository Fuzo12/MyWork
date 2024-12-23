import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Classe responsavel pelas operacoes sobre as tabelas, esta classe contem metodos que permitem:
 * 	-A criacao de uma tabela por meio de um ficheiro .csv;
 * 	-A conversao de uma tabela para uma String de forma a que seja possivel realizar um print dos conteudos da tabela;
 * 	-A verificacao de todas as combinacoes possiveis entre as linhas de duas tabelas e a similariedade entre o conteudo dessas linhas por meio da metrica escolhida;
 * 	-A juncao de duas tabelas, com base numa metrica. 
 * @author Claudio Coelho
 */
public class Table {
	/**
	 * Metodo responsavel pela criação de uma tabela por meio de um ficheiro .csv, sempre que o código deteta uma celula vazia o conteudo dela e substituido por "empty".
	 * @param filename Representa o nome do ficheiro que se pretende utilizar para criar uma tabela.
	 * @return Um ArrayList de HashMaps onde cada HashMap representa uma linha na tabela.
	 * @throws IOException Esta excecao e lancada quando o ficheiro com o nome inserido no parametro filename nao existe ou nao e encontrado.
	 */
	public static ArrayList<HashMap> fillTable(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		String[] keys = line.split(",");
		for(int i=0; i<keys.length;i++) {
			keys[i] = keys[i];
		}
		ArrayList<HashMap> table = new ArrayList<HashMap>();
		while((line = reader.readLine()) != null) {
			HashMap<String,String> map = new LinkedHashMap<String,String>();
			String[] values = line.replace(", "," ").split(",");
			for(int i=0;i<values.length;i++) {
				if(values[i].equals("")) {values[i]="empty";}
				map.put(keys[i], values[i]);
			}
			table.add(map);
		}
		return table;
	}
	
	/**
	 * Cria uma tabela que pode ser utilizada como filtro, a mesma só pode ter duas colunas.
	 * @param filename Ficheiro utilizado para criar a tabela.
	 * @return Tabela a ser utilizada como filtro.
	 * @throws NumberFormatException Excecao que surge caso determinados elementos da tabela utilizada como filtro nao puderem ser convertidas num valor numerico.
	 * @throws IOException Excecao que surge caso nao seja encontrado o ficheiro que cria a tabela a ser utilizada pelo filtro.
	 * @throws InvalidTimeException Excecao que surge caso a tabela de filtro tenha uma string a ser convertida em tempo crie uma hora invalida.
	 */
	public static LinkedHashMap<String,Time> createFilterTable(String filename) throws IOException, NumberFormatException, InvalidTimeException{
		LinkedHashMap<String,Time> filterTable = new LinkedHashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		while((line = reader.readLine()) != null) {
			String[] values = line.replace(", "," ").split(",");
			String[] time = values[1].split(":");
			filterTable.put(values[0],new Time(Integer.valueOf(time[0]),Integer.valueOf(time[1]),Integer.valueOf(time[2])));
		}
		return filterTable;
	}
	
	/**
	 * Converte os conteudos de uma tabela numa String de forma a que estes nao sejam apresentados no formato de ArrayList quando se realiza print da tabela.
	 * @param table Representa a tabela que se pretende converter em String.
	 * @return Uma String com os conteudos da tabela.
	 */
	public static String convertTable(ArrayList<HashMap> table) {
		String result = "";
		result += table.get(0).keySet().toString().replace("[","").replace("]","").replace(", ", ",") + "\n";
		for(int i=0;i<table.size();i++) {
			result += table.get(i).values().toString().replace("[","").replace("]","").replace(", ", ",");
			if(i != table.size()-1) {result += "\n";}
		}
		return result;
	}
	
	/**
	 * Metodo que permite obter uma linha de uma tabela.
	 * @param table Tabela a partir da qual se pretende obter uma linha.
	 * @param line Linha a obter.
	 * @return HashMap correspondente a linha da tabela.
	 */
	public static HashMap getLine(ArrayList<HashMap> table,int line) {
		String[] keys = table.get(line).keySet().toString().replace("[", "").replace("]", "").split(", ");
		String[] values = table.get(line).values().toString().replace("[", "").replace("]", "").split(", ");
		LinkedHashMap<String,String> result = new LinkedHashMap<>();
		for(int i=0; i<keys.length;i++) {
			result.put(keys[i],values[i]);
		}
		return result;
	}
	
	/**
	 * Metodo utilizado para ordernar a tabela segundo os dados de outra, a segunda tabela usa o tempo como unidade de medida.
	 * @param table Tabela a ser ordenada.
	 * @param filterFile Ficheiro associado a tabela que vai ser utilizada para ordenar a tabela principal.
	 * @return Tabela principal ordenada.
	 * @throws NumberFormatException Excecao que surge caso determinados elementos da tabela utilizada como filtro nao puderem ser convertidas num valor numerico.
	 * @throws IOException Excecao que surge caso nao seja encontrado o ficheiro que cria a tabela a ser utilizada pelo filtro.
	 * @throws InvalidTimeException Excecao que surge caso a tabela de filtro tenha uma string a ser convertida em tempo crie uma hora invalida.
	 */
	public static ArrayList<HashMap> sortTable(ArrayList<HashMap> table, String filterFile) throws NumberFormatException, IOException, InvalidTimeException{
		LinkedHashMap<String,Time> filterTable = createFilterTable(filterFile);
		String[] keysTable = table.get(0).keySet().toString().replace("[", "").replace("]","").split(", ");
		String[] keysFilter = filterTable.keySet().toString().replace("[", "").replace("]","").split(", ");
		for(int i=0;i<filterTable.size();i++) {
			for(int j=0;j<filterTable.size()-1;j++) {
				if(!filterTable.get(keysFilter[i]).compareTime(filterTable.get(keysFilter[j]))) {
					String temp = keysFilter[i];
					keysFilter[i] = keysFilter[j];
					keysFilter[j] = temp;
				}
			}
		}
		for(int i = 0; i<keysFilter.length;i++) {
			keysTable[i+1] = keysFilter[i];
		}
		ArrayList<HashMap> tempTable = new ArrayList<>();
		for(int i = 0; i < table.size();i++) {
			LinkedHashMap<String,String> map = new LinkedHashMap<>();
			for(int j=0; j<keysTable.length;j++) {
				map.put(keysTable[j],table.get(i).get(keysTable[j]).toString());
			}
			tempTable.add(map);
		}
		
		ArrayList<HashMap> sortedTable = new ArrayList<>();
		
		for(int i = 1; i<keysTable.length;i++) {
			for(int j=0;j<tempTable.size();j++) {
				if(tempTable.get(j).get(keysTable[0]).equals(keysTable[i])) {
					sortedTable.add(tempTable.get(j));
				}
			}
		}
		return sortedTable;
	}
	
	/**
	 * Converte uma tabela num grafo orientado, utilizando uma segunda tabela como filtro para determinar a orientacao do caminho.
	 * Segundo o contexto do problema a orientacao do grafo e determinado do original para a copia.
	 * @param table Tabela a ser convertida em grafo.
	 * @param filterFile Tabela a ser utilizada como filtro.
	 * @param threshold No contexto do problema, representa o limite da metrica presente na tabela a partir do qual os valores sao considerados.
	 * @return Grafo criado a partir da tabela.
	 * @throws NumberFormatException Excecao que surge caso determinados elementos da tabela utilizada como filtro nao puderem ser convertidas num valor numerico.
	 * @throws IOException Excecao que surge caso nao seja encontrado o ficheiro que cria a tabela a ser utilizada pelo filtro.
	 * @throws InvalidTimeException Excecao que surge caso a tabela de filtro tenha uma string a ser convertida em tempo crie uma hora invalida.
	 */
	//Converte a tabela num grafo orientado, utilizando um filtro para determinar a orientação do caminho
	//A orientação do grafo é do original para a cópia
	public static Graph toGraph(ArrayList<HashMap> table, String filterFile, int threshold) throws IOException, NumberFormatException, InvalidTimeException {
			table = sortTable(table,filterFile);
			BufferedReader reader = new BufferedReader(new FileReader(filterFile));
			String line = reader.readLine();
		LinkedHashMap<String,Time> filterTable = createFilterTable(filterFile);
		Graph grafo = new Graph(table.get(0).keySet().size()-1);
		String[] keysTable = table.get(0).keySet().toString().replace("[", "").replace("]", "").split(", ");
		String[] vName = new String[table.get(0).keySet().size()-1];
		for(int i=0;i<vName.length;i++) {
			vName[i] = keysTable[i+1];
		}
		int counter = 1;
		for(HashMap i : table) {
			for(int j=counter; j<keysTable.length; j++) {
				if(Integer.valueOf((i.get(keysTable[j])).toString()) >= threshold) {
					String v1 = i.get(keysTable[0]).toString();
					String v2 = keysTable[j];
					int from = counter;
					int to = j;
					String temp = "";
					if(!filterTable.get(v1).compareTime(filterTable.get(v2))){
						from = counter-1;
						to = j-1;
						
					}else{
						from = j-1;
						to = counter-1;
						temp = v1;
						v1 = v2;
						v2 = temp;
					}
					double cost = Double.valueOf(i.get(keysTable[j]).toString());
					grafo.addEdgeDirected(new Edge(from,to,cost,v1,v2));
				}
			}
			counter++;
		}
		return grafo;
	}
	
	/**
	 * Metodo utilizado para criar grupos de semelhanca a partir de um grafo, dos grupos definidos aqueles que estejam contidos dentro de outros sao considerados redundantes e removidos dos ArrayList.
	 * @param grafo Grafo a ser utilizado para determinar os grupos.
	 * @return ArrayList de ArrayLists que determina os grupos de semelhanca existententes.
	 */
	public static ArrayList<ArrayList> findGroups(Graph grafo){
		 ArrayList<ArrayList> groups = new ArrayList<>();
		 for(int i = 0; i<grafo.nVertices;i++) {
			 ArrayList<String> group = new ArrayList<>();
			 for(Edge j : grafo.adj(i)) {
				 group.add(j.getNameTo());
			 }
			 groups.add(group);
		 }
		 for(int i = 0;i<groups.size();i++) {
			 for(int j = 0; j<groups.size();j++) {
				 if(groups.get(i).equals(groups.get(j))) {continue;}
				 ArrayList<String> iTemp = new ArrayList<>();
				 ArrayList<String> jTemp = new ArrayList<>();
				 if(groups.get(i).size()>groups.get(j).size()) {
					 iTemp = groups.get(i);
					 jTemp = groups.get(j);
					 if(iTemp.containsAll(jTemp)) {groups.remove(j);i=0;}
				 }else{
					 iTemp = groups.get(i);
					 jTemp = groups.get(j);
					 if(jTemp.containsAll(iTemp)) {groups.remove(i);i=0;}
				 }
			 }
		 }
		 return groups;
	}
	
	/**
	 * No contexto do problema determina a percentagem de um grupo de semelhanca existir, fazendo a media entre os valores de cada grupo segundo os valores presentes na tabela.
	 * @param table Tabela a ser utilizada.
	 * @param group Grupo a ser considerado.
	 * @return Percentagem do grupo existir.
	 */
	public static double groupProbability(ArrayList<HashMap> table, ArrayList<String> group){
		double probability = 0;
		String[] keysTable = table.get(0).keySet().toString().replace("]", "").replace("[", "").replace(" ", "").split(",");
		HashMap<String,String> line = new HashMap<>();
		for(int i=0;i<table.size();i++) {
			if(table.get(i).get(keysTable[0]).toString().equals(group.get(0))) {
				line = table.get(i);
			}
		}
		for(int i=0;i<group.size();i++) {
			probability += Double.valueOf(line.get(group.get(i)));
		}
		return (probability/(double)group.size());
	}
				
	/**
	 * Converte os grupos encontrados numa tabela e da probabilidade de existencia desses grupos numa String.
	 * @param table Tabela a ser utilizada.
	 * @param list	Grupos a serem imprimidos tabela.
	 * @return String que contem os grupos de semelhanca e percentagem de existencia de cada grupo.
	 */
	public static String convertGroups(ArrayList<HashMap> table, ArrayList<ArrayList> list) {
		String result = "";
		for(int i=0; i<list.size();i++) {
			double probability = groupProbability(table,list.get(i)); 
			result += i + ": " + list.get(i).toString().replace(",", "").replace("[","").replace("]", "") + " (" + new DecimalFormat("0.00").format(probability)  +"%)";
			if(i<list.size()-1) {result += "\n";}
		}
		return result;
	}

}

//package aed_TP1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
 *
 */
public class Table {
	/**
	 * Metodo responsavel pela criação de uma tabela por meio de um ficheiro .csv, sempre que o código deteta uma celula vazia o conteudo dela e substituido por "empty".
	 * @param filename Representa o nome do ficheiro que se pretende utilizar para criar uma tabela.
	 * @return Um ArrayList de HashMaps onde cada HashMap representa uma linha na tabela.
	 * @throws IOException Esta excecao e lancada quando o ficheiro com o nome inserido no parametro filename nao existe ou nao e encontrado.
	 */
	public static ArrayList<HashMap> fillTable(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String[] fileInfo = filename.split(".");
		String line = reader.readLine();
		String[] keys = line.split(",");
		for(int i=0; i<keys.length;i++) {
			keys[i] = keys[i] + "." + filename.substring(0, filename.lastIndexOf("."));
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
	 * Cria uma tabela que mostra todas as combinacoes possiveis entre os elementos das colunas de duas tabelas.
	 * @param table1 Representa a primeira tabela a ser utilizada para a comparacao.
	 * @param name1 Representa o nome da coluna da primeira tabela a ser utilizado para a comparacao.
	 * @param table2 Representa a segunda tabela a ser utilizada para a comparacao.
	 * @param name2 Representa o nome da coluna da segunda tabela a ser utilizado para a comparacao.
	 * @param metric Representa a metrica a ser utilizada para realizar as comparacoes, neste programa sao apenas suportadas as metricas Levenshtein, Hamming, Jaccard, Cosine.
	 * @return Uma tabela com todas as combinacoes possiveis entre os elementos de uma coluna de cada tabela, cada combinacao e seguida do valor da metrica obtido entre os elementos de cada comparacao.
	 * @throws InvalidMetricException Esta excecao surge quando e selecionada uma metrica invalida para realizar a comparacao entre os elementos das colunas das duas tabelas.
	 */
	public static ArrayList<HashMap> combinations(ArrayList<HashMap> table1, String name1, ArrayList<HashMap> table2, String name2, String metric) throws Exception{
		ArrayList<HashMap> combinationTable = new ArrayList<HashMap>();
		String[] arr1 = table1.get(0).keySet().toString().replace("[","").replace("]","").replace(", ",",").split(",");
		String[] arr2 = table2.get(0).keySet().toString().replace("[","").replace("]","").replace(", ",",").split(",");
		arr1[0] = arr1[0].substring(arr1[0].lastIndexOf("."), arr1[0].length());
		arr2[0] = arr2[0].substring(arr2[0].lastIndexOf("."), arr2[0].length());
		name1=name1+arr1[0];
		name2=name2+arr2[0];
		for(int i=0; i<table1.size();i++) {
			for(int j=0;j<table2.size();j++) {
				LinkedHashMap<String,String> line = new LinkedHashMap<String,String>();
				line.put(name1, table1.get(i).get(name1).toString());
				line.put(name2, table2.get(j).get(name2).toString());
				switch(metric.toLowerCase()){
					case "levenshtein":
						line.put("Levenshtein", String.valueOf( Levenshtein.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) ));
						break;
					case "hamming":
						line.put("Hamming", String.valueOf( Hamming.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) ));
						break;	
					case "jaccard":
						line.put("Jaccard", String.valueOf( Jaccard.jaccardSimilarity(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) ));
						break;
					case "cosine":
						line.put("Cosine", String.valueOf( Cosine.cosineSimilarity(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) ));
						break;
					default:
						throw new InvalidMetricException("Métrica Inválida");
				}
				combinationTable.add(line);
			}	
		}
		return combinationTable;
	}
	
	/**
	 * Realiza a juncao entre duas tabelas, esta juncao e realizada com base no valor obtido atraves da comparacao dos elementos presentes em uma coluna de cada tabela.
	 * Este metodo cria uma tabela intermedia que guarda o maior resultado obtido para os elementos da coluna de primeira tabela.
	 * Quando preenche a tabela final este metodo deixa de parte todas as combinacoes em que o valor maximo da metrica da coluna da primeira tabela e zero.
	 * @param table1 Representa a primeira tabela a ser utilizada para a juncao.
	 * @param name1 Representa o nome da coluna da primeira tabela a ser utilizada para definir que linhas devem ser emparelhadas.
	 * @param table2 Representa a segunda tabela a ser utilizada para a juncao.
	 * @param name2 Representa o nome da coluna da segunda tabela a ser utilizada para definir que linhas devem ser emparelhadas..
	 * @param metric Representa a metrica a ser utilizada para a juncao das tabelas, este metodo suporta as metricas Levenshtein, Hamming, Jaccard e Cosine.
	 * @return A juncao das duas primeiras tabelas, segundo a metrica selecionada.
	 * @throws HammingException Esta excecao e lancada quando as String que estao a ser comparadas na cumprem as normas da metrica de Hamming.
	 * @throws InvalidMetricException Esta excecao e lancada caso o utilizador selecione uma metrica que nao e suportada por este metodo.
	 */
	public static ArrayList<HashMap> mergeTable(ArrayList<HashMap> table1, String name1, ArrayList<HashMap> table2, String name2, String metric) throws HammingException, InvalidMetricException {
		ArrayList<HashMap> table3 = new ArrayList<HashMap>();
		ArrayList<HashMap> middleTable = new ArrayList<HashMap>();
		String[] arr1 = table1.get(0).keySet().toString().replace("[","").replace("]","").replace(", ",",").split(",");
		String[] arr2 = table2.get(0).keySet().toString().replace("[","").replace("]","").replace(", ",",").split(",");
		arr1[0] = arr1[0].substring(arr1[0].lastIndexOf("."), arr1[0].length());
		arr2[0] = arr2[0].substring(arr2[0].lastIndexOf("."), arr2[0].length());
		name1=name1+arr1[0];
		name2=name2+arr2[0];
		for(int i=0; i<table1.size();i++) {
			int result = 0;
			LinkedHashMap<String,String> line = new LinkedHashMap<String,String>(); //O resultado da métrica só vai para o fim com LinkedHashMap
			
			for(int j=0;j<table2.size();j++) {
				switch(metric.toLowerCase()) {
				case "levenshtein":
					if(Levenshtein.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) > result) {
						result = Levenshtein.ration(table1.get(i).get(name1).toString(), table2.get(j).get(name2).toString());
					}
					break;
				case "hamming":
					if(Hamming.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase()) > result) {
						result = Hamming.ration(table1.get(i).get(name1).toString(), table2.get(j).get(name2).toString());
					}
					break;
				case "jaccard":
					if(Jaccard.jaccardSimilarity(table1.get(i).get(name1).toString(), table2.get(j).get(name2).toString()) > result) {
						result = Jaccard.jaccardSimilarity(table1.get(i).get(name1).toString(), table2.get(j).get(name2).toString());
					}
					break;
				case "cosine":
					if(Jaccard.jaccardSimilarity(table1.get(i).get(name1).toString(), table2.get(j).get(name2).toString()) > result) {
						result = Cosine.cosineSimilarity(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase());
					}
					break;
				default:
					throw new InvalidMetricException("Métrica inválida");
				}
			}
				line.put(name1, table1.get(i).get(name1).toString());
				line.put("Metric", String.valueOf(result));
				middleTable.add(line);
		}
		for(int i=0; i<table1.size();i++) {
			int result = 0;
			for(int j=0;j<table2.size();j++) {
				switch(metric.toLowerCase()) {
				case "levenshtein":
					result = Levenshtein.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase());
					break;
				case "hamming":
					result = Hamming.ration(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase());
					break;
				case "jaccard":
					result = Jaccard.jaccardSimilarity(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase());
					break;
				case "cosine":
					result = Cosine.cosineSimilarity(table1.get(i).get(name1).toString().toLowerCase(), table2.get(j).get(name2).toString().toLowerCase());
					break;
				default:
					throw new InvalidMetricException("Métrica inválida");
				}
				LinkedHashMap<String,String> line = new LinkedHashMap<String,String>(); //O resultado da métrica só vai para o fim com LinkedHashMap
				if((table1.get(i).get(name1).toString().toLowerCase().equals(middleTable.get(i).get(name1).toString().toLowerCase()))
					&&(result) == Integer.parseInt(middleTable.get(i).get("Metric").toString()) && result>0) {
					line.putAll(table1.get(i));
					line.putAll(table2.get(j));
					switch(metric.toLowerCase()) {
					case "levenshtein":
						line.put("Levenshtein", middleTable.get(i).get("Metric").toString());
						break;
					case "hamming":
						line.put("Hamming", middleTable.get(i).get("Metric").toString());
						break;
					case "jaccard":
						line.put("Jaccard", middleTable.get(i).get("Metric").toString());
						break;
					case "cosine":
						line.put("Cosine", middleTable.get(i).get("Metric").toString());
						break;
					default:
						throw new InvalidMetricException("Métrica inválida");
					}
					table3.add(line);
				}	
			}
		}
		return table3;
	}
}

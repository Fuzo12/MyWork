//package aed_TP1;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class Client {

	/*
	Args[0] = first table name
	Args[1] = second table name
	Args[2] = column name
	Args[3] = column name
	Args[4] = metric name

	input example: tabela1.csv tabela2.csv country country levenshtein
	 */
	public static void main (String[] args) throws Exception {
		ArrayList<HashMap> table1 = new ArrayList<HashMap>();
		table1 = Table.fillTable(args[0]);
		ArrayList<HashMap> table2 = new ArrayList<HashMap>();
		table2 = Table.fillTable(args[1]);
		ArrayList<HashMap> table3 = new ArrayList<HashMap>();
		System.out.println(Table.convertTable(table1));
		System.out.println();
		System.out.println(Table.convertTable(table2));
		table3 = Table.mergeTable(table1,args[2].replace("_", " "),table2,args[3].replace("_", " "),args[4]);
		System.out.println("\nfuzzy-join --filename1="+ args[0] +" --filename2=" + args[1] + " --name1=" + args[2] + " --name2=" + args[3] + " --distance=" + args[4].toLowerCase());
		System.out.println();
		System.out.println(Table.convertTable(table3));
		System.out.println();
		File file = new File("resultado.csv");
		file.delete();
		file.createNewFile();
		FileWriter text = new FileWriter("resultado.csv");
		text.write(Table.convertTable(table3));
		text.close();
	}
}

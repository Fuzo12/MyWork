import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {

	/*
		Arg[0] = table file name
		Arg[1] = time file name
		Arg[2] = treshold value

		Example: table1.csv time1.csv 80

	*/
 public static void main(String[] args) throws IOException, NumberFormatException, InvalidTimeException {
	 ArrayList<HashMap> table1 = Table.fillTable(args[0]);
	 System.out.println(Table.convertTable(table1));
	 System.out.println();
	 ArrayList<HashMap> table2 = Table.fillTable(args[1]);
	 System.out.println(Table.convertTable(table2));
	 Graph grafo = Table.toGraph(table1,args[1],Integer.valueOf(args[2]));
	 System.out.println();
	 System.out.println("copy-detection --correlation="+args[0]+" --time="+args[1]+" --threshold="+args[2]);
	 System.out.println();
	 ArrayList<ArrayList> groups = Table.findGroups(grafo);
	 System.out.println(Table.convertGroups(table1,groups));
 }
}

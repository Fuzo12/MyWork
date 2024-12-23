import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class TableTest {

	@Test
	public void testFillTable() throws IOException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		assertEquals(Table.convertTable(table),"row_0,S100,S115,S131,S31,S32\n"
											+ "S100,100,98,76,99,99\n"
											+ "S115,98,100,72,97,97\n"
											+ "S131,76,72,100,75,75\n"
											+ "S31,99,97,75,100,99\n"
											+ "S32,99,97,75,99,100");
	}

	@Test
	public void testCreateFilterTable() throws NumberFormatException, IOException, InvalidTimeException {
		HashMap<String,Time> table = Table.createFilterTable("time.csv");
		String[] keys = table.keySet().toString().replace("[", "").replace("]","").split(", ");
		String[] values = table.values().toString().replace("[", "").replace("]","").split(", ");
		for(int i=0;i<keys.length;i++) {
			assertEquals(table.get(keys[i]).toString(),values[i]);
		}
	}

	@Test
	public void testConvertTable() throws IOException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		assertEquals(Table.convertTable(table),"row_0,S100,S115,S131,S31,S32\n"
											+ "S100,100,98,76,99,99\n"
											+ "S115,98,100,72,97,97\n"
											+ "S131,76,72,100,75,75\n"
											+ "S31,99,97,75,100,99\n"
											+ "S32,99,97,75,99,100");
	}

	@Test
	public void testGetLine() throws IOException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		HashMap<String,String> line = Table.getLine(table, 0);
		assertEquals(line.toString(),"{row_0=S100, S100=100, S115=98, S131=76, S31=99, S32=99}");
	}

	@Test
	public void testSortTable() throws IOException, NumberFormatException, InvalidTimeException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		table = Table.sortTable(table, "time.csv");
		assertEquals(Table.convertTable(table),"row_0,S31,S32,S100,S115,S131\n"
						+ "S31,100,99,99,97,75\n"
						+ "S32,99,100,99,97,75\n"
						+ "S100,99,99,100,98,76\n"
						+ "S115,97,97,98,100,72\n"
						+ "S131,75,75,76,72,100");
	}

	@Test
	public void testToGraph() throws IOException, NumberFormatException, InvalidTimeException, InterruptedException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		Graph graph = Table.toGraph(table, "time.csv", 90);
		Graph expected = new Graph(5);
		expected.addEdgeDirected(new Edge(0,0,100,"S31","S31"));
		expected.addEdgeDirected(new Edge(0,1,99,"S31","S32"));
		expected.addEdgeDirected(new Edge(0,2,99,"S31","S100"));
		expected.addEdgeDirected(new Edge(0,3,97,"S31","S115"));
	
		expected.addEdgeDirected(new Edge(1,1,100,"S32","S32"));
		expected.addEdgeDirected(new Edge(1,2,99,"S32","S100"));
		expected.addEdgeDirected(new Edge(1,3,97,"S32","S115"));
		
		expected.addEdgeDirected(new Edge(2,2,100,"S100","S100"));
		expected.addEdgeDirected(new Edge(2,3,98,"S100","S115"));
		
		expected.addEdgeDirected(new Edge(3,3,100,"S115","S115"));
		
		expected.addEdgeDirected(new Edge(4,4,100,"S131","S131"));
		
		for(int i=0;i<graph.nVertices;i++) {
			assertEquals(graph.adj(i).toString(),expected.adj(i).toString());
		}
		assertEquals(graph.nVertices,5);

	}

	@Test
	public void testFindGroups() throws IOException, NumberFormatException, InvalidTimeException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		Graph graph = Table.toGraph(table, "time.csv", 80);
		assertEquals(Table.findGroups(graph).toString(),"[[S31, S32, S100, S115], [S131]]");
	}

	@Test
	public void testGroupProbability() throws IOException, NumberFormatException, InvalidTimeException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		Graph graph = Table.toGraph(table, "time.csv", 80);
		ArrayList<ArrayList> group = Table.findGroups(graph);
		assertEquals((int)Table.groupProbability(table,group.get(0)),(99+97+99+100)/(4));
		assertEquals((int)Table.groupProbability(table,group.get(1)),(100)/(1));
	}

	@Test
	public void testConvertGroups() throws IOException, NumberFormatException, InvalidTimeException {
		ArrayList<HashMap> table = Table.fillTable("SimpleTable.csv");
		Graph graph = Table.toGraph(table, "time.csv", 80);
		ArrayList<ArrayList> group = Table.findGroups(graph);
		assertEquals(Table.convertGroups(table, group),"0: S31 S32 S100 S115 (98,75%)\n"
					+ "1: S131 (100,00%)");
	}

}

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testGraph() {
		Graph graph = new Graph(3);
		assertEquals(graph.nVertices,3);
	}

	@Test
	public void testAddEdgeDirected() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdgeDirected(edge[i]);
		}
		int j=0;
		for(Edge i : graph.edges()) {
			assertEquals(i,edge[j]);
			j++;
		}
	}

	@Test
	public void testAddEdge() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdge(edge[i]);
		}
		int j=0;
		int k=0;
		for(Edge i : graph.edges()) {
			if(k==0) {assertEquals(i.toString(),edge[j].toString());k++;}
			else {assertEquals(i.toString(),edge[j].reverse().toString());k=0;j++;}
		}
	}

	@Test
	public void testEdges() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdge(edge[i]);
		}
		int j=0;
		int k=0;
		for(Edge i : graph.edges()) {
			if(k==0) {assertEquals(i.toString(),edge[j].toString());k++;}
			else {assertEquals(i.toString(),edge[j].reverse().toString());k=0;j++;}
		}
	}

	@Test
	public void testAdj() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdge(edge[i]);
		}
		int j=0;
		for(Edge i:graph.adj(1)) {
			j++;
		}
		assertEquals(j,2);
	}

	@Test
	public void testNumOfEdges() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdge(edge[i]);
		}
		assertEquals(graph.numOfEdges(),6);
	}

	@Test
	public void testGetAdj() {
		Graph graph = new Graph(3);
		Edge[] edge = {new Edge(0,1,1,"0","1"),new Edge(1,2,1,"1","2"),new Edge(0,2,1,"0","2")};
		for(int i=0; i<edge.length; i++) {
			graph.addEdge(edge[i]);
		}
		Bag<Edge>[] bag = graph.getAdj();
		/*for(int i=0;i<bag.length;i++) {
			System.out.println(i);
			for(Edge j: bag[i]) {
				System.out.println(j);
			}
		}*/
		for(int i=0;i<bag.length;i++) {
			int j=0;
			for(Edge k:graph.adj(i)) {j++;}
			assertEquals(bag[i].size(),j);
		}
		
	}

}

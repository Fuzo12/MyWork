import static org.junit.Assert.*;

import org.junit.Test;

public class EdgeTest {

	@Test
	public void testEdgeIntIntDoubleStringString() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.getFrom(),0);
		assertEquals(edge.getTo(),1);
		assertEquals((int) edge.getWeight(),(int) 1.0);
		assertEquals(edge.getNameFrom(),"0");
		assertEquals(edge.getNameTo(),"1");
	}

	@Test
	public void testEdgeIntIntStringString() {
		Edge edge = new Edge(0,1,"0","1");
		assertEquals(edge.getFrom(),0);
		assertEquals(edge.getTo(),1);
		assertEquals((int) edge.getWeight(),(int) 1.0);
		assertEquals(edge.getNameFrom(),"0");
		assertEquals(edge.getNameTo(),"1");
	}

	@Test
	public void testReverse() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.reverse().getFrom(),1);
		assertEquals(edge.reverse().getTo(),0);
		assertEquals((int) edge.reverse().getWeight(),(int) 1.0);
		assertEquals(edge.reverse().getNameFrom(),"1");
		assertEquals(edge.reverse().getNameTo(),"0");
	}

	@Test
	public void testCompareByWeight() {
		Edge edge1 = new Edge(0,1,1.0,"0","1");
		Edge edge2 = new Edge(0,1,2.0,"0","1");
		assertEquals(Edge.compareByWeight(edge1, edge1),0);
		assertEquals(Edge.compareByWeight(edge2, edge1),1);
		assertEquals(Edge.compareByWeight(edge1, edge2),-1);
	}

	@Test
	public void testGetFrom() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.getFrom(),0);
	}

	@Test
	public void testGetTo() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.getTo(),1);
	}

	@Test
	public void testGetWeight() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals((int)edge.getWeight(),(int)1.0);
	}

	@Test
	public void testGetNameFrom() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.getNameFrom(),"0");
	}

	@Test
	public void testGetNameTo() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.getNameTo(),"1");
	}

	@Test
	public void testToString() {
		Edge edge = new Edge(0,1,1.0,"0","1");
		assertEquals(edge.toString(),"0 1 1.0");
	}

}

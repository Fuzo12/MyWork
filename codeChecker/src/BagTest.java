import static org.junit.Assert.*;

import org.junit.Test;

public class BagTest {

	@Test
	public void testBag() {
		Bag<Integer> bag = new Bag<>();
		assertEquals(bag.size(),0);
		assertEquals(bag.get(0),null);
	}

	@Test
	public void testAdd() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		assertEquals(bag.size(),1);
		assertEquals((int)bag.get(0),2);
		bag.add(15);
		assertEquals(bag.size(),2);
		assertEquals((int)bag.get(1),15);
	}

	@Test
	public void testGet() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		bag.add(15);
		assertEquals((int)bag.get(0),2);
		assertEquals((int)bag.get(1),15);
	}

	@Test
	public void testSize() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		bag.add(15);
		assertEquals(bag.size(),2);
	}

	@Test
	public void testIsEmpty() {
		Bag<Integer> bag = new Bag<>();
		assertEquals(bag.isEmpty(),true);
		bag.add(2);
		assertEquals(bag.isEmpty(),false);
	}

	@Test
	public void testRefill() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		bag.add(15);
		bag.add(5);
		Integer[] items = {3,6,9};
		bag.refill(items);
		assertEquals((int)bag.get(0),3);
		assertEquals((int)bag.get(1),6);
		assertEquals((int)bag.get(2),9);
	}

	@Test
	public void testIterator() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		bag.add(15);
		bag.add(5);
		int[] items = {2,15,5};
		int j=0;
		for(Integer i:bag) {
			assertEquals((int)i,items[j]);
			j++;
		}
	}
	
	@Test
	public void testToString() {
		Bag<Integer> bag = new Bag<>();
		bag.add(2);
		bag.add(15);
		bag.add(5);
		assertEquals(bag.toString(),"[2,15,5]");
	}

}

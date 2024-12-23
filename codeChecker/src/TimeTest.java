import static org.junit.Assert.*;

import org.junit.Test;

public class TimeTest {

	@Test
	public void testTime() throws InvalidTimeException {
		Time hour=new Time(10,10,0);
		try {
			Time hourInvalidHour1=new Time(-10,0,0);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		try {
			Time hourInvalidHour2=new Time(24,0,0);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		
		try {
			Time hourInvalidMinute1=new Time(10,-10,0);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		try {
			Time hourInvalidMinute2=new Time(10,60,0);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		
		try {
			Time hourInvalidSecond1=new Time(10,10,-10);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		try {
			Time hourInvalidSecond2=new Time(10,10,60);
		} catch (InvalidTimeException e) {
			assertTrue(true);
		}
		assertEquals(hour.toString(),"10:10:00");
	}

	@Test
	public void testConvertString() throws NumberFormatException, InvalidTimeException {
		String hour = "10:15:00";
		assertEquals(Time.convertString(hour).toString(),hour);
	}

	@Test
	public void testToString() throws InvalidTimeException {
		Time hour1 = new Time(1,1,1);
		assertEquals(hour1.toString(),"01:01:01");
		Time hour2 = new Time(10,10,10);
		assertEquals(hour2.toString(),"10:10:10");
	}

	@Test
	public void testCompareTime() throws InvalidTimeException {
		Time hour1 = new Time(5,5,0);
		Time hour2 = new Time(5,5,10);
		Time hour3 = new Time(5,10,0);
		Time hour4 = new Time(10,5,0);
		assertEquals(hour1.compareTime(hour2), false);
		assertEquals(hour2.compareTime(hour1), true);
		assertEquals(hour2.compareTime(hour3), false);
		assertEquals(hour3.compareTime(hour2), true);
		assertEquals(hour1.compareTime(hour4), false);
		assertEquals(hour4.compareTime(hour1), true);
	}

}

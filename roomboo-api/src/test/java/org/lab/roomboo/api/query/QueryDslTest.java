package org.lab.roomboo.api.query;

import org.junit.Assert;
import org.junit.Test;

public class QueryDslTest {

	@Test
	public void testString() {
		BasicQueryDsl query = new BasicQueryDsl("key==value");
		String test = query.get("key");
		Assert.assertEquals("value", test);
	}
	
	@Test
	public void testStringMultiple() {
		BasicQueryDsl query = new BasicQueryDsl("key1==value1;key2==value2");
		String value1 = query.get("key1");
		String value2 = query.get("key2");
		Assert.assertEquals("value1", value1);
		Assert.assertEquals("value2", value2);
	}
	
	@Test
	public void testStringEmpty() {
		BasicQueryDsl query = new BasicQueryDsl("key==");
		String test = query.get("key");
		Assert.assertNull("value", test);
	}

	@Test
	public void testBooleanNull() {
		BasicQueryDsl query = new BasicQueryDsl("");
		Boolean test = query.get("key", Boolean.class);
		Assert.assertNull(test);
	}

	@Test
	public void testBooleanTrue() {
		BasicQueryDsl query = new BasicQueryDsl("key==true");
		Boolean test = query.get("key", Boolean.class);
		Assert.assertTrue(test);
	}

}

package org.lab.roomboo.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.api.config.RoombooConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoombooApplicationTests {

	@Autowired
	private RoombooConfig config;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(config);
	}

}

package org.lab.roomboo.mail;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooMailTestConfig.class, properties = "app.env.mail.enabled=false")
public class JavaMailSenderDisabledTest {

	@Autowired(required = false)
	private JavaMailSender sender;

	@Test
	public void test() {
		Assert.assertNull(sender);
	}

}

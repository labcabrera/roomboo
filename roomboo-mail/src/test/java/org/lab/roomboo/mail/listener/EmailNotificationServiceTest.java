package org.lab.roomboo.mail.listener;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.event.ReserveCreatedEvent;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.mail.RoombooMailTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooMailTestConfig.class)
@ActiveProfiles("active")
public class EmailNotificationServiceTest {

	@Rule
	public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.ALL);

	@Autowired
	private ReserveCreatedEmailListener service;

	@Test
	public void test() throws MessagingException {
		ServerSetup config = new ServerSetup(9999, "localhost", "smtp");
		GreenMail greenMail = new GreenMail(config);
		greenMail.start();

		Reserve reserve = Reserve.builder() //@formatter:off
			.id("12345")
			.name("Dummy reserve name")
			.code("ABCDEF")
			.from(LocalDateTime.now())
			.to(LocalDateTime.now().plusHours(1))
			.user(AppUser.builder().id("1").name("Name").lastName("Lastname").build())
			.build(); //@formatter:on

		service.onApplicationEvent(new ReserveCreatedEvent(this, reserve));
		greenMail.waitForIncomingEmail(1);

		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		Assert.assertEquals(receivedMessages.length, 1);
		MimeMessage received = receivedMessages[0];

		Assert.assertEquals("Roomboo verification code", received.getSubject());

		greenMail.stop();
	}

}

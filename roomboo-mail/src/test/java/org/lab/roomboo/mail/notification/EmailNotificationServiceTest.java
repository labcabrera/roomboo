package org.lab.roomboo.mail.notification;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.mail.RoombooMailTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooMailTestConfig.class)
@ActiveProfiles("active")
public class EmailNotificationServiceTest {

	@Rule
	public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.ALL);

	@Autowired
	private EmailNotificationService service;

	@Test
	public void test() throws MessagingException {
		ServerSetup config = new ServerSetup(9999, "localhost", "smtp");
		GreenMail greenMail = new GreenMail(config);
		greenMail.start();

		//@formatter:off
		Reserve reserve = Reserve.builder()
			.id("12345")
			.name("Dummy reserve name")
			.code("ABCDEF")
			.from(LocalDateTime.now())
			.to(LocalDateTime.now().plusHours(1))
			.owner(ReserveOwner.builder().id("1").build())
			.build();
		//@formatter:on

		service.reserveCreated(reserve);

		greenMail.waitForIncomingEmail(1);

		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		Assert.assertEquals(receivedMessages.length, 1);
		MimeMessage received = receivedMessages[0];

		Assert.assertEquals("Roomboo verification code", received.getSubject());

		greenMail.stop();
	}

}

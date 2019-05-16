package org.lab.roomboo.core.integration.handler;

import javax.mail.internet.MimeMessage;

import org.lab.roomboo.core.model.MailMessage;
import org.lab.roomboo.domain.exception.RoombooException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailSenderHandler implements GenericHandler<MailMessage> {

	@Autowired(required = false)
	protected JavaMailSender sender;

	@Override
	public Object handle(MailMessage payload, MessageHeaders headers) {
		if (sender == null) {
			// TODO generate alert
			log.warn("Email sender is not configured");
		}
		else {
			log.debug("Sending mail {}", payload);
			try {
				MimeMessage mimeMessage = sender.createMimeMessage();
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setTo((String[]) payload.getRecipients().toArray(new String[0]));
				message.setSubject(payload.getSubject());
				message.setText(payload.getBody(), true);
				sender.send(mimeMessage);
			}
			catch (Exception ex) {
				throw new RoombooException("Email error", ex);
			}
		}
		return payload;
	}

}

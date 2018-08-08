package org.lab.roomboo.core.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = "subject")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailMessage {

	private String subject;
	private String body;
	private List<String> recipients;

}

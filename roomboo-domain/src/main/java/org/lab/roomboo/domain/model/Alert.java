package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "alerts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

	public enum AlertType {
		INFO, WARNING, ERROR
	}

	private String id;

	private AlertType alertType;

	private String subject;

	private String message;

	private List<String> destinations;

	private LocalDateTime created;

	private LocalDateTime readed;
}

package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "reserves")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserve {

	private String id;

	private String name;

	@DBRef
	private Room room;

	@DBRef
	private ReserveOwner owner;

	private LocalDateTime from;

	private LocalDateTime to;

	private Boolean confirmed;

	private String code;

}

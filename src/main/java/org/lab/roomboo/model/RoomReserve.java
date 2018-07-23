package org.lab.roomboo.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

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
public class RoomReserve {

	private String id;

	@DBRef
	@NotNull
	private Room room;

	@NotNull
	@DBRef
	private ReserveOwner owner;

	private LocalDateTime from;

	private LocalDateTime to;

	private Boolean confirmed;

	private String code;

}

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

@Document(collection = "rooms")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

	private String id;

	private String name;

	@DBRef
	private RoomGroup group;

	private RoomFeatures features;

	private LocalDateTime locked;

}

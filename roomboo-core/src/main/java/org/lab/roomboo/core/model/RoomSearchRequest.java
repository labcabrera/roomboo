package org.lab.roomboo.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomSearchRequest {

	private String groupId;

	private Integer minSize;

	private Boolean videoRequired;

}

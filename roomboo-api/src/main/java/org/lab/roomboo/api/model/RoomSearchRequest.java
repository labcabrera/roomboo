package org.lab.roomboo.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomSearchRequest {

	private String buildingId;

	private Integer minSize;

	private Boolean videoRequired;

}

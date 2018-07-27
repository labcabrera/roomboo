package org.lab.roomboo.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomSearchOptions {

	private String groupId;

	private Integer minSize;

	private Boolean videoRequired;

	private Boolean includeLocked;

}

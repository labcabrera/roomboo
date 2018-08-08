package org.lab.roomboo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomFeatures {

	private Integer size;

	private Boolean videoCall;

	private Boolean conferenceCall;
	
	private Boolean board;
	
	private Boolean audio;
	
	private Boolean projector;
	
	
	

}

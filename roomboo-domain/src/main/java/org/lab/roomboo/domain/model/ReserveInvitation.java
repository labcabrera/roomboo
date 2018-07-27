package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

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
public class ReserveInvitation {

	private String target;

	// TODO avoid serialization
	private String token;

	private LocalDateTime confirmed;

}

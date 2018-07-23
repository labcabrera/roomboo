package org.lab.roomboo.api.model.hateoas;

import org.lab.roomboo.domain.model.RoomReserve;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomReserveResource extends ResourceSupport {

	private RoomReserve roomReserve;

}

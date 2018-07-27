package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.domain.model.Room;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.util.UriComponents;

public class RoomAssembler extends PagedResourcesAssembler<Room> {

	public RoomAssembler(HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {
		super(resolver, baseUri);
	}

}

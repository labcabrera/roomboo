package org.lab.roomboo.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.controller.BuildingController;
import org.lab.roomboo.api.controller.CalendarController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.domain.model.Building;
import org.lab.roomboo.domain.model.Room;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class RoomResource extends ResourceSupport {

	private final Room room;

	public RoomResource(Room room) {
		this.room = room;
		String id = room.getId();
		add(linkTo(methodOn(RoomController.class).findById(id)).withSelfRel());
		if (room.getBuilding() != null) {
			String buildingId = room.getBuilding().getId();
			add(linkTo(methodOn(BuildingController.class).findById(buildingId)).withRel("building"));
			room.setBuilding(Building.builder().id(buildingId).build());
		}
		add(linkTo(methodOn(ReserveController.class).find(id, StringUtils.EMPTY, 0, 10)).withRel("reserves"));
		add(linkTo(methodOn(CalendarController.class).findToday(id)).withRel("today"));
		add(linkTo(methodOn(CalendarController.class).findTomorrow(id)).withRel("tomorrow"));
		add(linkTo(AppUserController.class).withRel("rooms"));
	}

}

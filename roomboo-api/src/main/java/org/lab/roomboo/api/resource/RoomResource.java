package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.CalendarController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.api.controller.RoomGroupController;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.model.RoomGroup;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class RoomResource extends ResourceSupport {

	private final Room room;

	public RoomResource(Room room) {
		this.room = room;
		String id = room.getId();

		add(linkTo(methodOn(RoomController.class).findById(id)).withSelfRel());
		add(linkTo(methodOn(RoomController.class).status(id)).withRel("status"));

		if (room.getGroup() != null) {
			String groupId = room.getGroup().getId();
			add(linkTo(methodOn(RoomGroupController.class).findById(groupId)).withRel("roomGroup"));
			room.setGroup(RoomGroup.builder().id(groupId).build());
		}

		add(linkTo(methodOn(ReserveController.class).find("roomId==" + id, 0, 10)).withRel("roomReserves"));
		add(linkTo(methodOn(CalendarController.class).findToday(id)).withRel("today"));
		add(linkTo(methodOn(CalendarController.class).findTomorrow(id)).withRel("tomorrow"));
		add(linkTo(AppUserController.class).withRel("users"));
	}

}

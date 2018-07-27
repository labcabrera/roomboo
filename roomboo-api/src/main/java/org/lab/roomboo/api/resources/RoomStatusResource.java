package org.lab.roomboo.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.api.controller.RoomGroupController;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.model.RoomGroup;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomStatusResource extends ResourceSupport {

	private RoomStatus status;
	private LocalDateTime now;
	private LocalDateTime currentFrom;
	private LocalDateTime currentTo;
	private LocalDateTime nextReserve;

	public RoomStatusResource(Room room, Reserve current, Reserve next) {
		now = LocalDateTime.now();
		if (room.getLocked() != null && room.getLocked().isAfter(now)) {
			status = RoomStatus.ROOM_LOCKED;
		}
		else {
			status = current != null ? RoomStatus.IN_USE : RoomStatus.AVAILABLE;
			currentFrom = current != null ? current.getFrom() : null;
			currentTo = current != null ? current.getTo() : null;
			nextReserve = next != null ? next.getFrom() : null;
		}

		if (room.getGroup() != null) {
			String groupId = room.getGroup().getId();
			add(linkTo(methodOn(RoomGroupController.class).findById(groupId)).withRel("roomGroup"));
			room.setGroup(RoomGroup.builder().id(groupId).build());
		}

		add(linkTo(methodOn(RoomController.class).status(room.getId())).withSelfRel());
		add(linkTo(methodOn(RoomController.class).findById(room.getId())).withRel("room"));
		add(linkTo(methodOn(RoomController.class).find(StringUtils.EMPTY, 0, 10)).withRel("rooms"));
	}

	public enum RoomStatus {
		ROOM_LOCKED, IN_USE, AVAILABLE
	};

}

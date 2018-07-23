package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class RoomOwnerNotFoundException extends RoombooException {

	public RoomOwnerNotFoundException(String id) {
		super("Room " + id + " not found");
	}

}

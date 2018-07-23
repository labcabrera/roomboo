package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class RoomNotFoundException extends RoombooException {

	public RoomNotFoundException(String id) {
		super("Room " + id + " not found");
	}

}

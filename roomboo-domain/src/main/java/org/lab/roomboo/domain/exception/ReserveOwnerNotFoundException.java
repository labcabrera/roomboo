package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class ReserveOwnerNotFoundException extends RoombooException {

	public ReserveOwnerNotFoundException(String id) {
		super("Reserve owner " + id + " not found");
	}

}

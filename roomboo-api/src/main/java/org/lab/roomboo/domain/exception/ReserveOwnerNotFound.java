package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class ReserveOwnerNotFound extends RoombooException {

	public ReserveOwnerNotFound(String id) {
		super("Reserve owner " + id + " not found");
	}

}

package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class BuildingNotFoundException extends RoombooException {

	public BuildingNotFoundException(String id) {
		super("Building " + id + " not found");
	}

}

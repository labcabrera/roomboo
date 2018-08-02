package org.lab.roomboo.core.model.event.listener;

public interface EventListenerOrder {

	public interface UserCreated {
		int Token = 1000;
		int Alert = 2000;
		int EmailNotification = 3000;
	}

	public interface ReserveCreated {
		int TokenCreation = 1000;
		int AlertCreation = 2000;
		int EmailNotification = 3000;
	}

}

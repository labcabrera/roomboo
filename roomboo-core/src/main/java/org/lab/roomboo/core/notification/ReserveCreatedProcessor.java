package org.lab.roomboo.core.notification;

import org.lab.roomboo.domain.model.Reserve;

public interface ReserveCreatedProcessor {

	public interface NotificationOrder {
		int TokenCreation = 1010;
		int AlertCreation = 1020;
		int EmailCreation = 1030;
	}

	void reserveCreated(Reserve reserve);

}

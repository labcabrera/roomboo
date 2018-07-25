package org.lab.roomboo.core.notification;

import org.lab.roomboo.domain.model.Reserve;

public interface BookingNotificationService {

	void reserveCreated(Reserve reserve);

}

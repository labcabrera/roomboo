package org.lab.roomboo.core.notification;

public interface NotificationProcessor<T> {

	void process(T source);

}

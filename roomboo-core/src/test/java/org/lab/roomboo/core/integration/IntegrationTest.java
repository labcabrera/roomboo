package org.lab.roomboo.core.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public abstract class IntegrationTest {

	@Autowired
	protected ThreadPoolTaskExecutor executor;

	public void awaitTaskTermination() {
		long check = System.currentTimeMillis() + 1000000;
		while (System.currentTimeMillis() < check) {
			if (executor.getActiveCount() == 0) {
				break;
			}
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException ignore) {
			}
		}
	}

}

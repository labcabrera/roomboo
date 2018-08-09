package org.lab.roomboo.core.integration.handler;

import java.util.Map;

import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;

public class ReserveCodeConfirmationHandler implements GenericHandler<String> {

	@Autowired
	private ReserveRepository reserveRepository;

	@Override
	public Object handle(String payload, Map<String, Object> headers) {
		Reserve reserve = reserveRepository.findById(payload).get();
		// TODO check state
		return null;
	}

}

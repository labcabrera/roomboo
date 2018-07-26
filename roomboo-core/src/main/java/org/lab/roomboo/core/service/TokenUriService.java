package org.lab.roomboo.core.service;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;

public interface TokenUriService {

	void processUri(ReserveConfirmationToken token);

}

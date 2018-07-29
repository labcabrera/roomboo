package org.lab.roomboo.core.service;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;

public interface TokenUriService {

	void processUri(ReserveConfirmationToken token);

	void processUri(UserConfirmationToken token);

}

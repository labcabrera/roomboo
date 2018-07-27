package org.lab.roomboo.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveSearchOptions {

	private String roomId;

	private String userId;

	private boolean includeUnconfirmed;

	private boolean includeCancelled;

}

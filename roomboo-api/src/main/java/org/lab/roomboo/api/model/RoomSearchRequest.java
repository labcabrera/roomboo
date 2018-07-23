package org.lab.roomboo.api.model;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomSearchRequest {

	@ApiModelProperty(value = "Building identifier", required = false)
	private String buildingId;

	@ApiModelProperty(value = "Initial reserve date", required = false)
	private LocalDateTime from;

	@ApiModelProperty(value = "Final reserve date", required = false)
	private LocalDateTime to;

	@ApiModelProperty(value = "Min room size", required = false)
	private Integer minSize;

	@ApiModelProperty(value = "Video required", required = false)
	private Boolean videoRequired;
}

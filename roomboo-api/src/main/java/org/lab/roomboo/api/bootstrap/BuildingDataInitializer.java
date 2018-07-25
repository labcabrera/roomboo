package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.Building;
import org.lab.roomboo.domain.repository.BuildingRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.Building)
public class BuildingDataInitializer extends DataInitializer<Building> {

	public BuildingDataInitializer(BuildingRepository repository) {
		super(Building.class, repository, "bootstrap/buildings.json");
	}

}

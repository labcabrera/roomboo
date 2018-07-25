package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.Company)
public class CompanyDataInitializer extends DataInitializer<Company> {

	public CompanyDataInitializer(CompanyRepository repository) {
		super(Company.class, repository, "bootstrap/companies.json");
	}

}

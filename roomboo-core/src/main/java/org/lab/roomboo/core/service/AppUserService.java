package org.lab.roomboo.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.AppUserSearchOptions;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

	private static final String CASE_INSENSITIVE_OPTIONS = "i";

	@Autowired
	private MongoTemplate mongoTemplate;

	public Page<AppUser> findPageable(AppUserSearchOptions options, Pageable pageable) {
		Query query = new Query().with(pageable);
		if (StringUtils.isNotBlank(options.getCompanyId())) {
			query.addCriteria(Criteria.where("company.id").is(options.getCompanyId()));
		}
		if (StringUtils.isNotBlank(options.getEmail())) {
			query.addCriteria(Criteria.where("email").regex("^" + options.getEmail(), CASE_INSENSITIVE_OPTIONS));
		}
		if (StringUtils.isNotBlank(options.getText())) {
			String regex = "^" + options.getText();
			query.addCriteria(new Criteria().orOperator( //@formatter:off
				Criteria.where("name").regex(regex, CASE_INSENSITIVE_OPTIONS),
				Criteria.where("lastName").regex(regex, CASE_INSENSITIVE_OPTIONS),
				Criteria.where("email").regex(regex, CASE_INSENSITIVE_OPTIONS))); //@formatter:on
		}
		// TODO find by company
		List<AppUser> list = mongoTemplate.find(query, AppUser.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, AppUser.class));
	}

}

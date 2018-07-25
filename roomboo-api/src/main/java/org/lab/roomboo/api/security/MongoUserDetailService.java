package org.lab.roomboo.api.security;

import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MongoUserDetailService implements UserDetailsService {

	private final ApiUserRepository apiUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return apiUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}

}
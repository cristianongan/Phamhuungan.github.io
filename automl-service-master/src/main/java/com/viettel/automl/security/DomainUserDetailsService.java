package com.viettel.automl.security;

import com.viettel.automl.model.UsersEntity;
import com.viettel.automl.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

	private final UsersRepository usersRepository;

	public DomainUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		// Kiểm tra xem user có tồn tại trong database không?
		UsersEntity user = usersRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}

	public UserDetails loadUserById(Long id) throws Exception {
		// Kiểm tra xem user có tồn tại trong database không?
		Optional<UsersEntity> opt = usersRepository.findById(id);
		if (!opt.isPresent()) {
			throw new Exception("not found");
		}
		return new CustomUserDetails(opt.get());
	}
}

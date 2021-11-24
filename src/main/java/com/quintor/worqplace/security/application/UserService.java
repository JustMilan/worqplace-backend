package com.quintor.worqplace.security.application;

import com.quintor.worqplace.application.EmployeeService;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.security.data.SpringUserRepository;
import com.quintor.worqplace.security.data.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implements UserDetailsService in order to make it usable
 * as login/registration service for Spring.
 * (see AuthenticationFilter)
 */
@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
	private final SpringUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private EmployeeService employeeService;

	public void register(String username, String password, String firstname, String lastname) {
		String encodedPassword = this.passwordEncoder.encode(password);

		Employee employee = employeeService.saveEmployee(firstname, lastname);
		if (employee == null)
			return;

		User user = new User(username, encodedPassword, employee);
		this.userRepository.save(user);
	}

	@Override
	public User loadUserByUsername(String username) {
		return this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}
}

package com.quintor.worqplace.security.presentation;

import com.quintor.worqplace.security.application.UserService;
import com.quintor.worqplace.security.presentation.dto.Registration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationController {
	private UserService userService;

	@PostMapping
	public void register(@Validated @RequestBody Registration registration) {
		this.userService.register(
				registration.username,
				registration.password
		);
	}
}

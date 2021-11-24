package com.quintor.worqplace.security.data;

/**
 * This is a data model.
 * <p>
 * It is similar to a domain model, but is
 * intended for storage purposes. It does not
 * contain a lot of business logic.
 */
public class UserProfile {
	private String username;

	public UserProfile(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}

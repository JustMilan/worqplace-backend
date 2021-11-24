package com.quintor.worqplace.security.data;

public enum UserRoles {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String roleName;

	UserRoles(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}
}

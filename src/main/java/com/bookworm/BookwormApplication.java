package com.bookworm;

import com.bookworm.domain.User;
import com.bookworm.domain.security.Role;
import com.bookworm.domain.security.UserRole;
import com.bookworm.service.UserService;
import com.bookworm.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookwormApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookwormApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		User user = new User();
		user.setFirstName("ram");
		user.setLastName("ram");
		user.setUsername("r");
		user.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user.setEmail("jj@pj.com");
		Set<UserRole> userRoles = new HashSet();
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);
	}
}

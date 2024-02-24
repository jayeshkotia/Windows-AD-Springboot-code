package com.soap.application.ldap.controller;

import javax.naming.NamingException;

import com.soap.application.ldap.Model.SearchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soap.application.ldap.Model.User;
import com.soap.application.ldap.service.LdapService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ldapController {
	@Autowired
	private LdapService ldapService;
	
	@GetMapping("/test")
	public String test() {
	   return "welcome to ldap app";
	}

	@Autowired
	public ldapController(LdapService ldapService) {
		this.ldapService = ldapService;
	}

	@GetMapping("/createuser")
	public ResponseEntity<String> createUser() {
		try {
			ldapService.addUser("createldapdelete4","5khm0m1VYt");
			return ResponseEntity.ok("User created successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
		}
	}

	@GetMapping("/getusers")
	public ResponseEntity<List<User>> getUser( ) throws NamingException {
		List<User> userList=ldapService.getUsers();

		return ResponseEntity.ok(userList);
	}
	@GetMapping("/search")
	public ResponseEntity<List<SearchUser>> search( ) throws NamingException {
		List<SearchUser> userList=ldapService.search("createldapdelete4","jdfkl444fd");

		return ResponseEntity.ok(userList);
	}

	@GetMapping("/updateuser")
	public ResponseEntity<String> updateUserSn() {
		try {
			ldapService.updateUser("createldapdelete4","aman");
			return ResponseEntity.ok("User updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
		}
	}

	@GetMapping("/updateuserpassword")
	public ResponseEntity<String> updateUser() {
		try {
			ldapService.updateUserPassword("createldapdelete4","B@6323d81e","jdfkl444fd");
			return ResponseEntity.ok("User updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
		}
	}

	@GetMapping("/deleteuser")
	public ResponseEntity<String> deleteUser() {
		try {
			ldapService.deleteUser("createldapdelete4");
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
		}
	}

}

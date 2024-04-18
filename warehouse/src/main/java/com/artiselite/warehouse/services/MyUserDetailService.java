package com.artiselite.warehouse.services;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.artiselite.warehouse.models.Role;
import com.artiselite.warehouse.models.UserEntity;
 
@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		Optional<UserEntity> user = userRepository.findByUsername(username);
		
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("Could not find user");
		}
		var userObject = user.get();
		return new User(userObject.getUsername(), 
					userObject.getPassword(), 
					mapRolesToAuthorities(userObject.getRoles()));
//		return org.springframework.security.core.userdetails.User.builder()
//				.username(userObject.getUsername())
//				.password(userObject.getPassword())
//				.roles(mapRolesToAuthorities(userObject.getRoles())
//				.build();
		
//		return new MyUserDetails(user);
	}
	
	private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}

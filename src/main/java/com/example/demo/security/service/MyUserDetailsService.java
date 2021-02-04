package com.example.demo.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.MyUserDetails;
import com.example.demo.security.repo.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
@Autowired
public UserRepository iUserRepo;
	
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	// TODO Auto-generated method stub
	Optional<UserDao> user = iUserRepo.findByEmail(username);
	return user.map(MyUserDetails::new).get();
}
}

package com.example.demo.security.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.UserDao;

@Repository
public interface UserRepository extends CrudRepository<UserDao, String>{
	Optional<UserDao> findByEmail(String email);
}

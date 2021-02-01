package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.UserDao;

@Repository
public interface UserRepo extends CrudRepository<UserDao, String>{

}

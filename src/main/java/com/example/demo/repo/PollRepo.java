package com.example.demo.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.PollDao;

@Repository
public interface PollRepo extends CrudRepository<PollDao, String>{

}

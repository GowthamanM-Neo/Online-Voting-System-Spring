package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.PollDao;
import com.example.demo.dao.VoteDao;

@Repository
public interface VoteRepo extends CrudRepository<VoteDao, Integer>{

	Iterable<VoteDao> findAllByVoterName(String string);

	Iterable<VoteDao> findAllByPollName(String pollName);

}

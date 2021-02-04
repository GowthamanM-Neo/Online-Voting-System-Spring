package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CategoryDao;

@Repository
public interface CategoryRepo extends CrudRepository<CategoryDao, String>{

}

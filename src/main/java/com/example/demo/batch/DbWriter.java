package com.example.demo.batch;

import java.util.List;



import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

 
import com.example.demo.dao.UserDao;
import com.example.demo.repo.UserRepo;

 

@Component
public class DbWriter implements ItemWriter<UserDao> {
    
    private UserRepo userRepository;

 

    @Autowired
    public DbWriter (UserRepo userRepository) {
        this.userRepository = userRepository;
    }

 

    @Override
    public void write(List<? extends UserDao> users) throws Exception{
        System.out.println("Data Saved for Users: " + users);
        userRepository.saveAll(users);
    }
}

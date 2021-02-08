package com.example.demo.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.dao.UserDao;

 



 

@Component
public class Processor implements ItemProcessor<UserDao, UserDao> {
    @Override
    public UserDao process(UserDao item) throws Exception {
        // TODO Auto-generated method stub
        return item;
    }
}
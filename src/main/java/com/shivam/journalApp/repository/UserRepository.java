package com.shivam.journalApp.repository;

import com.shivam.journalApp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String userName);

    void deleteByUserName(String userName);
}
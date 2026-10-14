package com.shivam.journalApp.repository;

import com.shivam.journalApp.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA() {

        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users =  mongoTemplate.find(query, User.class);
        return users;
    }

    public @Nullable Object getUserForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users =  mongoTemplate.find(query, User.class);
        return users.get(0);
    }
}

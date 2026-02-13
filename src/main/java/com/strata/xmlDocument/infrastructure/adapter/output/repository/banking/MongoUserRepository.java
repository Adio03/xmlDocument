package com.strata.xmlDocument.infrastructure.adapter.output.repository.banking;

import com.strata.xmlDocument.application.output.banking.UserRepository;
import com.strata.xmlDocument.domain.model.banking.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB implementation of UserRepository
 * Handles persistence for User entities
 */
@Repository
@RequiredArgsConstructor
public class MongoUserRepository implements UserRepository {
    
    private final MongoTemplate mongoTemplate;
    
    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }
    
    @Override
    public Optional<User> findById(String userId) {
        User user = mongoTemplate.findById(userId, User.class);
        return Optional.ofNullable(user);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
    
    @Override
    public Optional<User> findByNationalId(String nationalId) {
        Query query = new Query(Criteria.where("nationalId").is(nationalId));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.exists(query, User.class);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.exists(query, User.class);
    }
    
    @Override
    public void deleteById(String userId) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(userId)), User.class);
    }
}
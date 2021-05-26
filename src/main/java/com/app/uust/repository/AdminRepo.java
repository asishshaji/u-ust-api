package com.app.uust.repository;

import com.app.uust.models.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends MongoRepository<Admin, String> {
    List<Admin> findByUsername(String username);

}

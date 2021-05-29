package com.app.uust.repository;

import com.app.uust.models.LeaveType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepo extends MongoRepository<LeaveType, String> {}

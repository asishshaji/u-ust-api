package com.app.uust.repository;

import com.app.uust.models.Leave;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepo extends MongoRepository<Leave, String> {
  List<Leave> findByUsername(String username, Sort sort);
}

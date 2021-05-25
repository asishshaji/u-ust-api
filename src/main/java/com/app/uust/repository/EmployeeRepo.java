package com.app.uust.repository;

import com.app.uust.models.Employee;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends MongoRepository<Employee, String> {
  List<Employee> findByUsername(String username);
}

package com.app.uust.services;

import com.app.uust.models.Employee;
import com.app.uust.repository.EmployeeRepo;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements UserDetailsService {
  @Autowired
  private EmployeeRepo employeeRepo;

  public Employee getEmployeeByUsername(String username) {
    return employeeRepo.findByUsername(username).get(0);
  }

  @Override
  public UserDetails loadUserByUsername(String arg0)
    throws UsernameNotFoundException {
    Employee employee = getEmployeeByUsername(arg0);
    return new User(
      employee.getUsername(),
      employee.getPassword(),
      new ArrayList<>()
    );
  }
}

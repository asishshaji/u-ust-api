package com.app.uust.services;

import com.app.uust.models.Admin;
import com.app.uust.models.Employee;
import com.app.uust.repository.AdminRepo;
import com.app.uust.repository.EmployeeRepo;
import com.app.uust.repository.TimeSheetRepo;
import com.app.uust.utils.Helpers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements UserDetailsService {
  @Autowired
  private TimeSheetRepo timeSheetRepo;

  @Autowired
  private AdminRepo adminRepo;

  @Autowired
  private EmployeeRepo employeeRepo;

  public Employee createEmployee(Employee employee) throws Exception {
    // Check if employee exists

    List<Employee> emps = employeeRepo.findByUsername(employee.getUsername());
    if (emps.size() > 0) {
      throw new Exception("Username already exists");
    }

    Employee emp = new Employee();
    emp.setUsername(employee.getUsername());
    emp.setFirstName(employee.getFirstName());
    emp.setLastName(employee.getLastName());
    emp.setPassword(employee.getPassword());
    emp.setCreatedTimestamp(Helpers.getCurrentTimestamp());

    System.out.println(emp);
    return employeeRepo.save(emp);
  }

  public Employee updateEmployee(Employee employee) throws Exception {
    Optional<Employee> o = employeeRepo.findById(employee.getId());
    Employee emp = o.get();
    if (emp != null) {
      return employeeRepo.save(employee);
    } else {
      throw new Exception("User doesnt exists");
    }
  }

  public List<Employee> getAllEmployees() {
    return employeeRepo.findAll();
  }

  public void deleteEmployee(String id) {
    employeeRepo.deleteById(id);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    // TODO Auto-generated method stub

    Admin admin = adminRepo.findByUsername(username).get(0);
    return new User(
      admin.getUsername(),
      admin.getPassword(),
      new ArrayList<>()
    );
  }
}

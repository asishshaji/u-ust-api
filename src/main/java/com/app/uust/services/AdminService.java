package com.app.uust.services;

import com.app.uust.models.Admin;
import com.app.uust.models.Employee;
import com.app.uust.models.Leave;
import com.app.uust.models.LeaveType;
import com.app.uust.repository.AdminRepo;
import com.app.uust.repository.EmployeeRepo;
import com.app.uust.repository.LeaveRepo;
import com.app.uust.repository.LeaveTypeRepo;
import com.app.uust.repository.TimeSheetRepo;
import com.app.uust.utils.Helpers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

  @Autowired
  private LeaveTypeRepo leaveTypeRepo;

  @Autowired
  private LeaveRepo leaveRepo;

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
    emp.setImageUrl(employee.getImageUrl());

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
    System.out.println("HIHELLO " + username);
    List<Admin> admins = adminRepo.findByUsername("admin");
    System.out.println("HIHELLO " + admins.size());

    Admin admin = (admins.size() > 0) ? admins.get(0) : null;
    System.out.println("HIHELLO " + admin.toString());
    return new User(
      admin.getUsername(),
      admin.getPassword(),
      new ArrayList<>()
    );
  }

  public Employee getEmployeeById(String id) {
    Optional<Employee> optionEmp = employeeRepo.findById(id);
    Employee emp = optionEmp.get();
    return emp;
  }

  public LeaveType createLeaveType(LeaveType leaveType) {
    return leaveTypeRepo.insert(leaveType);
  }

  public List<LeaveType> getAllLeaveType() {
    return leaveTypeRepo.findAll();
  }

  public List<Leave> getAllLeave() {
    return leaveRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
  }

  public Leave createLeave(Leave leave) {
    return leaveRepo.save(leave);
  }
}

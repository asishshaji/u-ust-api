package com.app.uust.controller;

import com.app.uust.models.AuthReq;
import com.app.uust.models.AuthResp;
import com.app.uust.models.Employee;
import com.app.uust.models.Leave;
import com.app.uust.models.LeaveType;
import com.app.uust.models.MessageResponse;
import com.app.uust.models.SheetDetail;
import com.app.uust.models.TimesheetReq;
import com.app.uust.repository.LeaveTypeRepo;
import com.app.uust.services.AdminService;
import com.app.uust.services.EmployeeService;
import com.app.uust.utils.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin")
public class AdminController {
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  @Qualifier("adminAuthManager")
  private AuthenticationManager authenticationManager;

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private AdminService adminService;

  @PostMapping("/authorize")
  public ResponseEntity<?> createauthenticationToken(
    @RequestBody AuthReq authReq
  )
    throws Exception {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          authReq.getUsername(),
          authReq.getPassword()
        )
      );
    } catch (BadCredentialsException e) {
      return ResponseEntity
        .badRequest()
        .body(new MessageResponse("Incorrect username and password "));
    }
    System.out.println("HIHELLO " + authReq.toString());
    final UserDetails userDetails = adminService.loadUserByUsername(
      authReq.getUsername()
    );

    final String jwt = jwtUtil.generateToken(userDetails, "admin");

    return ResponseEntity.ok(new AuthResp(jwt));
  }

  @GetMapping("/hello")
  public String hello(@RequestAttribute("type") String username) {
    System.out.println(username);
    return "Hello World!";
  }

  @PostMapping("/employee")
  public ResponseEntity<?> createEmployee(
    @RequestAttribute("type") String type,
    @RequestBody Employee employee
  )
    throws Exception {
    if (type.equals("admin")) {
      Employee emp = adminService.createEmployee(employee);
      if (emp != null) return ResponseEntity.ok(
        new MessageResponse("Created employee")
      ); else return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new MessageResponse("Error creating employee"));
    }

    return ResponseEntity.badRequest().body(new MessageResponse("Invalid jwt"));
  }

  @PutMapping("/employee")
  public ResponseEntity<?> updateEmployee(
    @RequestAttribute("type") String type,
    @RequestBody Employee employee
  )
    throws Exception {
    if (type.equals("admin")) {
      adminService.updateEmployee(employee);
      return ResponseEntity.ok(new MessageResponse("Updated employee"));
    }

    return ResponseEntity.badRequest().body(new MessageResponse("Invalid jwt"));
  }

  @GetMapping("/employee")
  public ResponseEntity<?> getEmployees(@RequestAttribute("type") String type) {
    if (type.equals("admin")) return ResponseEntity.ok(
      adminService.getAllEmployees()
    );
    return ResponseEntity.badRequest().body(new MessageResponse("Invalid jwt"));
  }

  @DeleteMapping("/employee")
  public ResponseEntity<?> deleteEmployee(
    @RequestAttribute("type") String type,
    @RequestParam(name = "id") String id
  ) {
    System.out.println("DELETEING");
    if (type.equals("admin")) {
      adminService.deleteEmployee(id);
      return ResponseEntity.ok(new MessageResponse("Deleted employee"));
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Invalid jwt"));
  }

  @GetMapping("/employeebyid")
  public ResponseEntity<?> getEmployeeById(
    @RequestAttribute("type") String type,
    @RequestParam(name = "id") String id
  ) {
    if (type.equals("admin")) return ResponseEntity.ok(
      adminService.getEmployeeById(id)
    );
    return ResponseEntity.badRequest().body(new MessageResponse("Invalid jwt"));
  }

  @PostMapping("/timesheet")
  public ResponseEntity<?> getTimesheetForUser(
    @RequestParam("username") String username,
    @RequestBody TimesheetReq tReq
  ) {
    List<SheetDetail> res = employeeService.getTimeSheetForUserInThisWeek(
      username,
      tReq
    );
    return ResponseEntity.ok(res);
  }

  @PostMapping("/leavetype")
  public LeaveType createLeaveType(@RequestBody LeaveType leaveType) {
    return adminService.createLeaveType(leaveType);
  }

  @GetMapping("leavetype")
  public List<LeaveType> getAllLeaveType() {
    return adminService.getAllLeaveType();
  }

  @GetMapping("leavebyname")
  public List<Leave> getLeaveByUsername(
    @RequestParam("username") String username
  ) {
    return employeeService.findByUsername(username);
  }

  @GetMapping("leave")
  public List<Leave> getAllLeaves() {
    return adminService.getAllLeave();
  }

  @PutMapping("leave")
  public Leave updateLeave(@RequestBody Leave leave) {
    return adminService.createLeave(leave);
  }
}

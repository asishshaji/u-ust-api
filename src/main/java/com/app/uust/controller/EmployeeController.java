package com.app.uust.controller;

import com.app.uust.models.*;
import com.app.uust.services.EmployeeService;
import com.app.uust.utils.JwtUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/employee")
public class EmployeeController {
  @Autowired
  @Qualifier("empAuthManager")
  private AuthenticationManager authenticationManager;

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private JwtUtil jwtUtil;

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
      System.out.println("Error");
      return ResponseEntity
        .badRequest()
        .body(new MessageResponse("Incorrect username and password "));
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new MessageResponse("Invalid credentials "));
    }

    final UserDetails userDetails = employeeService.loadUserByUsername(
      authReq.getUsername()
    );

    final String jwt = jwtUtil.generateToken(userDetails, "employee");
    return ResponseEntity.ok(new AuthResp(jwt));
  }

  @PostMapping("/timesheet")
  public ResponseEntity<?> addTimeSheet(
    @RequestBody TimeSheetReq timeSheetReq,
    @RequestAttribute("username") String username
  )
    throws Exception {
    // add, update can be done here
    // Check if today's timesheet exists. Add if exists create if not

    employeeService.addToTimeSheet(timeSheetReq, username);

    return ResponseEntity.ok(timeSheetReq.toString());
  }

  @PutMapping("/timesheet")
  public ResponseEntity<?> updateTimeSheet(
    @RequestBody TimeSheetReq timeSheetReq,
    @RequestAttribute("username") String username
  )
    throws Exception {
    // add, update can be done here
    // Check if today's timesheet exists. Add if exists create if not
    System.out.println(timeSheetReq);
    employeeService.updateTimesheet(timeSheetReq, username);

    return ResponseEntity.ok(timeSheetReq.toString());
  }

  @PutMapping("/profile")
  public ResponseEntity<?> updateProfile(
    @RequestBody Employee employee,
    @RequestAttribute("username") String username
  )
    throws Exception {
    employeeService.updateProfile(employee, username);

    return ResponseEntity.ok(
      new MessageResponse("Updated employee").toString()
    );
  }

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile(
    @RequestAttribute("username") String username
  )
    throws Exception {
    Employee employee = employeeService.getEmployeeByUsername(username);
    return ResponseEntity.ok(employee);
  }

  @PutMapping("/password")
  public ResponseEntity<?> changePassword(
    @RequestAttribute("username") String username,
    @RequestBody Map<String, String> resp
  )
    throws Exception {
    employeeService.updatePassword(resp.get("password"), username);
    return ResponseEntity.ok(new MessageResponse("Password updated"));
  }

  @PostMapping("/profile/timesheet")
  public ResponseEntity<?> getTimesheetForUser(
    @RequestAttribute("username") String username,
    @RequestBody TimesheetReq tReq
  ) {
    List<SheetDetail> res = employeeService.getTimeSheetForUserInThisWeek(
      username,
      tReq
    );
    return ResponseEntity.ok(res);
  }

  @GetMapping("/profile/timesheet")
  public List<SheetDetail> getAllTimesheetForUser(
    @RequestAttribute("username") String username
  ) {
    return employeeService.getAllTimesheetsForUser(username);
  }

  @GetMapping("leavetype")
  public List<LeaveType> getAllLeaveType() {
    return employeeService.getAllLeaveType();
  }

  @PostMapping("leave")
  public Leave createLeave(
    @RequestBody Leave leave,
    @RequestAttribute("username") String username
  ) {
    leave.setUsername(username);
    return employeeService.createLeave(leave);
  }

  @GetMapping("leave")
  public List<Leave> getLeaveByUsername(
    @RequestAttribute("username") String username
  ) {
    return employeeService.findByUsername(username);
  }

  @PutMapping("leave")
  public Leave updateLeave(@RequestBody Leave leave) {
    return employeeService.createLeave(leave);
  }
}

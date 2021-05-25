package com.app.uust.controller;

import com.app.uust.models.AuthReq;
import com.app.uust.models.AuthResp;
import com.app.uust.models.TimeSheetReq;
import com.app.uust.services.EmployeeService;
import com.app.uust.utils.JwtUtil;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private JwtUtil jwtUtil;

  // @GetMapping("/")
  // public String hello(@RequestAttribute("username") String username) {
  //   System.out.println(username);
  //   return "Hello World!";
  // }

  // @GetMapping("/hi")
  // public String hi(
  //   @RequestAttribute("username") String username,
  //   @RequestAttribute("type") String type
  // ) {
  //   System.out.println(username);
  //   return "Hello " + username + " Type: " + type;
  // }

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
      throw new Exception("Incorrect username and password ", e);
    }

    final UserDetails userDetails = employeeService.loadUserByUsername(
      authReq.getUsername()
    );
    final String jwt = jwtUtil.generateToken(userDetails);
    return ResponseEntity.ok(new AuthResp(jwt));
  }

  @PostMapping("/timesheet")
  public ResponseEntity<?> addTimeSheet(
    @RequestBody TimeSheetReq timeSheetReq,
    @RequestAttribute("username") String username
  ) {
    // Check if today's timesheet exists. Add if exists create if not

    employeeService.addToTimeSheet(timeSheetReq, username);

    return ResponseEntity.ok(timeSheetReq.toString());
  }
}

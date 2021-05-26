package com.app.uust.controller;

import com.app.uust.models.*;
import com.app.uust.services.EmployeeService;
import com.app.uust.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {
    @Autowired
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

        employeeService.updateTimesheet(timeSheetReq, username);

        return ResponseEntity.ok(timeSheetReq.toString());
    }

    @GetMapping("/")
    public String hello(@RequestAttribute("username") String username) {
        System.out.println(username);
        return "Hello World!";
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

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestAttribute("username") String username,
            @RequestBody Map<String, String> resp
    )
            throws Exception {
        employeeService.updatePassword(resp.get("password"), username);
        return ResponseEntity.ok(new MessageResponse("Password updated"));
    }
}

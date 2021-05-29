package com.app.uust.services;

import com.app.uust.models.Employee;
import com.app.uust.models.Leave;
import com.app.uust.models.LeaveType;
import com.app.uust.models.SheetDetail;
import com.app.uust.models.TimeSheet;
import com.app.uust.models.TimeSheetReq;
import com.app.uust.models.TimesheetReq;
import com.app.uust.repository.EmployeeRepo;
import com.app.uust.repository.LeaveRepo;
import com.app.uust.repository.LeaveTypeRepo;
import com.app.uust.repository.TimeSheetRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements UserDetailsService {
  @Autowired
  private EmployeeRepo employeeRepo;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  private TimeSheetRepo timeSheetRepo;

  @Autowired
  private LeaveTypeRepo leaveTypeRepo;

  @Autowired
  private LeaveRepo leaveRepo;

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

  public void addToTimeSheet(TimeSheetReq timesheetRequest, String username)
    throws Exception {
    List<TimeSheet> timeSheets = timeSheetRepo.findByDateTimestamp(
      timesheetRequest.getTimestamp()
    );
    SheetDetail empSheetDetail;

    if (timeSheets.size() == 0) {
      empSheetDetail = new SheetDetail();
      empSheetDetail.setAttendanceState(timesheetRequest.getState());
      empSheetDetail.setStatus(timesheetRequest.getStatus());
      empSheetDetail.setUsername(username);

      timeSheetRepo.save(
        new TimeSheet(timesheetRequest.getTimestamp(), username, empSheetDetail)
      );
    } else {
      // Timesheet for timestamp exists
      // Now check for current user's attendance

      TimeSheet timeSheet = timeSheets.get(0);
      List<SheetDetail> attendance = timeSheet.getAttendance();

      SheetDetail uSheetDetail = attendance
        .stream()
        .filter(s -> username.equals(s.getUsername()))
        .findAny()
        .orElse(null);

      if (uSheetDetail == null) {
        empSheetDetail =
          new SheetDetail(
            username,
            timesheetRequest.getState(),
            timesheetRequest.getStatus()
          );
        attendance.add(empSheetDetail);
        timeSheet.setAttendance(attendance);
        timeSheetRepo.save(timeSheet);
      } else {
        empSheetDetail = uSheetDetail;
        System.out.println(empSheetDetail);
        System.out.println(timesheetRequest);
        if (empSheetDetail.getAttendanceState() != "submitted") {
          attendance.remove(empSheetDetail);

          empSheetDetail.setAttendanceState(timesheetRequest.getState());
          empSheetDetail.setStatus(timesheetRequest.getStatus());
          attendance.add(empSheetDetail);
          timeSheet.setAttendance(attendance);

          timeSheetRepo.save(timeSheet);
        } else throw new Exception(
          "Already added for " + timesheetRequest.getTimestamp()
        );
      }
    }
  }

  public void updateTimesheet(TimeSheetReq timesheetRequest, String username)
    throws Exception {
    List<TimeSheet> timeSheets = timeSheetRepo.findByDateTimestamp(
      timesheetRequest.getTimestamp()
    );
    SheetDetail empSheetDetail = null;

    if (timeSheets.size() > 0) {
      TimeSheet timeSheet = timeSheets.get(0);
      List<SheetDetail> attendance = timeSheet.getAttendance();

      SheetDetail uSheetDetail = attendance
        .stream()
        .filter(s -> username.equals(s.getUsername()))
        .findAny()
        .orElse(null);

      System.out.println(uSheetDetail);

      if (uSheetDetail != null) {
        attendance.remove(uSheetDetail);
        empSheetDetail = uSheetDetail;
        empSheetDetail.setAttendanceState(timesheetRequest.getState());
        empSheetDetail.setStatus(timesheetRequest.getStatus());
        empSheetDetail.setUsername(username);

        attendance.add(empSheetDetail);
        timeSheet.setAttendance(attendance);
        timeSheetRepo.save(timeSheet);
      } else throw new Exception("Cannot add.");
    } else throw new Exception("Cannot add.");
  }

  public List<SheetDetail> getTimeSheetForUserInThisWeek(
    String username,
    TimesheetReq tReq
  ) {
    Long end = tReq.getEnd();
    Long start = tReq.getStart();
    AggregationResults<Document> document = timeSheetRepo.getTimeSheets(
      tReq.getEnd(),
      tReq.getStart(),
      username
    );

    Long days = (end - start) / 86400000;
    System.out.println(days);

    // 86400000 add this to get next day

    List<Document> docs = document.getMappedResults();
    List<SheetDetail> sheetDetails = new ArrayList<>();
    for (Document doc : docs) {
      Long timestamp = doc.getLong("dateTimestamp");

      String attendanceState = doc.getEmbedded(
        List.of("attendance", "attendanceState"),
        String.class
      );
      String attendanceStatus = doc.getEmbedded(
        List.of("attendance", "status"),
        String.class
      );
      SheetDetail sheet = new SheetDetail(
        username,
        attendanceState,
        attendanceStatus,
        timestamp
      );
      sheetDetails.add(sheet);
    }

    return sheetDetails;
  }

  public List<SheetDetail> getAllTimesheetsForUser(String username) {
    AggregationResults<Document> document = timeSheetRepo.getallTimesheetsByUsername(
      username
    );
    List<Document> docs = document.getMappedResults();
    List<SheetDetail> sheetDetails = new ArrayList<>();

    for (Document doc : docs) {
      Long timestamp = doc.getLong("dateTimestamp");

      String attendanceState = doc.getEmbedded(
        List.of("attendance", "attendanceState"),
        String.class
      );
      String attendanceStatus = doc.getEmbedded(
        List.of("attendance", "status"),
        String.class
      );
      SheetDetail sheet = new SheetDetail(
        username,
        attendanceState,
        attendanceStatus,
        timestamp
      );
      sheetDetails.add(sheet);
    }
    return sheetDetails;
  }

  public void updateProfile(Employee employee, String username)
    throws Exception {
    Employee emp = getEmployeeByUsername(username);
    if (emp != null) {
      employeeRepo.save(employee);
    } else {
      throw new Exception("User doesnot exists");
    }
  }

  public void updatePassword(String password, String username)
    throws Exception {
    Employee emp = getEmployeeByUsername(username);
    if (emp != null) {
      emp.setPassword(password);
      employeeRepo.save(emp);
    } else {
      throw new Exception("User doesnot exists");
    }
  }

  public List<LeaveType> getAllLeaveType() {
    return leaveTypeRepo.findAll();
  }

  public Leave createLeave(Leave leave) {
    return leaveRepo.save(leave);
  }

  public List<Leave> findByUsername(String username) {
    return leaveRepo.findByUsername(
      username,
      Sort.by(Sort.Direction.DESC, "id")
    );
  }
}

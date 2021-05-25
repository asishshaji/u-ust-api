package com.app.uust.services;

import com.app.uust.models.Employee;
import com.app.uust.models.SheetDetail;
import com.app.uust.models.TimeSheet;
import com.app.uust.models.TimeSheetReq;
import com.app.uust.repository.EmployeeRepo;
import com.app.uust.repository.TimeSheetRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

  @Autowired
  private TimeSheetRepo timeSheetRepo;

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

  public void addToTimeSheet(TimeSheetReq timesheetRequest, String username) {
    List<TimeSheet> timeSheets = timeSheetRepo.findByDateTimestamp(
      timesheetRequest.getTimestamp()
    );
    SheetDetail empSheetDetail = null;

    if (timeSheets.size() == 0) {
      empSheetDetail = new SheetDetail();
      empSheetDetail.setAttendanceState(timesheetRequest.getState());
      empSheetDetail.setStatus(timesheetRequest.getStatus());

      timeSheetRepo.save(
        new TimeSheet(timesheetRequest.getTimestamp(), username, empSheetDetail)
      );
    } else {
      // Timesheet for timestamp exists
      // Now check for current user's attendance
      // List<SheetDetail> attendance = timeSheets.get(0).getAttendance();
      // SheetDetail usersDetail = attendance.stream().anyMatch(o -> o.getUsername().equals(username));

      TimeSheet timeSheet = timeSheets.get(0);
      System.out.println(timeSheet);
      HashMap<String, SheetDetail> attendance = timeSheet.getAttendance();
      if (attendance.containsKey(username)) {
        empSheetDetail = attendance.get(username);
        empSheetDetail.setAttendanceState(timesheetRequest.getState());
        empSheetDetail.setStatus(timesheetRequest.getStatus());
        attendance.put(username, empSheetDetail);
        //  update timeSheets

      }
      timeSheetRepo.save(timeSheet);
    }
  }
}

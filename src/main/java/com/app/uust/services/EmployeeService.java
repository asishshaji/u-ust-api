package com.app.uust.services;

import com.app.uust.models.Employee;
import com.app.uust.models.SheetDetail;
import com.app.uust.models.TimeSheet;
import com.app.uust.models.TimeSheetReq;
import com.app.uust.repository.EmployeeRepo;
import com.app.uust.repository.TimeSheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            HashMap<String, SheetDetail> attendance = timeSheet.getAttendance();
            if (!attendance.containsKey(username)) {
                empSheetDetail =
                        new SheetDetail(
                                username,
                                timesheetRequest.getState(),
                                timesheetRequest.getStatus()
                        );
                attendance.put(username, empSheetDetail);

                timeSheetRepo.save(timeSheet);
            } else {
                throw new Exception(
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
            HashMap<String, SheetDetail> attendance = timeSheet.getAttendance();
            if (attendance.containsKey(username)) {
                empSheetDetail = attendance.get(username);
                empSheetDetail.setAttendanceState(timesheetRequest.getState());
                empSheetDetail.setStatus(timesheetRequest.getStatus());
            }
            attendance.put(username, empSheetDetail);

            timeSheetRepo.save(timeSheet);
        } else throw new Exception("Cannot add.");
    }

    public void updateProfile(Employee employee, String username)
            throws Exception {
        Employee emp = getEmployeeByUsername(username);
        if (emp != null) {
            employee.setId(emp.getId());
            employee.setPassword(emp.getPassword());
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
}

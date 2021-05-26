package com.app.uust.models;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Timesheet")
public class TimeSheet {

  public TimeSheet(
    String id,
    String dateTimestamp,
    HashMap<String, SheetDetail> attendance
  ) {
    this.id = id;
    this.dateTimestamp = dateTimestamp;
    this.attendance = attendance;
  }

  public TimeSheet() {}

  public TimeSheet(String dateTimestamp, String username, SheetDetail detail) {
    this.dateTimestamp = dateTimestamp;
    this.attendance = new HashMap<>();
    this.attendance.put(username, detail);
  }

  @Id
  private String id;

  private String dateTimestamp;

  private HashMap<String, SheetDetail> attendance;
}

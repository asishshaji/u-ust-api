package com.app.uust.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    Long dateTimestamp,
    List<SheetDetail> attendance
  ) {
    this.id = id;
    this.dateTimestamp = dateTimestamp;
    this.attendance = attendance;
  }

  public TimeSheet() {}

  public TimeSheet(Long dateTimestamp, String username, SheetDetail detail) {
    this.dateTimestamp = dateTimestamp;
    this.attendance = new ArrayList();
    this.attendance.add(detail);
  }

  @Id
  private String id;

  private Long dateTimestamp;

  private List<SheetDetail> attendance;
}

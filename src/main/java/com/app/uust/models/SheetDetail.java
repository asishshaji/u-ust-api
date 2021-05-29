package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SheetDetail {
  private String username;
  private String attendanceState; //saved,submitted
  private String status; //absent, present
  private Long timestamp;

  public SheetDetail(String username, String attendanceState, String status) {
    this.username = username;
    this.attendanceState = attendanceState;
    this.status = status;
  }

  public SheetDetail(
    String username,
    String attendanceState,
    String status,
    Long timestamp
  ) {
    this.username = username;
    this.attendanceState = attendanceState;
    this.status = status;
    this.timestamp = timestamp;
  }

  public SheetDetail() {}
}

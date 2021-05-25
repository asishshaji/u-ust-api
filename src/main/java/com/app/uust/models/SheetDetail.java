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
}

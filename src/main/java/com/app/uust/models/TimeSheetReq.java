package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TimeSheetReq {
  private String status;
  private String state;
  private String timestamp;
}

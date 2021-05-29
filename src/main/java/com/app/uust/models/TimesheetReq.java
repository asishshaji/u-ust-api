package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class TimesheetReq {
  private Long start;
  private Long end;
}

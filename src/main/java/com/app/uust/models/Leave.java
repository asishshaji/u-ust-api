package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@ToString
@Document(collection = "Leave")
public class Leave {
  @Id
  private String id;

  private String leaveType;
  private String date;
  private String remarks;
  private Boolean validated = false;
  private String username;
}

package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "LeaveType")
public class LeaveType {
  @Id
  private String id;

  private String typeName;
}

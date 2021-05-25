package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Employee")
public class Employee {
  @Id
  private String id;

  private String username;
  private String firstName;
  private String lastName;

  private String password;
  private String imageUrl;

  private String createdTimestamp;
}

package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthReq {
  private String username;
  private String password;

  public AuthReq() {}

  public AuthReq(String username, String password) {
    this.username = username;
    this.password = password;
  }
}

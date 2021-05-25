package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthResp {
  private String jwt;

  public AuthResp(String jwt) {
    this.jwt = jwt;
  }

  public AuthResp() {}
}

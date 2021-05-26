package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MessageResponse {
  private String message;

  public MessageResponse() {}

  public MessageResponse(String message) {
    this.message = message;
  }
}

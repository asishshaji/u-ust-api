package com.app.uust.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Timesheet")
public class TimeSheet {
  @Id
  private String id;

  private String dateTimestamp;
  private List<SheetDetail> attendance;
}

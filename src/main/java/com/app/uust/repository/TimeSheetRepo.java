package com.app.uust.repository;

import com.app.uust.models.TimeSheet;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSheetRepo extends MongoRepository<TimeSheet, String> {
  List<TimeSheet> findByDateTimestamp(String dateTimestamp);
}

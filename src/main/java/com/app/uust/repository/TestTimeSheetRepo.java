package com.app.uust.repository;

import com.app.uust.models.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestTimeSheetRepo extends MongoRepository<TimeSheet, String> {}

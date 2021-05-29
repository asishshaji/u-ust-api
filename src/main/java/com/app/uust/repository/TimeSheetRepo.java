package com.app.uust.repository;

import com.app.uust.models.TimeSheet;
import java.util.List;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSheetRepo extends MongoRepository<TimeSheet, String> {
  List<TimeSheet> findByDateTimestamp(Long dateTimestamp);

  @Aggregation(
    pipeline = {
      "{$match: {\n" + "dateTimestamp: {$gte:?1,$lte:?0}" + "     }}",
      "{$unwind: \n" + "$attendance}",
      "{$match: {\n" + "'attendance.username': ?2" + "     }}",
      "{$sort : {\n" + "dateTimestamp: 1}" + "}",
    }
  )
  AggregationResults<org.bson.Document> getTimeSheets(
    Long startTime,
    Long endTime,
    String username
  );

  @Aggregation(
    pipeline = {
      "{$unwind : \n" + "$attendance}",
      "{$match : {\n" + "'attendance.username': ?0" + " }}",
    }
  )
  AggregationResults<org.bson.Document> getallTimesheetsByUsername(
    String username
  );
}
//  "{{ $match: { dateTimestamp : ?0 } }, {$unwind : attendance},{$match : {attendance.username:?2}}}"

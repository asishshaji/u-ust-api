package com.app.uust.controller;

import com.app.uust.models.TimeSheet;
import com.app.uust.repository.TestTimeSheetRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTimeSheet {
  @Autowired
  private TestTimeSheetRepo _repo;

  @PostMapping("/save")
  public String saveTimeSheet(@RequestBody TimeSheet timeSheet) {
    _repo.save(timeSheet);
    return "Saved";
  }

  @GetMapping("/get")
  public List<TimeSheet> getSheet() {
    return _repo.findAll();
  }
}

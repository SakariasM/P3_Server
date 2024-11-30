package com.p3.Server.weeklyTimelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weeklytimelog")
public class WeeklyTimelogController {

    private final WeeklyTimelogService weeklyTimelogService;

    @Autowired
    public WeeklyTimelogController(WeeklyTimelogService weeklyTimelogService) {
        this.weeklyTimelogService = weeklyTimelogService;
    }

    @GetMapping
    public List<WeeklyTimelog> getAllWeeklyTimelogs() {
        return weeklyTimelogService.getAllWeeklyTimelogs();
    }

    @GetMapping("/{id}")
    public Optional<WeeklyTimelog> getWeeklyTimelogById(@PathVariable int id) {
        return weeklyTimelogService.getWeeklyTimelogById(id);
    }

    @PostMapping
    public WeeklyTimelog addNewWeeklyTimelog(@RequestBody WeeklyTimelog weeklyTimelog) {
        return weeklyTimelogService.addNewWeeklyTimelog(weeklyTimelog);
    }

    @DeleteMapping("/{id}")
    public void deleteWeeklyTimelog(@PathVariable int id) {
        weeklyTimelogService.deleteWeeklyTimelog(id);
    }

    @PutMapping("/{id}")
    public WeeklyTimelog updateWeeklyTimelog(@PathVariable int id, @RequestBody WeeklyTimelog updatedWeeklyTimelog) {
        return weeklyTimelogService.updateWeeklyTimelog(id, updatedWeeklyTimelog);
    }

}

package com.p3.Server.weeklyTimelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

}

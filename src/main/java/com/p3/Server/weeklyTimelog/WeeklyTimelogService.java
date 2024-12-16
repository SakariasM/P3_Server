package com.p3.Server.weeklyTimelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeeklyTimelogService {

    private final WeeklyTimelogRepository weeklyTimelogRepository;

    @Autowired
    public WeeklyTimelogService(WeeklyTimelogRepository weeklyTimelogRepository) {
        this.weeklyTimelogRepository = weeklyTimelogRepository;
    }

    public List<WeeklyTimelog> getAllWeeklyTimelogs() {
        return weeklyTimelogRepository.findAll();
    }

    public Optional<WeeklyTimelog> getWeeklyTimelogById(int id) {
        return weeklyTimelogRepository.findById(id);
    }

    public void updateWeeklyTimelog(WeeklyTimelog updatedWeeklyTimelog) {
        Optional<WeeklyTimelog> weeklyTimelogOptional = weeklyTimelogRepository
                .findWeeklyTimelogByWeekStartAndUserId(
                        updatedWeeklyTimelog.getWeekStart(),
                        updatedWeeklyTimelog.getUserId()
                );

        if (weeklyTimelogOptional.isPresent()) {
            WeeklyTimelog weeklyTimelog = weeklyTimelogOptional.get();
            weeklyTimelog.setTotalHoursWorked(updatedWeeklyTimelog.getTotalHoursWorked());
            weeklyTimelogRepository.save(weeklyTimelog);
        } else {
            weeklyTimelogRepository.save(updatedWeeklyTimelog);
        }
    }


}
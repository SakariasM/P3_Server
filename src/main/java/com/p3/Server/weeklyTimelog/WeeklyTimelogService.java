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

    public WeeklyTimelog addNewWeeklyTimelog(WeeklyTimelog weeklyTimelog) {
        return weeklyTimelogRepository.save(weeklyTimelog);
    }

    public void deleteWeeklyTimelog(int id) {
        if (!weeklyTimelogRepository.existsById(id)) {
            throw new IllegalStateException("Weekly timelog with ID " + id + " does not exist.");
        }
        weeklyTimelogRepository.deleteById(id);
    }

    public WeeklyTimelog updateWeeklyTimelog(int id, WeeklyTimelog updatedWeeklyTimelog) {
        return weeklyTimelogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setUserId(updatedWeeklyTimelog.getUserId());
                    existingLog.setWeekStart(updatedWeeklyTimelog.getWeekStart());
                    existingLog.setTotalHoursWorked(updatedWeeklyTimelog.getTotalHoursWorked());
                    return weeklyTimelogRepository.save(existingLog);
                })
                .orElseThrow(() -> new IllegalStateException("Weekly timelog with ID " + id + " does not exist."));
    }

    
}
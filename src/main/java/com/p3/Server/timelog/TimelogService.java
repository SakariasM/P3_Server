package com.p3.Server.timelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelogService {

    private final TimelogRepository timelogRepository;

    @Autowired
    public TimelogService(TimelogRepository timelogRepository) {this.timelogRepository = timelogRepository;}

    // Get specific period?
    // Get a month of all timelogs
    // Get a month/week for 1 user

    public List<Timelog> getMonthTimelogs(int month, int year) {  // Kan enten tage imod int som bare bliver ændret, eller tage imod date object
        return timelogRepository.findByMonthAndYear(month, year);
    }

}
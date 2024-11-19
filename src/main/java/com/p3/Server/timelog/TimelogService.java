package com.p3.Server.timelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimelogService {

    private final TimelogRepository timelogRepository;

    @Autowired
    public TimelogService(TimelogRepository timelogRepository) {
        this.timelogRepository = timelogRepository;
    }

    // Get specific period?
    // Get a month of all timelogs
    // Get a month/week for 1 user

    public List<Timelog> getMonthTimelogs(int month, int year) {  // Kan enten tage imod int som bare bliver ændret, eller tage imod date object
        return timelogRepository.findByMonthAndYear(month, year);
    }

    public void postCheckIn(String json) {
        Timelog timelog = new Timelog();

        System.out.println(json);

    }

    public void checkAndHandleIncompleteTimelogs() {
        // Find timelogs with no checkout time
        List<Timelog> incompleteTimelogs = timelogRepository.findTodaysCheckInsWithoutCheckOuts();

        for (Timelog timelog : incompleteTimelogs) {
            System.out.println("Found incomplete check-in for user: " + timelog.getUser_id());


            // Create a new Timelog for the check-out event
            Timelog checkOut = new Timelog();
            checkOut.setUser_id(timelog.getUser_id());
            checkOut.setShift_date(timelog.getShift_date());
            checkOut.setEvent_type("check_out");
            checkOut.setEvent_time(LocalDateTime.now());
            timelogRepository.save(checkOut); // Persist the update
            System.out.println("Updated timelog ID = " + timelog.getLog_id());
        }
    }
}
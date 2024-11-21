package com.p3.Server.timelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void postCheckIn(Timelog checkIn) {
        checkIn.setEvent_time(LocalDateTime.now());
        checkIn.setShift_date(LocalDate.now());
        timelogRepository.save(checkIn);
    }

    public void postBreakStart(Timelog breakStart) {
        breakStart.setEvent_time(LocalDateTime.now());
        breakStart.setShift_date(LocalDate.now());
        timelogRepository.save(breakStart);
    }

    public void postBreakEnd(Timelog breakEnd) {
        breakEnd.setEvent_time(LocalDateTime.now());
        breakEnd.setShift_date(LocalDate.now());
        timelogRepository.save(breakEnd);
    }

    public Map<String, Object> getAllUserTodayTimelogs(int user_id, LocalDate date){    // TODO overvej om det ikke skal være en del af jsonutil
        List<Timelog> timelogs = timelogRepository.findByIdAndDay(user_id, date);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user_id);
        response.put("date", date);
        response.put("timelogs", timelogs);

        return response;
    }

    public void postCheckOut(Timelog checkOut) {
        checkOut.setEvent_time(LocalDateTime.now());
        checkOut.setShift_date(LocalDate.now());
        timelogRepository.save(checkOut);
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

    public Timelog getLastCheckOutEvent(int userId) {
        Timelog timelog = timelogRepository.findLastCheckOutEvent(userId, "check_out");
        if (timelog == null) {
            System.out.println("TimelogRepository returned null for userId: " + userId);
        }
        return timelog;
    }
}
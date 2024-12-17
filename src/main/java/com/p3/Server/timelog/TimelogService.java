package com.p3.Server.timelog;

import com.p3.Server.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;

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
    // Get timelogs for a specific date
    public List<Timelog> getTimelogsForDate(LocalDate date) {
        return timelogRepository.findBySpecificDate(date);
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

            //Getting the date and setting a specific time
            LocalDate currentDate = LocalDate.now();
            LocalTime specificTime = LocalTime.of(23, 59, 0);
            LocalDateTime dateTimeWithSpecificTime = LocalDateTime.of(currentDate, specificTime);

            // Create a new Timelog for the check-out event
            Timelog checkOut = new Timelog();
            checkOut.setUser_id(timelog.getUser_id());
            checkOut.setShift_date(timelog.getShift_date());
            checkOut.setEvent_type("check_out");
            checkOut.setEvent_time(dateTimeWithSpecificTime);
            timelogRepository.save(checkOut); // Persist the update
            System.out.println("Updated timelog ID = " + timelog.getLog_id());

        }
    }

    public Timelog getLastCheckOutEvent(int userId) {
        Timelog timelog = timelogRepository.findLastCheckOutEvent(userId, "check_out");
        if (timelog == null) {
            timelog = new Timelog();
            timelog.setUser_id(userId);
            timelog.setShift_date(LocalDate.now());
            timelog.setEvent_time(LocalDateTime.now());
            timelog.setEvent_type("no_event");
        }
        return timelog;
    }

    public List<List<Timelog>> getWeekTimelogs(LocalDate date, int userId) {
        LocalDate weekStart = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Timelog> weekTimelogs = timelogRepository.findByUserIdAndWeek(userId, weekStart, weekEnd); // Returns a list of all timelogs
        List<List<Timelog>> weekTimelogsGroupedByDay = new ArrayList<>();   // A list that contains lists of timelogs, each sublist corresponds to all timelogs for the user on a specific day

        for(int i = 0; i < 7; i++){     // O(n^2) not good :( but shit, it works    For each day in the week
            LocalDate currentDay = weekStart.plusDays(i);
            List<Timelog> dailyTimelogs = new ArrayList<>();

            for(Timelog timelog : weekTimelogs){
                if(timelog.getShift_date().isEqual(currentDay)){
                    dailyTimelogs.add(timelog);
                }
            }
            weekTimelogsGroupedByDay.add(dailyTimelogs);
        }

        return weekTimelogsGroupedByDay;
    }

    public List<Timelog> getTimelogsByDateAndId(LocalDate date, int userId) {
        return timelogRepository.findByIdAndDay(userId, date);
    }

    public void putTimelogs(List<Timelog> timelogs) {
        timelogRepository.saveAll(timelogs);
    }

    public List<Timelog> getTimelogsByPeriod(LocalDate startDate, LocalDate endDate) {
        return timelogRepository.findByDatePeriod(startDate, endDate);
    }

    // ONLY USED BT THE DAILY RESET, DELETES TIMELOGS FROM 5 YEARS BACK AS TO FOLLOW REGULATIONS
    public void deleteExpiredTimelogs() {
        LocalDate date = LocalDate.now().plusYears(-5);
        List<Timelog> timelogs = timelogRepository.findByEvent_time(date);
        System.out.println(timelogs);
        timelogRepository.deleteAll(timelogs);
    }
}
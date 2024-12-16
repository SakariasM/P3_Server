package com.p3.Server.global;

import com.p3.Server.timelog.*;
import com.p3.Server.user.User;
import com.p3.Server.user.UserService;
import com.p3.Server.weeklyTimelog.WeeklyTimelog;
import com.p3.Server.weeklyTimelog.WeeklyTimelogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Component
public class ScheduledReset {

    private final UserService userService;
    private final TimelogService timelogService;
    private final WeeklyTimelogService weeklyTimelogService;

    @Autowired
    public ScheduledReset(TimelogService timelogService, UserService userService, WeeklyTimelogService weeklyTimelogService) {
        this.timelogService = timelogService;
        this.userService = userService;
        this.weeklyTimelogService = weeklyTimelogService;
    }

    // Run daily just before midnight
    @Scheduled(cron = "0 59 23 * * *")
    public void handleDailyReset() {
        System.out.println("Running daily reset for incomplete timelogs...");
        timelogService.checkAndHandleIncompleteTimelogs();
        userService.checkAndHandleIncompleteClockedIns();
        updateWeeklyTimelog();
    }

    public void updateWeeklyTimelog() {
        List<User> users = userService.getUsers();
        // This shit is dumb
        for (User user : users) {
            Duration weekWorkHours = Duration.ZERO;
            List<List<Timelog>> weekTimelogs = timelogService.getWeekTimelogs(LocalDate.now(), user.getUserId());
            for (List<Timelog> dayTimelogs : weekTimelogs) {
                Duration dayWorkHours = Duration.ZERO;
                Duration totalBreakTime = Duration.ZERO;
                LocalDateTime checkInTime = null;
                LocalDateTime breakStartTime = null;
                for (Timelog timelog : dayTimelogs) {
                    // Check event_type add/sub from hourmark
                    String event_type = timelog.getEvent_type();
                    LocalDateTime eventTime = timelog.getEvent_time();

                    switch (event_type) {
                        case "check_in":
                            checkInTime = eventTime;
                            break;
                        case "break_start":
                            breakStartTime = eventTime;
                            break;
                        case "break_end":
                            if (breakStartTime != null) {
                                totalBreakTime = totalBreakTime.plus(Duration.between(breakStartTime, eventTime));
                                breakStartTime = null;
                            }
                            break;
                        case "check_out":
                            if (checkInTime != null && !(eventTime.getHour() == 23 && eventTime.getMinute() == 59)) {
                                Duration shiftDuration = Duration.between(checkInTime, eventTime);
                                dayWorkHours = dayWorkHours.plus(shiftDuration.minus(totalBreakTime));
                                checkInTime = null;
                                totalBreakTime = Duration.ZERO;
                            }
                            break;
                        default:
                            break;
                    }
                }
                weekWorkHours = weekWorkHours.plus(dayWorkHours);
            }

            String weeklyWorkHours = String.format("%02d:%02d:%02d", weekWorkHours.toHours(), weekWorkHours.toMinutesPart(), weekWorkHours.toSecondsPart());
            WeeklyTimelog weeklyTimelog = new WeeklyTimelog(
                    user.getUserId(),
                    user.getFullName(),
                    LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1),
                    weeklyWorkHours
            );

            weeklyTimelogService.updateWeeklyTimelog(weeklyTimelog);
        }
    }
}
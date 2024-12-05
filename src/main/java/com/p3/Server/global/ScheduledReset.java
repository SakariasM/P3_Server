package com.p3.Server.global;

import com.p3.Server.timelog.*;
import com.p3.Server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledReset {

    private final UserService userService;
    private TimelogService timelogService;

    @Autowired
    public ScheduledReset(TimelogService timelogService, UserService userService) {
        this.timelogService = timelogService;
        this.userService = userService;
    }

    // Run daily just before midnight
    @Scheduled(cron = "0 59 23 * * *")
    public void handleDailyReset() {
        System.out.println("Running daily reset for incomplete timelogs...");
        timelogService.checkAndHandleIncompleteTimelogs();
        userService.checkAndHandleIncompleteClockedIns();
    }
}
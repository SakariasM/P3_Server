package com.p3.Server.timelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/timelog")
public class TimelogController {

    private final TimelogService timelogService;

    @Autowired
    public TimelogController(TimelogService timelogService) {this.timelogService = timelogService;}

    /*
     * GET
     */
    @GetMapping(path="/getTimelogsByDate")  // api/timelog/getTimelogsByDate?date={yyyy-MM-dd}
    public List<Timelog> getTimelogsByDate(@RequestParam LocalDate date) {
        return timelogService.getTimelogsForDate(date);

    }
    @GetMapping(path="/getMonthlyTimelog")          // api/timelog/getMonthlyTimelog?month={month}&year={year}
    public List<Timelog> getAllUsersTimelogsMonth(@RequestParam int month, @RequestParam int year){
        return timelogService.getMonthTimelogs(month, year);
    }

    @GetMapping(path = "/ALL")
    public Map<String, Object> getAllUserTodayTimelogs(@RequestParam int user_id, @RequestParam LocalDate date){
        return timelogService.getAllUserTodayTimelogs(user_id, date);
    }

    @GetMapping("/lastCheckOut")
    public ResponseEntity<Timelog> getLastCheckOutEvent(@RequestParam int user_id) {
        Timelog timelog = timelogService.getLastCheckOutEvent(user_id);
        if (timelog == null) {
            System.out.println("No last checkout event found for user_id: " + user_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            System.out.println("Last checkout event: " + timelog);
            return ResponseEntity.ok(timelog);
        }
    }

    @GetMapping(path="history")
    public List<List<Timelog>> getWeekTimelogs(@RequestParam LocalDate date, @RequestParam int userId) {
        return timelogService.getWeekTimelogs(date, userId);
    }

    /*
     * POST
     */

    @PostMapping(path="/checkIn")       // api/timelog/checkIn
    public void postCheckIn(@RequestBody Timelog checkIn){
        timelogService.postCheckIn(checkIn);
    }

    @PostMapping(path="/breakStart")    // api/timelog/breakStart
    public void postBreakStart(@RequestBody Timelog breakStart){
        timelogService.postBreakStart(breakStart);
    }

    @PostMapping(path="/breakEnd")      // api/timelog/breakEnd
    public void postBreakEnd(@RequestBody Timelog breakEnd){
        timelogService.postBreakEnd(breakEnd);
    }

    @PostMapping(path="/checkOut")
    public void postCheckOut(@RequestBody Timelog checkOut){
        timelogService.postCheckOut(checkOut);
    }


    /*
     * DELETE
     */

    /*
     * PUT
     */



}

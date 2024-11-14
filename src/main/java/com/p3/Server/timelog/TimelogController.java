package com.p3.Server.timelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/timelog")
public class TimelogController {

    private final TimelogService timelogService;

    @Autowired
    public TimelogController(TimelogService timelogService) {this.timelogService = timelogService;}

    /*
     * GET
     */

    @GetMapping(path="/getMonthlyTimelog")          // api/timelog/getMonthlyTimelog?month={month}&year={year}
    public List<Timelog> getAllUsersTimelogsMonth(@RequestParam int month, @RequestParam int year){
        return timelogService.getMonthTimelogs(month, year);
    }

    /*
     * POST
     */

    /*
     * DELETE
     */

    /*
     * PUT
     */


}

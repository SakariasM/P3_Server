package com.p3.Server.timelog;

import com.p3.Server.user.User;
import com.p3.Server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/timelog")
public class TimelogController {

    private final TimelogService timelogService;
    private final UserService userService;

    @Autowired
    public TimelogController(TimelogService timelogService, UserService userService) {this.timelogService = timelogService;
        this.userService = userService;
    }

    /*
     * GET
     */

    // Gets all timelogs for a specific date (includes multiple user_ids)
    @GetMapping(path="/getTimelogsByDate")  // api/timelog/getTimelogsByDate?date={yyyy-MM-dd}
    public List<Timelog> getTimelogsByDate(@RequestParam LocalDate date) {
        return timelogService.getTimelogsForDate(date);
    }

    // Gets all timelogs for a specific date for a specific user
    @GetMapping(path="/day")
    public List<Timelog> getTimelogsByDateAndId(@RequestParam LocalDate date, @RequestParam int userId) {
        return timelogService.getTimelogsByDateAndId(date, userId);
    }

    // Gets last check out for a specific user - if none returns null
    //TODO client checks if body is returned, so can we not just check client side and return no matter what?
    //WTF ARE WE EVEN DOING? IT IS NEVER NULL CUZ YOU CHECK IN SERVICE AND ADD A COMPLETELY NEW OBJECT IF THAT IS THE CASE!?!?!
    // DOING 4 FUCKING CHECKS ON THE EXACT SAME THING?!?! FAM...........
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

    // Gets all timelogs for a specific user within a specific week (monday - sunday) Week dates are calc in service
    @GetMapping(path="/weekly")
    public List<List<Timelog>> getWeekTimelogs(@RequestParam LocalDate date, @RequestParam int userId) {
        return timelogService.getWeekTimelogs(date, userId);
    }

    // Gets all timelogs within a specific period and returns as a downloadable csv file
    @GetMapping(value = "/downloadCSV", produces = "text/csv")
    public ResponseEntity<Resource> getCSV(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate) {
        // Fetch timelogs for the specified period
        List<Timelog> timelogs = timelogService.getTimelogsByPeriod(startDate, endDate);

        List<User> users = userService.getUsers();
        Map<Integer, String> usersMap = new HashMap<>();
        for(User user : users) {
            String fullName = user.getFullName();
            Integer userId = user.getUserId();
            usersMap.put(userId, fullName);
        }

        Map<String, String> eventTypeMap = Map.of(
                "check_in", "Vagt start",
                "check_out", "Vagt slut",
                "break_start", "Pause start",
                "break_end", "Pause slut"
        );


        // Check if the list is empty
        if (timelogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Build the csv
        StringBuilder csvContent = new StringBuilder("\"Navn\";\"Dato\";\"Type\";\"Tidspunkt\"\n");
        for (Timelog timelog : timelogs) {
            csvContent.append("\"").append(usersMap.get(timelog.getUser_id())).append("\";")
                    .append("\"").append(timelog.getShift_date()).append("\";")
                    .append("\"").append(eventTypeMap.get(timelog.getEvent_type())).append("\";")
                    .append("\"").append(timelog.getEvent_time()).append("\"");
        }

        // Convert csv
        ByteArrayResource resource = new ByteArrayResource(csvContent.toString().getBytes());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=timelogs.csv")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    /*
     * POST
     */

    // TODO check if it can be made to one post mapping, and just saves the timelog with a given event_type
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

    // TODO slet gamle timelogs der ikke eksisterer længere

    /*
     * PUT
     */

    // Takes multiple timelogs and updates the corresponding ones already found in the database
    @PutMapping(path="/list")
    public void postTimelogs(@RequestBody List<Timelog> timelogs){
        timelogService.putTimelogs(timelogs);
    }



}

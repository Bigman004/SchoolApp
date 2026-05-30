package com.example.SchoolApp.monitoring;

import com.example.SchoolApp.events.CreateUserEvent;
import com.example.SchoolApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private LogService logService;
    private UserService userService;
    private ApplicationEventPublisher publisher;

    @Autowired
    public MonitorController(LogService logService,  UserService userService,  ApplicationEventPublisher publisher) {
        this.logService = logService;
        this.userService = userService;
        this.publisher = publisher;
    }
    @GetMapping("/dev_log")
    @Secured("DEVELOPER")
    public PagedModel<LogMonitor> devPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value ="searchParam", defaultValue = "") String searchParams,
            @RequestParam(value = "searchBy", defaultValue ="")  String searchBy
    ) {
        LocalDate date1;
        Pageable pageable = PageRequest.of(page, size);

        if(date.isEmpty()){
           date1 = LocalDate.now();

        }
        else{
            var str = date.split("-");
            date1 = LocalDate.of(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
        }
        Page<LogMonitor> logs = logService.getTodayLogs(pageable, searchBy, searchParams, date1);
        return new PagedModel<>(logs);
    }
    @Secured("DEVELOPER")
    @GetMapping("/user_size")
    public ResponseEntity<?> userSize(){

        return new ResponseEntity<>(userService.listUser().size(), HttpStatus.OK);
    }
    @GetMapping("/start-monitoring")
    public void startMonitoring(){
        publisher.publishEvent(new CreateUserEvent(
                1L,
                "DEVELOPER",
                "DEVELOPER",
                "developer_password"
        ));

        return;
    }
    @PostMapping("/save_school")
    public ResponseEntity<?> add_school(){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

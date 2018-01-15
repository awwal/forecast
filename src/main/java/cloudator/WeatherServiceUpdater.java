package cloudator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WeatherServiceUpdater {

    @Autowired
    TaskScheduler taskScheduler;


    @Autowired
    WeatherServiceExecutor task;

    @Autowired
    Environment env;

    @PostConstruct
    public void init() {
        int schedulingInterval = env.getProperty("interval.sec", Integer.class, 60);
        System.out.println("Scheduling  every " + schedulingInterval);
        taskScheduler.scheduleWithFixedDelay(task, schedulingInterval * 1000);
    }
}

package com.n26.exam.api.controller;

import com.n26.exam.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(path = "/statistics", method = {RequestMethod.GET})
    public ResponseEntity<?> getLastMinuteStats() {

        return new ResponseEntity<>(statisticsService.getStatisticsFromLastMinute(), HttpStatus.OK);

    }

    /* for testing only */
    @RequestMapping(path = "/statistics/{seconds}", method = {RequestMethod.GET})
    public ResponseEntity<?> getLastSecondsStats(@PathVariable("seconds") Long seconds) {

        return new ResponseEntity<>(statisticsService.getStatisticsFromLastSeconds(seconds), HttpStatus.OK);

    }
}

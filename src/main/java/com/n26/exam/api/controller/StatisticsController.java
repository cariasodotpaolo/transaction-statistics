package com.n26.exam.api.controller;

import static java.util.Objects.isNull;

import com.n26.exam.api.exception.NotFoundException;
import com.n26.exam.model.Statistics;
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
    public ResponseEntity<?> getLastMinuteStats() throws Exception {

        Statistics statistics = statisticsService.getStatisticsFromLastMinute();

        if (isNull(statistics) || statistics.getCount() == 0) {
            throw new NotFoundException("No transactions found.");
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    /* for testing only */
    @RequestMapping(path = "/statistics/{seconds}", method = {RequestMethod.GET})
    public ResponseEntity<?> getLastSecondsStats(@PathVariable("seconds") Long seconds) throws Exception {

        Statistics statistics = statisticsService.getStatisticsFromLastSeconds(seconds);

        if (isNull(statistics) || statistics.getCount() == 0) {
            throw new NotFoundException("No transactions found.");
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}

package com.n26.exam.service;

import com.n26.exam.model.Statistics;

public interface StatisticsService {

    Statistics getStatisticsFromLastMinute();

    Statistics getStatisticsFromLastSeconds(Long seconds);
}

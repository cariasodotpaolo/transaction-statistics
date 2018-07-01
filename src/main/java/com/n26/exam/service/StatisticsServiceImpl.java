package com.n26.exam.service;

import com.n26.exam.repository.TransactionRepository;
import com.n26.exam.model.Statistics;
import com.n26.exam.model.Transaction;
import java.time.ZonedDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.NavigableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private TransactionRepository transactionRepository;

    @Autowired
    public StatisticsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Statistics getStatisticsFromLastMinute() {


        NavigableMap<ZonedDateTime, Transaction> transactions =
                                    transactionRepository.getTransactionsFromLastMinute();

        DoubleSummaryStatistics stats = transactions.values().stream().
                                        mapToDouble((t) -> t.getAmount()).summaryStatistics();

        return new Statistics(stats.getSum(),stats.getAverage(), stats.getMax(),
                              stats.getMin(),stats.getCount());
    }

    @Override
    public Statistics getStatisticsFromLastSeconds(Long seconds) {


        NavigableMap<ZonedDateTime, Transaction> transactions =
            transactionRepository.getTransactionsFromLastSeconds(seconds);

        DoubleSummaryStatistics stats = transactions.values().stream().
            mapToDouble((t) -> t.getAmount()).summaryStatistics();

        return new Statistics(stats.getSum(),stats.getAverage(), stats.getMax(),
            stats.getMin(),stats.getCount());
    }
}

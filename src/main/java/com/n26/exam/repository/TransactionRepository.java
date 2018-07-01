package com.n26.exam.repository;

import com.n26.exam.model.Transaction;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    ConcurrentSkipListMap<ZonedDateTime, Transaction> transactions;


    public TransactionRepository() {

    }

    @PostConstruct
    public void initialize() {

        transactions = new ConcurrentSkipListMap<>(
                            Comparator.comparingLong(z -> z.toInstant().toEpochMilli()));
    }

    public ZonedDateTime add(Transaction transaction) {

        ZonedDateTime zdt = ZonedDateTime.from(transaction.getTimestamp().atZone(ZoneId.systemDefault()));

        transactions.put(zdt, transaction);

        return zdt;

    }

    public ConcurrentNavigableMap<ZonedDateTime, Transaction> getTransactionsFromLastMinute() {

        return transactions.tailMap(ZonedDateTime.now().minusMinutes(1));
    }

    /* used for testing */
    public ConcurrentNavigableMap<ZonedDateTime, Transaction> getTransactionsFromLastSeconds(Long seconds) {

        return transactions.tailMap(ZonedDateTime.now().minusSeconds(seconds));
    }

    /* used for testing */
    public Set<ZonedDateTime> getZoneDateTimeFromLastSeconds(Long seconds) {

        return transactions.tailMap(ZonedDateTime.now().minusSeconds(seconds)).keySet();
    }

}

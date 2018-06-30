package test.n26.exam.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.exam.SpringBootMavenApplication;
import com.n26.exam.model.Statistics;
import com.n26.exam.model.Transaction;
import com.n26.exam.service.StatisticsService;
import com.n26.exam.service.TransactionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootMavenApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionStatisticsServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    private Random random = new Random();

    @Test
    public void testAddTransaction() throws JsonProcessingException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        int numberOfThreads = 2;

        Random random = new Random();

        Runnable producer = () -> IntStream
            .rangeClosed(0, 100)
            .forEach(index -> transactionService.add(
                new Transaction(random.nextDouble(), LocalDateTime.now().minusSeconds(index)))
            );

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(producer);
        }

        logger.info("\n{}",jsonObjectMapper.writeValueAsString(statisticsService.getStatisticsFromLastMinute()));
    }

    @Test
    public void testGetTransactionsFromLastGivenSeconds() throws JsonProcessingException {

        int numberOfThreads = 15;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable producer = () -> {
                Transaction t = new Transaction(new BigDecimal(1.0 + random.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                                LocalDateTime.now());
                transactionService.add(t);
                logger.info("\nNEW TXN: Timestamp: {}, Amount: {}", t.getTimestamp(), t.getAmount());
            };

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(producer);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Statistics stats = statisticsService.getStatisticsFromLastSeconds(5L);
        logger.info("\n{}",jsonObjectMapper.writeValueAsString(stats));
    }
}

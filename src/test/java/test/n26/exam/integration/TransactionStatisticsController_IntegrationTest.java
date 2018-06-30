package test.n26.exam.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.exam.model.Statistics;
import com.n26.exam.model.Transaction;
import io.restassured.response.ValidatableResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class TransactionStatisticsController_IntegrationTest extends IntegrationTestBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper jsonObjectMapper;

    private Random random = new Random();

    @Test
    public void testGetStatisticsForLastMinute() {


        executeCreateTransactions(15, 2, 5);

        getStatisticsForLastMinute();
    }

    @Test
    public void testGetStatisticsForLastSeconds() {

        executeCreateTransactions(15, 2, 1);

        getStatisticsForLastSeconds(10);
    }

    private void executeCreateTransactions(int numberOfThreads,
                                           final int numberOfTransactionsPerRun,
                                           int secondsInterval) {

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable producer = () -> {

            IntStream
                .rangeClosed(0, numberOfTransactionsPerRun)
                .forEach( i ->
                    sendTransactions()
                );
        };

        for (int i = 0; i < numberOfThreads; i++) {

            executorService.execute(producer);

            try {
                TimeUnit.SECONDS.sleep(secondsInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getStatisticsForLastSeconds(int seconds) {

        ValidatableResponse response =

        given()
            .header("Accept","application/json")
            .pathParam("seconds", seconds)
            .log().all()
        .when()
            .get( getUrlContextPathWithPort() + "/statistics/{seconds}")
        .then()
            .statusCode(200)
            .log().all();

        Statistics stats = response.extract().body().as(Statistics.class);

        assertNotNull(stats);
        assertTrue(stats.getCount() > 0);
    }

    private void getStatisticsForLastMinute() {

        ValidatableResponse response =

        given()
            .header("Accept","application/json")
            .log().all()
        .when()
            .get( getUrlContextPathWithPort() + "/statistics")
        .then()
            .statusCode(200)
            .log().all();

        Statistics stats = response.extract().body().as(Statistics.class);

        assertNotNull(stats);
        assertTrue(stats.getCount() > 0);
    }

    private void sendTransactions() {

            given()
                .contentType("application/json")
                .body(givenTransactionJsonRequestBody())
                .log().all()
            .when()
                .post( getUrlContextPathWithPort() + "/transaction")
            .then()
                .statusCode(201)
                .log().all();

    }

    private String givenTransactionJsonRequestBody() {

        String jsonBody = null;

        Transaction transaction = new Transaction(new BigDecimal(1.0 + random.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                                    LocalDateTime.now());

        try {
            jsonBody = jsonObjectMapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return jsonBody;
    }

}

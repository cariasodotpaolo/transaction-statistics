package test.n26.exam.integration;

import static io.restassured.RestAssured.given;
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

        Statistics stats = getStatisticsForLastMinute();

        assertNotNull(stats);
        assertTrue(stats.getCount() > 0);
    }

    @Test
    public void testGetStatisticsForLastGivenSeconds() {

        executeCreateTransactions(15, 2, 1);

        Statistics stats = getStatisticsForLastSeconds(10);

        assertNotNull(stats);
        assertTrue(stats.getCount() > 0);
    }

    @Test
    public void getStatisticsWhenNoTransactionsExist() {

        given()
            .header("Accept","application/json")
            .log().all()
        .when()
            .get( getUrlContextPathWithPort() + "/statistics")
        .then()
            .statusCode(404)
            .log().all();
    }

    @Test
    public void sendTransactionRequestWithMissingFields() {

        given()
            .contentType("application/json")
            .body(new Transaction())
            .log().all()
        .when()
            .post( getUrlContextPathWithPort() + "/transaction")
        .then()
            .statusCode(400)
            .log().all();
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

    private Statistics getStatisticsForLastSeconds(int seconds) {

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

        return response.extract().body().as(Statistics.class);

    }

    private Statistics getStatisticsForLastMinute() {

        ValidatableResponse response =

        given()
            .header("Accept","application/json")
            .log().all()
        .when()
            .get( getUrlContextPathWithPort() + "/statistics")
        .then()
            .statusCode(200)
            .log().all();

        return response.extract().body().as(Statistics.class);

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

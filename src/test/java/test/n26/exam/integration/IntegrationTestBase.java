package test.n26.exam.integration;

import com.n26.exam.SpringBootMavenApplication;
import javax.annotation.PostConstruct;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootMavenApplication.class,  webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTestBase {

    @Value("${server.contextPath}")
    private String contextPath;

    /* SpringBootTest Overrides {server.port} */
    private int serverPort = 8081;

    @Value("${spring.profiles.active}")
    private String profile;

    private String urlContextPathWithPort;


    @PostConstruct
    public void init() {
        urlContextPathWithPort =  String.format("http://localhost:%s%s", serverPort, contextPath);
    }

    public String getUrlContextPathWithPort() {
        return urlContextPathWithPort;
    }

    public int getPort() {
        return serverPort;
    }
}

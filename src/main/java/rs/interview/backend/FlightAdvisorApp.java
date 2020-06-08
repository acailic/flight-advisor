package rs.interview.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a.ilic
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
public class FlightAdvisorApp {

    private final Environment env;


    public FlightAdvisorApp(Environment env) {
        this.env = env;
    }
    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FlightAdvisorApp.class);
        Map<String, Object> defProperties = new HashMap();
        defProperties.put("spring.profiles.default", "dev,swagger");
        app.setDefaultProperties(defProperties);
        app.run(args);
    }



}

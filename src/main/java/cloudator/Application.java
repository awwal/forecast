package cloudator;

import cloudator.model.Celsius$;
import cloudator.model.Location;
import cloudator.model.WeatherRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

//    --location 60.1674,24.9426 --interval.min 5

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        Location helsinki = new Location(60.1674, 24.9426);
//
//        context.getBean(WeatherServiceExecutor.class).getWeatherResult(new WeatherRequest(helsinki, Celsius$.MODULE$));
    }

}

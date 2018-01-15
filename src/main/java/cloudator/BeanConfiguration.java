package cloudator;

import cloudator.model.RequestContext;
import cloudator.service.WeatherService;
import cloudator.service.YahooWeatherService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Environment env;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        logger.info("Initializing thread");
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("WeatherServiceScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public WeatherService weatherService() {
        return new YahooWeatherService(restTemplate());
    }

    @Bean
    public WeatherServiceExecutor weatherServiceExecutor() {
        int minTemp = env.getProperty("mintemp", Integer.class, -30);
        int maxTemp = env.getProperty("maxtemp", Integer.class, +40);
        return new WeatherServiceExecutor(weatherService(), new RequestContext(minTemp, maxTemp));
    }

    @Bean
    RestTemplate restTemplate(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return  restTemplateBuilder
                .setConnectTimeout(500)
                .setReadTimeout(500)
                .build();

    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(objectMapper);
        objectMapper.registerModule(new DefaultScalaModule());

        return converter;
    }



}

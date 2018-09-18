package be.zlz.kara.bin.config;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.Semaphore;

@Configuration
public class ApplicationConfig {

    @Value("${max.concurrent.delay}")
    private int delays;

    @Bean("RequestMeter")
    public Meter getRequestMeter(MetricRegistry metricRegistry){
        return metricRegistry.meter("requests");
    }

    @Bean
    public MetricRegistry getMainMetricRegistry(){
        return new MetricRegistry();
    }

    @Bean
    public Semaphore getDelaySemaphore(){
        return new Semaphore(delays);
    }

}

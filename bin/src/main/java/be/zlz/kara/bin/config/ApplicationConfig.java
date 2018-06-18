package be.zlz.kara.bin.config;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean("RequestMeter")
    public Meter getRequestMeter(MetricRegistry metricRegistry){
        return metricRegistry.meter("requests");
    }

    @Bean
    public MetricRegistry getMainMetricRegistry(){
        return new MetricRegistry();
    }

}

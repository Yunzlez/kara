package be.zlz.kara.bin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Semaphore;

@Configuration
public class ApplicationConfig {

    @Value("${max.concurrent.delay}")
    private int delays;

    @Bean
    public Semaphore getDelaySemaphore(){
        return new Semaphore(delays);
    }

}

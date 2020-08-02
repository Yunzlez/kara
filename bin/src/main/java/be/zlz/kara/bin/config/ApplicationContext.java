package be.zlz.kara.bin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.Semaphore;

@Configuration
@EnableJpaRepositories(basePackages = "be.zlz.kara.bin.repositories")
public class ApplicationContext {

    @Value("${max.concurrent.delay}")
    private int delays;

    @Bean
    public Semaphore getDelaySemaphore(){
        return new Semaphore(delays);
    }

}

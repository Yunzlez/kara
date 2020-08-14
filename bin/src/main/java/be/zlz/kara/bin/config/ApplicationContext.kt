package be.zlz.kara.bin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.concurrent.Semaphore

@Configuration
@EnableJpaRepositories(basePackages = ["be.zlz.kara.bin.repositories"])
open class ApplicationContext {

    @Value("\${max.concurrent.delay}")
    private val delays = 0

    @Bean
    open fun getDelaySemaphore(): Semaphore? {
        return Semaphore(delays)
    }
}

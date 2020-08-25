package be.zlz.kara.bin.config

import be.zlz.kara.bin.domain.ModuleConfig
import be.zlz.kara.bin.modules.KaraModule
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.util.concurrent.RateLimiter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


@Configuration
@EnableJpaRepositories(basePackages = ["be.zlz.kara.bin.repositories"])
open class ApplicationContext {

    @Value("\${max.concurrent.delay}")
    private val delays = 0

    @Bean
    open fun getDelaySemaphore(): Semaphore? {
        return Semaphore(delays)
    }

    @Bean
    @Suppress("UnstableApiUsage")
    //todo configurable
    open fun createRatelimiterCache(): Cache<Long, RateLimiter> {
        return CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES)
                .initialCapacity(50)
                .build(object : CacheLoader<Long, RateLimiter>() {
                    override fun load(key: Long): RateLimiter {
                        return RateLimiter.create(2.0)
                    }
                })
    }

    @Bean
    open fun createModuleConfigCache(): Cache<Long, List<ModuleConfig>> {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .initialCapacity(500)
                .build()
    }

    @Bean("moduleMap")
    open fun createModuleMap(modules: List<KaraModule>): Map<String, KaraModule> {
        return modules.associateBy({it.key}, {it})
    }
}

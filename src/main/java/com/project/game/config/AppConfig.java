package com.project.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AppConfig {
    private Integer aeCorePoolSize = 1;
    private Integer aeMaxPoolSize = 1;
    private Integer aeQueueCapacity = 1000;
    private Integer aeKeepAliveSeconds = 60;
    private String aeThreadnamePrefix = "AsynchThread";
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(aeCorePoolSize);
        executor.setMaxPoolSize(aeMaxPoolSize);
        executor.setQueueCapacity(aeQueueCapacity);
        executor.setThreadNamePrefix(aeThreadnamePrefix);
        executor.setKeepAliveSeconds(aeKeepAliveSeconds);
        executor.initialize();
        return executor;
    }
}

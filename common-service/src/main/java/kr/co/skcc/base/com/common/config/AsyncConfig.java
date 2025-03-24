package kr.co.skcc.base.com.common.config;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshotFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    private final ThreadPoolTaskExecutor micrometerTaskExecutor;

    public AsyncConfig(@Qualifier("micrometerThreadPoolTaskExecutor") ThreadPoolTaskExecutor micrometerTaskExecutor) {
        this.micrometerTaskExecutor = micrometerTaskExecutor;
    }

    @Override
    @Bean("threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        return ContextExecutorService.wrap(micrometerTaskExecutor.getThreadPoolExecutor(),
                ContextSnapshotFactory.builder().build()::captureAll);

    }
}


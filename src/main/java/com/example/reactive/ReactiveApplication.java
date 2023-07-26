package com.example.reactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableCaching
public class ReactiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}


	@Bean(name = "threadPoolExecutor")
	public Executor coreExecutor(@Value("${spring.custom.executor.core-pool-size:2}") Integer corePoolSize,
								 @Value("${spring.custom.executor.max-pool-size:5}") Integer maxPoolSize,
								 @Value("${spring.custom.executor.queue-capacity:500}") Integer queueCapacity) {
		ThreadPoolTaskExecutor fac = new ThreadPoolTaskExecutor ();
		fac.setCorePoolSize(corePoolSize);
		fac.setMaxPoolSize(maxPoolSize);
		fac.setQueueCapacity(queueCapacity);
		fac.initialize();
		return fac;
	}

	@Bean
	HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
		return new ReactivePageableHandlerMethodArgumentResolver();
	}
}

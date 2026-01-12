package com.rafawhitee.estudo.paralelismo.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = { "executor", "defaultExecutor", "taskExecutor", "ioBoundExecutor" })
	@Primary
	public Executor ioBoundExecutor() {
		ExecutorService delegate = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("io-vt-", 0).factory());
		return new AsyncTaskExecutor() {
			@Override
			public void execute(Runnable command) {
				delegate.execute(() -> {
					command.run();
				});
			}
			@Override
			public void execute(Runnable command, long startTimeout) {
				execute(command);
			}
			@Override
			public Future<?> submit(Runnable task) {
				return delegate.submit(() -> {
					task.run();
				});
			}
			@Override
			public <T> Future<T> submit(Callable<T> task) {
				return delegate.submit(() -> {
					return task.call();

				});
			}
		};
	}

	@Bean(name = "cpuBoundExecutor")
	public Executor cpuBoundExecutor() {
		int ncpu = Runtime.getRuntime().availableProcessors();
		ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
		ex.setThreadNamePrefix("cpu-");
		ex.setCorePoolSize(ncpu); // paralelismo ≈ #vCPUs
		ex.setMaxPoolSize(ncpu); // evita oversubscription de CPU
		ex.setQueueCapacity(100); // fila curta (se lotar, backpressure)
		ex.setKeepAliveSeconds(30);
		// Se a fila encher, quem chamou executa a tarefa (aplica backpressure)
		ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		ex.setTaskDecorator(r -> {
			return () -> {
				r.run();
			};
		});
		ex.initialize();
		return ex;
	}

}
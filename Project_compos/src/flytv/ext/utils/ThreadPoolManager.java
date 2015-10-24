package flytv.ext.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
	private ExecutorService service;

	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num * 2);
	}

	public void addTask(Runnable runnable) {
		service.execute(runnable);
	}
}



import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EstudoThread {

	private static final ConcurrentMap<Integer, Object> locks = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		ExecutorService pool = new ThreadPoolExecutor(8, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

}
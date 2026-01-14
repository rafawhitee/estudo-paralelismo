


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EstudoSynchronized {
	
	private static final ConcurrentMap<Integer, Object> locks = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		Runnable hunt5 = () -> processarHuntFullSynchronized(5, 4000);
		Runnable hunt5b = () -> processarHuntFullSynchronized(5, 3000);
		Runnable hunt3 = () -> processarHuntFullSynchronized(3, 2000);

		new Thread(hunt5, "Thread A").start();
		sleep(100); // pequena diferença de tempo
		new Thread(hunt5b, "Thread B").start();
		sleep(100); // pequena diferença de tempo
		new Thread(hunt3, "Thread C").start();
	}

	public static void processarHuntSynchronizedByHunt(int huntId, int tempoMs) {
		Object lock = locks.computeIfAbsent(huntId, id -> {
			System.out.println("Criando lock para hunt " + huntId);
			return new Object();
		});

		System.out.println(Thread.currentThread().getName() + " tentando entrar na hunt " + huntId);

		synchronized (lock) {
			try {
				System.out.println(Thread.currentThread().getName() + " ENTROU na hunt " + huntId);
				sleep(tempoMs);
			} finally {
				System.out.println(Thread.currentThread().getName() + " SAINDO da hunt " + huntId);
				locks.remove(huntId, lock);
			}
		}
	}

	public synchronized static void processarHuntFullSynchronized(int huntId, int tempoMs) {
		System.out.println(Thread.currentThread().getName() + " tentando entrar na hunt " + huntId);
		System.out.println(Thread.currentThread().getName() + " ENTROU na hunt " + huntId);
		sleep(tempoMs);
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}


}

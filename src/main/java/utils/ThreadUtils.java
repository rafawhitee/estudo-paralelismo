package utils;

public final class ThreadUtils {

	private ThreadUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static void printNameAndClass() {
		printNameAndClass(null);
	}

	public static void printNameAndClass(final String prefix) {
		System.out.println(String.format("%s (Thread %s - %s - %s)", prefix != null ? prefix : "",
				Thread.currentThread().getName(), Thread.currentThread().getClass().getName(),
				Thread.currentThread().isVirtual() ? "Virtual" : "Platform"));
	}

	public static void sleepSilently(final long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
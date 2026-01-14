package utils;

public class CustomAtomicInteger {
	
	private volatile Integer value;
	
	public CustomAtomicInteger() {
		this.value = 0;
	}
	
	public CustomAtomicInteger(Integer initialValue) {
		this.value = initialValue;
	}
	
	public int incrementAndGet() {
		this.increment();
		return this.value;
	}
	
	public void increment() {
		this.sum(1);
	}
	
	public synchronized void sum(Integer value) {
		this.value += value;
	}
	
	public Integer decrementAndGet() {
		this.decrement();
		return this.value;
	}
	
	public void decrement() {
		this.subtract(1);
	}
	
	public synchronized void subtract(Integer value) {
		this.value -= value;
	}
	
	public Integer get() {
		return this.value;
	}

}
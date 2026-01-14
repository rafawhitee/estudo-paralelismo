import java.util.concurrent.atomic.AtomicInteger;

public class EstudoAtomic {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Começo do Caso 001 \n");
		caso001();
		System.out.println("\nFim do Caso 001");
		System.out.println("----- \n");
	}

	/*
	 * 
	 * AtomicInteger é uma classe que fornece operações atômicas para inteiros
	 * se fosse o int ou Integer, poderia ocorrer condição de corrida
	 * duas threads poderiam ler o mesmo valor antes de qualquer uma atualizar
	 * com o atomic, isso é controlado, estilo synchronized mas mais eficiente
	 * 
	 * 
	 * OBS: mesmo se for volatile, não garante atomicidade 
	 * o volatile só garante leitura atualizada entre as threads
	 */
	private static void caso001() throws InterruptedException {
		AtomicInteger contador = new AtomicInteger(0);

		Thread t1 = new Thread(() -> contador.incrementAndGet());
		Thread t2 = new Thread(() -> contador.incrementAndGet());

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		// Sempre 2
		// se não fosse atomic, poderia ser 1
		System.out.println(contador.get()); 
	}

}
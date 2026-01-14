public class EstudoThread {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Começo do Caso 001 \n");
		caso001();
		System.out.println("\nFim do Caso 001");
		System.out.println("----- \n");

		System.out.println("Começo do Caso 002 \n");
		caso002();
		System.out.println("\nFim do Caso 002");
		System.out.println("----- \n");

		System.out.println("Começo do Caso 003 \n");
		caso003();
		System.out.println("\nFim do Caso 003");
		System.out.println("----- \n");
	}

	/*
	 * 
	 * start inicia a thread, chamando o método run em uma nova thread de execução
	 * 
	 */
	private static void caso001() throws InterruptedException {
		Runnable tarefa = () -> {
			try {
				System.out.println("Thread " + Thread.currentThread().getName() + " está executando a tarefa.");
				Thread.sleep(1000);
				System.out.println("Fim da Thread " + Thread.currentThread().getName() + ".\n");
			} catch (InterruptedException e) {

			}
		};
		Thread thread1 = new Thread(tarefa, "Thread 1 - Caso 001");
		Thread thread2 = new Thread(tarefa, "Thread 2 - Caso 001");

		// inicia as duas
		thread1.start();
		thread2.start();
	}

	/*
	 * 
	 * o join espera terminar se reparou no caso 001, o sysout ficou esquisito pois
	 * no momento do start, eles começaram mas a thread principal não esperou elas
	 * terminarem
	 * 
	 */
	private static void caso002() throws InterruptedException {
		Runnable tarefa = () -> {
			try {
				System.out.println("Thread " + Thread.currentThread().getName() + " está executando a tarefa.");
				Thread.sleep(1000);
				System.out.println("Fim da Thread " + Thread.currentThread().getName() + ".");
			} catch (InterruptedException e) {

			}
		};
		Thread thread1 = new Thread(tarefa, "Thread 1 - Caso 002");
		Thread thread2 = new Thread(tarefa, "Thread 1 - Caso 002");

		// inicia as duas
		thread1.start();
		thread2.start();

		// agora espera ambas terminarem
		thread1.join();
		thread2.join();
	}

	/*
	 * 
	 * quando é iniciada uma nova thread, antes do start a thread nova faz uma cópia
	 * das variáveis para um contexto interno dela
	 * 
	 */
	private static void caso003() throws InterruptedException {
		int variavelPrincipal1 = 10; // variável local
		// a lambda captura a variável local (efetivamente final), lendo seu valor de 10
		// por isso variáveis locais precisam ser final ou efetivamente final
		Thread thread1 = new Thread(() -> {
			System.out.println("variavelPrincipal1 dentro da thread: " + variavelPrincipal1);
		});
		// não pode alterar a variável depois do start
		// vai falar que precisa ser final ou efetivamente final
		//variavelPrincipal1 = 3;
		thread1.start();
		thread1.join();
	}
	
}
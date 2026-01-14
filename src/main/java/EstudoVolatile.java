public class EstudoVolatile {

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
	 * variável volatile garante que a thread worker veja a atualização feita pela
	 * thread main sem o volatile, o running poderia ser cacheada na thread worker,
	 * e ela nunca veria a mudança para false
	 * 
	 */
	private static volatile boolean running = true;
	
	private static void caso001() throws InterruptedException {
		Thread worker = new Thread(() -> {
			while (running) {
				System.out.println("Trabalhando...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			System.out.println("Worker parou!");
		});

		worker.start();

		Thread.sleep(2000); // deixa rodar 2s
		System.out.println("Parando o worker...");
		running = false; // sinaliza para parar
	}
	
	/*
	 * 
	 * leitura/escrita
	 * uma thread atualiza o valor de sharedValue, enquanto outra thread lê esse valor
	 * 
	 */
	private static volatile int sharedValue = 0;
	
	private static void caso002() throws InterruptedException {
		Thread writer = new Thread(() -> {
			for (int i = 1; i <= 5; i++) {
				sharedValue = i;
				System.out.println("Escreveu: " + i);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			}
		});

		Thread reader = new Thread(() -> {
			// cria uma cópia local para evitar múltiplas leituras de sharedValue
			// se for diferente da última, é porque mudou o valor
			int localValue = sharedValue;
			while (localValue < 5) {
				if (localValue != sharedValue) {
					System.out.println("Leu: " + sharedValue);
					localValue = sharedValue;
				}
			}
		});

		// começa as threads
		writer.start();
		reader.start();

		// garante que ambas as threads terminem antes de sair do método
		writer.join();
		reader.join();
	}
	
	/*
	 * 
	 * singleton (double-check locking)
	 * 
	 * o double-check locking usa uma variável volatile para garantir que a instância seja única para todas as threads
	 * a primeira verificação evita o custo do lock quando a instância já foi criada
	 * a segunda verificação dentro do bloco synchronized garante que apenas uma thread crie a instância
	 * 
	 */
	private static void caso003() throws InterruptedException {
		Runnable task = () -> {
            Singleton s = Singleton.getInstance();
            System.out.println(s);
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
	}

}
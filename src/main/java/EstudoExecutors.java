import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.ThreadUtils;

public class EstudoExecutors {

	private static final ExecutorService GLOBAL_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

	public static void main(String[] args) throws Exception {
		System.out.println("-----");
		System.out.println("Começo do Caso 001 \n");
		ThreadUtils.printNameAndClass("Main do Caso 001");
		caso001();
		System.out.println("\nFim do Caso 001");
		System.out.println("----- \n");

		System.out.println("-----");
		System.out.println("Começo do Caso 002 \n");
		ThreadUtils.printNameAndClass("Main do Caso 002");
		caso002();
		System.out.println("\nFim do Caso 002");
		System.out.println("----- \n");

		System.out.println("-----");
		System.out.println("Começo do Caso 003 \n");
		ThreadUtils.printNameAndClass("Main do Caso 003");
		caso003();
		System.out.println("\nFim do Caso 003");
		System.out.println("----- \n");

		System.out.println("\n\n ########## Fim EstudoExecutors ##########");
	}

	/*
	 * 
	 * ele usa o executor de threads virtuais para rodar um CompletableFuture mas
	 * usa o executor global
	 * 
	 */
	private static void caso001() throws Exception {
		// apesar de criar o executor, a main thread na thread principal (não virtual)
		ThreadUtils.printNameAndClass("Dentro do caso 001");

		// CompletableFuture rodando em thread virtual (sem try-resource)
		CompletableFuture.supplyAsync(() -> {
			// como esse future, foi passado o executor de threads virtuais
			// ele será executado em uma thread virtual
			ThreadUtils.printNameAndClass("Dentro do CompletableFuture do caso 001");
			ThreadUtils.sleepSilently(5000); // Simula trabalho
			System.out.println("CompletableFuture do caso 001 concluído, retornando o resultado.");
			return "Resultado do CompletableFuture caso 001";
		}, GLOBAL_EXECUTOR);

		// Podemos continuar fazendo algo no main thread
		System.out.println(
				"caso001 continua executando, e como não tinha mais nada para executar, foi finalizado, pois como não existe o try-resource aqui, "
						+ " ele deixa a thread virtual rodando e ninguém espera por ela");

	}

	/*
	 * 
	 * ele usa o executor de threads virtuais para rodar um CompletableFuture mas
	 * usa o executor global
	 * 
	 */
	private static void caso002() throws Exception {
		// apesar de criar o executor, a main thread na thread principal (não virtual)
		ThreadUtils.printNameAndClass("Dentro do caso 002");

		// CompletableFuture rodando em thread virtual
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			// como esse future, foi passado o executor de threads virtuais
			// ele será executado em uma thread virtual
			ThreadUtils.printNameAndClass("Dentro do CompletableFuture do caso 002");
			ThreadUtils.sleepSilently(5000); // Simula trabalho
			System.out.println("CompletableFuture do caso 002 concluído, retornando o resultado.");
			return "Resultado do CompletableFuture caso 002";
		}, GLOBAL_EXECUTOR);

		// Podemos continuar fazendo algo no main thread
		System.out.println("caso002 continua executando...");

		// Aguarda resultado do CompletableFuture
		// aqui a thread principal vai aguardar o resultado
		// então fica bloqueada até o resultado estar disponível
		System.out.println(
				"caso002 esperando pelo resultado do future (bloqueando a thread principal que chamou o caso002)...");
		String resultado = future.get();
		System.out.println("Resultado: " + resultado);

	}

	/*
	 * 
	 * o Executor é um gerenciador de threads quando é usado no try-resource, ele só
	 * é fechado quando o bloco try termina quando você fecha, você ta dizendo para
	 * a JVM: "não vou mais usar essas threads" nesse exemplo abaixo, o
	 * CompletableFuture é iniciado em uma thread virtual, mas esperará o
	 * try-resource fechar para terminar
	 * 
	 */
	private static void caso003() throws Exception {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			// apesar de criar o executor, a main thread na thread principal (não virtual)
			ThreadUtils.printNameAndClass("Dentro do caso 002");

			// CompletableFuture rodando em thread virtual
			CompletableFuture.supplyAsync(() -> {
				// como esse future, foi passado o executor de threads virtuais
				// ele será executado em uma thread virtual
				ThreadUtils.printNameAndClass("Dentro do CompletableFuture do caso 003");
				ThreadUtils.sleepSilently(3000); // Simula trabalho
				System.out.println("CompletableFuture caso 003 concluído, retornando o resultado.");
				return "Resultado do CompletableFuture caso 003";
			}, executor);

			// Podemos continuar fazendo algo no main thread
			System.out.println("Future continua executando o try-resource do caso003 acabou...");
		}
		System.out.println("Executor fechado, caso003 terminou");
	}

}
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.ThreadUtils;

public class EstudoCompletableFuture {

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

		System.out.println("\n\n ########## Fim do EstudoCompletableFuture ##########");
	}

	/*
	 * 
	 * ele inicia um CompletableFuture que roda em uma thread virtual mas não espera
	 * pelo resultado, apenas imprime internamente na thread virtual mas ele irá
	 * bloquear a thread principal até o executor ser fechado (try-resource)
	 * 
	 */
	private static void caso001() throws Exception {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			// apesar de criar o executor, a main thread na thread principal (não virtual)
			ThreadUtils.printNameAndClass("Dentro do caso 001");

			// CompletableFuture rodando em thread virtual
			CompletableFuture.supplyAsync(() -> {
				// como esse future, foi passado o executor de threads virtuais
				// ele será executado em uma thread virtual
				ThreadUtils.printNameAndClass("Dentro do CompletableFuture do caso 001");
				ThreadUtils.sleepSilently(2000); // Simula trabalho
				System.out.println("CompletableFuture concluído, retornando o resultado.");
				return "Resultado do CompletableFuture";
			}, executor);

			// Podemos continuar fazendo algo no main thread
			System.out.println("Future continua executando o try-resource do caso001 acabou...");
		}
		System.out.println("Executor fechado, caso001 terminou");
	}

	/*
	 * 
	 * ele inicia um CompletableFuture que roda em uma thread virtual e na thread
	 * principal aguarda o resultado com future.get() ele iria esperar de qualquer
	 * jeito, pois ele iria esperar o executor ser fechado (try-resource)
	 * 
	 */
	private static void caso002() throws Exception {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			// apesar de criar o executor, a main thread na thread principal (não virtual)
			ThreadUtils.printNameAndClass("Dentro do caso 002");

			// CompletableFuture rodando em thread virtual
			CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
				// como esse future, foi passado o executor de threads virtuais
				// ele será executado em uma thread virtual
				ThreadUtils.printNameAndClass("Dentro do CompletableFuture do caso 002");
				ThreadUtils.sleepSilently(2000); // Simula trabalho
				System.out.println("CompletableFuture concluído, retornando o resultado.");
				return "Resultado do CompletableFuture";
			}, executor);

			// Podemos continuar fazendo algo no main thread
			System.out.println("caso002 continua executando...");

			// Aguarda resultado do CompletableFuture
			// aqui a thread principal vai aguardar o resultado
			// mas ele vai esperar de qualquer jeito, pois o try-resource só fecha o
			// executor
			// se não tivesse o try-resource, aí sim faria diferença
			String resultado = future.get();
			System.out.println("Resultado: " + resultado);
		}
	}

}
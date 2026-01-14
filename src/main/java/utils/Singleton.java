package utils;
/** 
 * 
 * Implementação do padrão Singleton com Double-Checked Locking em Java.
 * Usada no EstudoVolatile (caso 003)
 * 
 **/
public class Singleton {
	
	private static volatile Singleton instance;
	
	private Singleton() {
        System.out.println("Singleton criado!");
    }

    public static Singleton getInstance() {
        if (instance == null) { // primeira verificação sem lock
            synchronized (Singleton.class) {
                if (instance == null) { // segunda verificação dentro do lock
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
	
}

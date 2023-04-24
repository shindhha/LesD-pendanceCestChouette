package controleur;

import java.util.LinkedList;

/**
 * Processus permettant de gérer les processus de test.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class GestionTest extends Thread {

	/** Vrai si un processus de test est en cours d'éxécution. */
	private boolean testEnCours = false;
	
	/** Liste des processus de test en attente. */
	private LinkedList<Thread> testsEnAttentes = new LinkedList<Thread>();
	
	/**
	 * Ajoute un processus de test à la liste.
	 * @param test
	 */
	public void add(Thread test) {
		testsEnAttentes.add(test);
	}
	
	/**
	 * Débute un test.
	 */
	public void debutTest() {
		testEnCours = true;
	}
	
	/**
	 * Termine un test.
	 */
	public void finTest() {
		testEnCours = false;
	}
	
	/**
	 * Arrête tous les tests en attente.
	 */
	public void interruptTests() {
		testsEnAttentes.clear();
		finTest();
	}
	
	/**
	 * boucle de gestion des tests.
	 */
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if (!testEnCours && !testsEnAttentes.isEmpty()) {
				testsEnAttentes.removeFirst().start();
			}
		}
	}
}

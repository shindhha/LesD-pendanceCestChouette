package modele;


import com.fazecast.jSerialComm.SerialPort;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet d'ouvrir une connexion SSH avec la carte de test.
 * Elle permet de lancer des tests sur la carte.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class CommunicationSSH {

	/** Session de connexion à la carte. */
	private Session session;

	/** Canal de communication avec la carte. */
	private ChannelShell channel;

	/** Entrée de la carte venant du processus. */
	private BufferedReader in;

	/** Sortie du processus vers la carte. */
	private PrintStream out;

	/** Le login de connexion. */
	private String user;

	/** host L'adresse IP de connexion (10.15.150.61). */
	private String host;

	/** Le port de connexion (22). */
	private int port;

	/** Le mot de passe de connexion. */
	private String password;

	/** Vrai si les tests ont été initialisé. */
	private boolean testInit;

	/**
	 * Initialisation des informations nécessaire à la connexion.
	 * @param user
	 * @param host
	 * @param port
	 * @param password
	 */
	public CommunicationSSH(String user, String host, int port,
			String password) {
		this.user = user;
		this.host = host;
		this.port = port;
		this.password = password;
		this.testInit = false;
	}

	/**
	 * Ouvre une connexion avec le serveur distant.
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void etablirConnexion() throws JSchException, IOException, InterruptedException {
		if (session == null || !session.isConnected()) {
			session = new JSch().getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");

			session.connect();
			channel = (ChannelShell) session.openChannel("shell");

			in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			out = new PrintStream(channel.getOutputStream(), true);

			channel.connect();

			execCmd("su", 300);
			execCmd("actia", 300);

			if (!testInit) {
				initialisationDesTests();
			}
		}
	}

	/** Fermeture de la connexion avec la carte. 
	 * @throws IOException 
	 * @throws InterruptedException */
	public void fermerConnexion() throws IOException, InterruptedException {
		if (session != null) { 
			session.disconnect();
			session = null;
		}
		if (channel != null) {
			channel.disconnect();
			channel = null;
		}
		if (in != null) {
			in.close();
			in = null;
		}
		if (out != null) {
			out.close();
			out = null;
		}
	}

	/** 
	 * Exécution de commandes préliminaires, 
	 * nécessaires au bon fonctionnement des tests.
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws JSchException 
	 */
	public void initialisationDesTests() throws IOException, InterruptedException, JSchException {

		etablirConnexion();
		execCmd("killall principal", 300);
		execCmd("principal -killall", 300);
		execCmd("killall wdogSw", 300);
		execCmd("./wdog.sh &", 600);
		lireEntree();
		testInit = true;

	}

	/* ----------- LES TESTS SUR LA CARTE ----------- */

	/**
	 * Allume toutes les leds vertes et éteint les rouges.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public void testLedsVertes() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("gpio-write -gpio=LED_HEARTBEAT 1", 200);
		execCmd("gpio-write -gpio=LED_ALARME_VERT 1", 200);
		execCmd("gpio-write -gpio=LED_ALARME_ROUGE 0", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_VERT 1", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_ROUGE 0", 200);
		execCmd("gpio-write -gpio=LED_ALARME_MSA 0", 200);
		execCmd("gpio-write -gpio=LED_POWER_SEQ 1", 200);
		lireEntree(); // Vide le cache
	}

	/**
	 * Allume toutes les leds rouges.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public void testLedsRouges() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("gpio-write -gpio=LED_HEARTBEAT 1", 200);
		execCmd("gpio-write -gpio=LED_ALARME_VERT 0", 200);
		execCmd("gpio-write -gpio=LED_ALARME_ROUGE 1", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_VERT 0", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_ROUGE 1", 200);
		execCmd("gpio-write -gpio=LED_ALARME_MSA 1", 200);
		execCmd("gpio-write -gpio=LED_POWER_SEQ 1", 200);
		lireEntree(); // Vide le cache
	}

	/**
	 * Eteint toutes les leds.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public void testLedsOff() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("gpio-write -gpio=LED_HEARTBEAT 0", 200);
		execCmd("gpio-write -gpio=LED_ALARME_VERT 0", 200);
		execCmd("gpio-write -gpio=LED_ALARME_ROUGE 0", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_VERT 0", 200);
		execCmd("gpio-write -gpio=LED_CAMERA_ROUGE 0", 200);
		execCmd("gpio-write -gpio=LED_ALARME_MSA 0", 200);
		execCmd("gpio-write -gpio=LED_POWER_SEQ 0", 200);
		lireEntree(); // Vide le cache
	}

	/**
	 * Test du fonctionnement des leds D1 et D4.
	 * @return 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public boolean[] testSeqRecorder(int numCOM) throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		boolean d1Ok, d4Ok;
		byte etat_led;
		/* Les deux leds à 0 */
		execCmd("gpio-write -gpio=LED_POWER_SEQ 0", 1000);
		execCmd("gpio-write -gpio=CMD_POWER_RECORDER 0", 1000);
		etat_led = demander_etat_leds(numCOM);
		d1Ok = (etat_led & ((byte) 0x01)) == 0;
		d4Ok = (etat_led & ((byte) 0x02)) == 0;
		/* D1 à 1 et D4 à 0 */
		execCmd("gpio-write -gpio=LED_POWER_SEQ 1", 1000);
		etat_led = demander_etat_leds(numCOM);
		d1Ok = d1Ok && (etat_led & ((byte) 0x01)) == 1;
		d4Ok = d4Ok && (etat_led & ((byte) 0x02)) == 0;
		/* D1 à 0 et D4 à 1 */
		execCmd("gpio-write -gpio=LED_POWER_SEQ 0", 1000);
		execCmd("gpio-write -gpio=CMD_POWER_RECORDER 1", 1000);
		etat_led = demander_etat_leds(numCOM);
		d1Ok = d1Ok && (etat_led & ((byte) 0x01)) == 0;
		d4Ok = d4Ok && (etat_led & ((byte) 0x02)) == 2;
		/* Les deux à 1 */
		execCmd("gpio-write -gpio=LED_POWER_SEQ 1", 1000);
		etat_led = demander_etat_leds(numCOM);
		d1Ok = d1Ok && (etat_led & ((byte) 0x01)) == 1;
		d4Ok = d4Ok && (etat_led & ((byte) 0x02)) == 2;

		lireEntree(); // Vide le cache

		return new boolean[] {d1Ok, d4Ok};
	}

	/**
	 * Teste l'accès au port SATA1.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public boolean testSataPort1() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("./t_hdda.sh", 16000);
		String reponse = lireEntree();

		//System.out.println(reponse);

		/* On compte le nombre de Transfert réussi */
		int nbOk = 0;
		for (String mot : reponse.split("\\s+")) {
			if (mot.equals("OK,")) { nbOk++; }
		}

		/* Si 5 OKs le test est validé */
		return nbOk >= 5;
	}

	/**
	 * Teste l'accès au port SATA2.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public boolean testSataPort2() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("./t_hddb.sh", 16000);
		String reponse = lireEntree();

		//System.out.println(reponse);

		/* On compte le nombre de Transfert réussi */
		int nbOk = 0;
		for (String mot : reponse.split("\\s+")) {
			if (mot.equals("OK,")) { nbOk++; }
		}

		/* Si 5 OKs le test est validé */
		return nbOk >= 5;
	}

	/**
	 * Teste le fonctionnement de l'accès réseau.
	 * @throws Exception
	 */
	public float[] testEthernet() throws Exception {
		etablirConnexion();
		String iperf = "./outils/iperf3.exe";
		String[] cmd = {iperf, "-c", host, "-u", "-b", "250M", "-t", "20"};
		execCmd("iptables -F", 500);
		execCmd("iptables -X", 500);
		execCmd("iptables -P INPUT ACCEPT", 500);
		execCmd("iperf3 -s -i 1 -1", 2000);
		Process p = Runtime.getRuntime().exec(cmd); // Lance un processus exécutant iperf
		Thread.sleep(22000);
		p.destroy();
		String reponse = lireEntree();
		//System.out.println(reponse);
		String[] entree = reponse.split("\\s+");
		/* Récupération du débit */
		boolean debit1 = true; // Le premier débit peut être inférieur à 200Mb/s de temps en temps
		float debit = 0;
		int nbDebit = 0;
		float perte = 0;
		int nbPerte = 0;
		float nbMBytes;
		String mot;
		for (int i = 1; i < entree.length - 1; i++) {
			mot = entree[i];
			if (mot.equals("MBytes")) {
				nbMBytes = Float.parseFloat(entree[i-1]);
				if (!debit1 && !(nbMBytes < 25)) {
					debit += Float.parseFloat(entree[i+1]);
					nbDebit++;
				}
				debit1 = false;
			} else if (mot.length() > 2 && mot.charAt(mot.length()-2) == '%') {
				perte += Float.parseFloat(mot.substring(1, mot.length()-2));
				nbPerte++;
			}
		}

		if (nbDebit != 0 && nbPerte != 0) {
			debit = debit / nbDebit;
			perte = perte / nbPerte;
		} else {
			debit = 0;
			perte = 100;
		}

		return new float[] {debit, perte};
	}

	/**
	 * Teste la DATAFLASH.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public boolean testDataflash() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("dataflash -autotest", 30000);
		String reponse = lireEntree();
		boolean OK = false;
		for (String mot : reponse.split("\\s+")) {
			if (mot.equals("OK")) { OK = true; }
		}

		return OK;
	}

	/**
	 * Test de la zone RAM et test écriture/lecture heure.
	 * On teste 2 fois car il peut y avoir 1 s de décalage.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException 
	 */
	public boolean[] testRTC() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		int nbTest = 0;
		boolean testOk[] = new boolean[2];
		do {
			execCmd("./t_rtc.sh", 14000);
			String reponse = lireEntree();
			//System.out.println(reponse);
			Pattern p = Pattern.compile("RAM OK");
			Matcher m = p.matcher(reponse);
			testOk[0] = m.find();
			p = Pattern.compile("test OK");
			m = p.matcher(reponse);
			testOk[1] = m.find();
			nbTest++;
		} while (nbTest < 2 && !(testOk[0] && testOk[1]));

		return testOk;
	}

	/**
	 * Teste l'écriture/relecture d'une partie de la mémoire.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSchException
	 */
	public boolean testFram() throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		execCmd("fram -autotest", 5000);
		Pattern p = Pattern.compile("AUTOTEST FRAM : OK");
		String reponse = lireEntree();
		Matcher m = p.matcher(reponse);
		boolean testOk = m.find();

		return testOk;
	}

	/**
	 * Vérifie que les températures sont cohérentes.
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public float[] testTemperature() throws JSchException, IOException, InterruptedException {
		etablirConnexion();
		execCmd("temperature -cpu -ds1621", 1500);
		String reponse = lireEntree();
		//System.out.println(reponse);
		String[] entree = reponse.split("\\s+");
		float tempDS = 0;
		float tempCPU = 0;
		for (int i = 0; i < entree.length - 1; i++) {
			if (entree[i].equals("DS1621:")) {
				tempDS = Float.parseFloat(entree[i+1]);
				//System.out.println(tempDS);
			} else if (entree[i].equals("CPU:")) {
				tempCPU = Float.parseFloat(entree[i+1]);
				//System.out.println(tempCPU);
			}
		}
		return new float[] {tempDS, tempCPU};
	}

	/**
	 * Test du bon fonctionnement des switchs.
	 * @param switchsAffichage les noms des switchs sur la carte
	 * @param switchsABouger les switchs sur le microprocesseur
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws JSchException 
	 */
	public boolean[] testSwitchs(String[] switchsAffichage, SWITCH[] switchsABouger, int numCOM) 
			throws IOException, InterruptedException, JSchException {
		etablirConnexion();
		String reponse;
		boolean[] switchOk = new boolean[switchsABouger.length];
		Pattern p;
		Matcher m;
		for (int i = 0; i < switchsABouger.length; i++) {
			bougerSwitch(switchsABouger[i], false, numCOM);
			Thread.sleep(200);
			execCmd("gpio-read -gpio=" + switchsAffichage[i], 500);
			reponse = lireEntree();
			//System.out.println(reponse);
			p = Pattern.compile(": 0");
			m = p.matcher(reponse);
			switchOk[i] = m.find();

			bougerSwitch(switchsABouger[i], true, numCOM);
			Thread.sleep(200);
			execCmd("gpio-read -gpio=" + switchsAffichage[i], 500);
			reponse = lireEntree();
			//System.out.println(reponse);
			p = Pattern.compile(": 1");
			m = p.matcher(reponse);
			switchOk[i] = switchOk[i] & m.find();

			//System.out.println(switchsAffichage[i] + " " + (switchOk[i] ? "OK" : "KO"));
		}

		return switchOk;
	}

	/**
	 * Test du redémarrage automatique de la carte.
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int testRemoveWatchdog() throws JSchException, IOException, InterruptedException {
		etablirConnexion();
		InetAddress address = InetAddress.getByName(host);
		execCmd("killall wdog.sh", 200);
		final int ATTENTE_MAX = 165;	// Temps maximal à attendre le redémarrage
		/* Attente de l'extinction */
		int i = 0;
		while (i < ATTENTE_MAX && address.isReachable(1000)) {
			i++;
			Thread.sleep(1000);
		}
		//		/* Attente du redémarrage */
		//		i = 0;
		//		while (i < ATTENTE_MAX && !address.isReachable(1000)) {
		//			i++;
		//			Thread.sleep(1000);
		//		}
		//		testOk = testOk && i < ATTENTE_MAX;

		lireEntree(); // Vide le cache

		testInit = false;

		return i;
	}

	/**
	 * Vérification de la bonne réception d'un message émis pendant 10s.
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean testRsOn() throws JSchException, IOException, InterruptedException {
		etablirConnexion();
		execCmd("gpio-write -gpio=RS485_TX_EN 1", 500);
		execCmd("test_rs485_fdp", 12000);
		execCmd("gpio-write -gpio=RS485_TX_EN 0", 500);

		String reponse = lireEntree();
		//System.out.println(reponse);

		/* On regarde que le nombre de test KO est nul */
		boolean testOk = true;
		String[] entree = reponse.split("\\s+");
		for (int i = 0; i < entree.length-1; i++) {
			if (entree[i].equals("KO")) {
				testOk = testOk && entree[i+1].equals("0");
			}
		}

		return testOk;

	}

	//	public boolean testRsOff() throws JSchException, IOException, InterruptedException {
	//		etablirConnexion();
	//		execCmd("gpio-write -gpio=RS485_TX_EN 1", 500);
	//		execCmd("test_rs485_fdp", 12000);
	//		execCmd("gpio-write -gpio=RS485_TX_EN 0", 500);
	//		
	//		String reponse = lireEntree();
	//		//System.out.println(reponse);
	//		
	//		/* On regarde que le nombre de test OK est nul */
	//		boolean testOk = true;
	//		String[] entree = reponse.split("\\s+");
	//		for (int i = 0; i < entree.length-1; i++) {
	//			if (entree[i].equals("OK")) {
	//				testOk = testOk && entree[i+1].equals("0");
	//			}
	//		}
	//		
	//		return testOk;
	//	}

	/* ---------------------------------------------- */

	/* -------------- METHODES UTILES --------------- */

	private static final int BUFF_SIZE = 1024;

	/**
	 * Lit tout le contenu de l'entrée .
	 * @return Une chaine contenant l'entrée.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String lireEntree() throws IOException, InterruptedException {
		String entree = "";
		char[] buff = new char[BUFF_SIZE];
		while(in.ready() && (in.read(buff, 0, BUFF_SIZE) != -1)) {
			entree += new String(buff);
		}
		return entree;
	}

	/**
	 * Exécute une commande et attend la réponse.
	 * @param cmd La commande à exécuter.
	 * @param attente Le temps à attendre après exécution de la commande en ms.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void execCmd(String cmd, int attente) throws IOException, InterruptedException {
		out.println(cmd);
		Thread.sleep(attente);
	}

	/**
	 * Les switchs et leur numéro correspondant.
	 */
	public enum SWITCH {
		S1_1(0), S1_2(1), S2_1(2), S2_2(3), S3_1(4), S3_2(5), S4_1(6), S4_2(7),
		S5_1(8), S5_2(9), S6_1(10), S6_2(11), S7_1(12), S7_2(13), S8_1(14),
		S8_2(15), S9_1(16), S9_2(17), S10_1(18), S10_2(19), ROT1_1(20),
		ROT1_2(21), ROT1_3(22), ROT2_1(23), ROT2_2(24), ROT2_3(25);

		private int num;

		private SWITCH(int num) {
			this.num = num;
		}

		public int getNum() {
			return num;
		}
	}

	/**
	 * Change l'état des switchs.
	 * @param switchs les switchs à changer voir enum SWITCH.
	 * @param on position à 1 ou à 0.
	 * @throws InterruptedException 
	 */
	public static void bougerSwitch(SWITCH switch_, boolean on, int numCOM) throws InterruptedException {
		SerialPort[] portList = SerialPort.getCommPorts();
		SerialPort comPort = null;
		Pattern pattern = Pattern.compile("COM" + numCOM);
		Matcher m;
		for (SerialPort port : portList) {
			m = pattern.matcher(port.getDescriptivePortName());
			if (m.find()) {
				comPort = port;
			}
		}
		if (comPort != null) {
			comPort.setBaudRate(115200);
			comPort.openPort();
			Thread.sleep(100);
			byte data = (byte) (192 + (switch_.getNum() << 1) + (on ? 1 : 0));
			//System.out.println(data);
			try {
				byte[] msg = {(byte) 0x02, data};
				comPort.writeBytes(msg, msg.length);
			} catch (Exception e) { e.printStackTrace(); }
			comPort.closePort();
		}
	}

	/**
	 * Envoie un message en serie au microprocesseur pour lui demander
	 * l'etat (0 ou 1) des leds D1 et D4.
	 */
	public static byte demander_etat_leds(int numCOM) throws InterruptedException {
		final int DELAI_MAX = 200;
		byte[] readBuffer = new byte[1];
		SerialPort[] portList = SerialPort.getCommPorts();
		SerialPort comPort = null;
		Pattern pattern = Pattern.compile("COM" + numCOM);
		Matcher m;
		for (SerialPort port : portList) {
			m = pattern.matcher(port.getDescriptivePortName());
			if (m.find()) {
				comPort = port;
			}
		}
		if (comPort != null) {
			comPort.setBaudRate(115200);
			comPort.openPort();
			Thread.sleep(500);
			try {
				/* Demande des etats */
				byte[] msg = {(byte) 0x04};
				comPort.writeBytes(msg, msg.length);
				/* Attente de réponse */
				int delai = 0;
				while (comPort.bytesAvailable() == 0 && delai++ < DELAI_MAX) {
					Thread.sleep(20);
				}
				/* Lecture de la réponse */
				if (delai < DELAI_MAX) {
					comPort.readBytes(readBuffer, 1);
				}
			} catch (Exception e) { e.printStackTrace(); }
			comPort.closePort();
		}
		return readBuffer[0];
	}
	/* ---------------------------------------------- */
}
package modele;

/**
 * Cette classe permet de stocker toutes les informations
 * relative au PV de test. Elles sont mises à jour au cours
 * de l'éxécution de l'application.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class PV {
	
	/* Info de connexion */
	
	public static String login;
	
	public static String mdp;
	
	public static int port;
	
	public static String ip;
	
	public static int com;
	
	/* Info du PV */
	
	public static String date;
	
	public static String operateur;
	
	public static String codeModule;
	
	public static String numModule;
	
	/* Config du BIOS */
	
	public static String versionBIOS;
	
	public static boolean userBIOS;
	
	public static boolean cardBIOS;
	
	public static boolean audioBIOS;
	
	public static boolean bootBIOS;
	
	/* Résultats des tests */
	
	public static boolean[] testLeds;
	
	public static boolean[] testledsD;
	
	public static boolean[] testSwitchs;
	
	public static boolean testSATA1;
	
	public static boolean testSATA2;
	
	public static float[] testEthernetDebit;
	
	public static float[] testEthernetPerte;
	
	public static boolean[] testEthernetLeds;
	
	public static boolean testDATAFLASH;
	
	public static boolean[] testRTC;
	
	public static boolean testFRAM;
	
	public static float testTempDS;
	
	public static float testTempCPU;
	
	public static int testWatchdog;
	
	public static boolean testRS;
	
	/**
	 * Initialisation des valeurs du PV.
	 */
	public static void initPvVide() {
		login = "";
		mdp = "";
		port = 22;
		ip = "10.15.150.61";
		com = 0;
		date = "";
		operateur = "";
		codeModule = "";
		numModule = "";
		versionBIOS = "";
		testLeds = new boolean[3];
		testledsD = new boolean[2];
		testSwitchs = new boolean[18];
		testEthernetDebit = new float[4];
		testEthernetPerte = new float[4];
		testEthernetLeds = new boolean[2];
		testRTC = new boolean[2];
		testSATA1 = false;
		testSATA2 = false;
		testDATAFLASH = false;
		testFRAM = false;
		testTempDS = 0;
		testTempCPU = 0;
		testWatchdog = 165;
		testRS = false;
		
	}
}

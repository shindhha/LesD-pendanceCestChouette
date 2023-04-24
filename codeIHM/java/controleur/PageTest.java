package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.gluonhq.charm.glisten.control.ProgressIndicator;
import com.jcraft.jsch.JSchException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import modele.CommunicationSSH;
import modele.PV;
import modele.CommunicationSSH.SWITCH;
import vue.App;

/**
 * Controleur de la page pageTest.
 * Permet d'éxécuter les tests sur la carte.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class PageTest implements Initializable {

	/** Boutton permettant d'accèder à la page précédente. */
	@FXML
	Button bouttonGauche;

	/** Boutton permettant d'accèder à la page suivante. */
	@FXML
	Button bouttonDroite;

	/** Boutton permettant d'éxécuter le test des leds. */
	@FXML
	Button bouttonLeds;

	/** Boutton permettant d'éxécuter le test des leds D1 et D4. */
	@FXML
	Button bouttonLedsD;

	/** Boutton permettant d'exécuter le test des switchs. */
	@FXML
	Button bouttonSwitchs;

	/** Boutton permettant d'exécuter le test des ports SATA. */
	@FXML
	Button bouttonSATA;

	/** Boutton permettant d'exécuter le test des ports Ethernet. */
	@FXML
	Button bouttonEthernet;

	/** Boutton permettant d'exécuter le test de la DATAFLASH. */
	@FXML
	Button bouttonDATAFLASH;

	/** Boutton permettant d'exécuter le test de la RTC. */
	@FXML
	Button bouttonRTC;

	/** Boutton permettant d'exécuter le test de la FRAM. */
	@FXML
	Button bouttonFRAM;

	/** Boutton permettant d'exécuter le test de la température. */
	@FXML
	Button bouttonTemperature;

	/** Boutton permettant d'exécuter le test du watchdog. */
	@FXML
	Button bouttonWatchdog;

	/** Boutton permettant d'exécuter le test de la liaison RS. */
	@FXML
	Button bouttonRS;

	/** Boutton permettant d'exécuter tous les tests. */
	@FXML
	Button bouttonTous;

	/** Indicateur de progression du test des leds. */
	@FXML
	ProgressIndicator progressLeds;

	/** Indicateur de progression du test des leds D1 et D4. */
	@FXML
	ProgressIndicator progressLedsD;

	/** Indicateur de progression du test des switchs. */
	@FXML
	ProgressIndicator progressSwitchs;

	/** Indicateur de progression du test des ports SATA. */
	@FXML
	ProgressIndicator progressSATA;

	/** Indicateur de progression du test des ports Ethernet. */
	@FXML
	ProgressIndicator progressEthernet;

	/** Indicateur de progression du test de la DATAFLASH. */
	@FXML
	ProgressIndicator progressDATAFLASH;

	/** Indicateur de progression du test de la RTC. */
	@FXML
	ProgressIndicator progressRTC;

	/** Indicateur de progression du test de la FRAM. */
	@FXML
	ProgressIndicator progressFRAM;

	/** Indicateur de progression du test de la température. */
	@FXML
	ProgressIndicator progressTemperature;

	/** Indicaeur de progression du test du watchdog. */
	@FXML
	ProgressIndicator progressWatchdog;

	/** Indicateur de progression du test de la liaison RS. */
	@FXML
	ProgressIndicator progressRS;
	
	/** Lien permettant d'afficher les détails du test des leds. */
	@FXML
	Hyperlink detailsLeds;
	
	/** Lien permettant d'afficher les détails du test des leds D1 et D4. */
	@FXML
	Hyperlink detailsLedsD;
	
	/** Lien permettant d'afficher les détails du test des switchs. */
	@FXML
	Hyperlink detailsSwitchs;
	
	/** Lien permettant d'afficher les détails du test de la liaison RS. */
	@FXML
	Hyperlink detailsRS;
	
	/** Lien permettant d'afficher les détails du test des ports SATA. */
	@FXML
	Hyperlink detailsSATA;
	
	/** Lien permettant d'afficher les détails du test des ports Ethernet. */
	@FXML
	Hyperlink detailsEthernet;
	
	/** Lien permettant d'afficher les détails du test de la DATADLASH. */
	@FXML
	Hyperlink detailsDATAFLASH;
	
	/** Lien permettant d'afficher les détails du test du RTC. */
	@FXML
	Hyperlink detailsRTC;
	
	/** Lien permettant d'afficher les détails du test du FRAM. */
	@FXML
	Hyperlink detailsFRAM;
	
	/** Lien permettant d'afficher les détails du test de la température. */
	@FXML
	Hyperlink detailsTemperature;
	
	/** Lien permettant d'afficher les détails du test du watchdog. */
	@FXML
	Hyperlink detailsWatchdog;
	

	/** Popup indiquant qu'une action est à effectuer. */
	private Alert actionAlert;

	/** Popup affichant les détails du test. */
	private Alert detailAlert;

	/** Vrai si l'action a été effectuée. */
	private boolean actionEffectuee;

	/** La communication SSH avec la carte. */
	public static CommunicationSSH com;

	/** Les switchs à tester pour la carte Tve Tav CPU. */
	private static SWITCH[] switchsABougerTveTav = new SWITCH[] {
			SWITCH.S6_2, SWITCH.S5_1, SWITCH.S5_2, SWITCH.ROT1_2,
			SWITCH.ROT1_3, SWITCH.ROT2_2, SWITCH.S1_2, SWITCH.S1_1,
			SWITCH.S3_2, SWITCH.S3_1, SWITCH.S2_2, SWITCH.S2_1,
			SWITCH.S8_1, SWITCH.S6_1
	};

	/** Le nom des switchs de la carte Tve Tav CPU. */
	private static String[] switchsAffichageTveTav = new String[] {
			"PRESENCE_V_BKP", "PRESENCE_12V_MSA", "PRESENCE_12V",
			"COM_ROT1_P1", "COM_ROT1_P2", "SEUIL_VIT", "CFG_MSA11_0",
			"CFG_MSA11_1", "BL_TRAIN", "RESERVE", "COUPLEMENT",
			"AUT_OP_DO", "AUT_OP_GA", "BL"
	};

	/** Les switchs à tester pour les cartes Hubview. */
	private static SWITCH[] switchsABougerHubview = new SWITCH[] {
			SWITCH.S6_2, SWITCH.S5_1, SWITCH.S5_2,
			SWITCH.ROT1_2, SWITCH.ROT1_3, SWITCH.ROT2_2, SWITCH.ROT2_3,
			SWITCH.S1_2, SWITCH.S1_1, SWITCH.S7_2, SWITCH.S7_1,
			SWITCH.S3_2, SWITCH.S3_1, SWITCH.S2_2, SWITCH.S2_1,
			SWITCH.S8_1, SWITCH.S8_2, SWITCH.S6_1
	};

	/** Le nom des switchs des carte Hubview. */
	private static String[] switchsAffichageHubview = new String[] {
			"PRESENCE_V_BKP", "PRESENCE_12V_MSA", "PRESENCE_12V",
			"COM_ROT1_P1", "COM_ROT1_P2", "COM_ROT2_P1", "COM_ROT2_P2",
			"CFG_MSA11_0", "CFG_MSA11_1", "CFG_MSA12_0", "CFG_MSA12_1",
			"CFG_MSA21_0", "CFG_MSA21_1", "CFG_MSA22_0", "CFG_MSA22_1",
			"PRESENCE_CPU_E", "PRESENCE_CPU_VIDEO", "SLOT_M_OU_E"
	};


	/**
	 * Affiche la page précédente (connexionCarte).
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void stagePrecedent(ActionEvent e) throws IOException {
		/* On met à jour le PV avec les infos de la page */

		/* On affiche la page précédente */
		Stage stage = (Stage) bouttonGauche.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/connexionCarte.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Connexion à la carte");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Affiche la page suivante (pagePV).
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void stageSuivant(ActionEvent e) throws IOException {
		Stage stage = (Stage) bouttonDroite.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/pagePV.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Génération du PV");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Affiche une alerte dans le cas où la connexion à
	 * la carte échoue.
	 */
	private void alerteConnexion() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Echec de la connexion");
		alert.setContentText("La connexion à la carte a échoué\n"
				+ "Vérifier qu'elle soit bien connectée à votre ordinateur");
		Stage stage  = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		alert.showAndWait();
	}

	/**
	 * Vrai si toutes les entrées du tableau sont vraies
	 */
	private static boolean testOK(boolean[] test) {
		boolean ok = true;
		for (boolean t : test) {
			ok = ok && t;
		}
		return ok;
	}

	/**
	 * Initialise les infos de la page.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (PV.codeModule.equals("X130379150")) {
			bouttonRS.setVisible(false);
		} else {
			bouttonRS.setVisible(true);
		}

		actionAlert = new Alert(Alert.AlertType.CONFIRMATION);
		actionAlert.setTitle("Action de l'opérateur");
		actionAlert.setResizable(false);
		Stage stage  = (Stage) actionAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);

		detailAlert = new Alert(Alert.AlertType.INFORMATION);
		detailAlert.setTitle("Détails du test");
		detailAlert.setResizable(false);
		detailAlert.setHeaderText("Détails du test");
		stage  = (Stage) detailAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
	}

	/**
	 * Popup qui demande à l'utilisateur de changer la position
	 * du cable LAN.
	 */
	private void popupActionCarte(String message) {
		actionAlert.setContentText(message);
		actionAlert.showAndWait();
		actionEffectuee = true;
	}

	/**
	 * Popup qui affiche les details du test.
	 */
	private void popupDetail(String message) {
		detailAlert.setContentText(message);
		detailAlert.show();
	}

	/** Lance le test des Leds. */
	@FXML
	private void testLeds(ActionEvent e) throws InterruptedException {
		bouttonLeds.setMouseTransparent(true);
		bouttonLeds.setFocusTraversable(false);
		progressLeds.setStyle("-fx-color: #2AA5FF");
		progressLeds.setVisible(true);
		progressLeds.setProgress(-1);
		Thread test = new ThreadLeds();
		App.gestionTest.add(test);
	}

	/** Processus exécutant le test des leds. */
	public class ThreadLeds extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean testLeds[] = new boolean[3];
			try {
				/* Test des leds vertes */
				com.testLedsVertes();
				Popup.reponseRecu = false;
				Platform.runLater(() -> {try {
					popupLedsVertes();
				} catch (IOException e) {
				}});
				while (!Popup.reponseRecu) {
					Thread.sleep(100);
				}
				testLeds[0] = Popup.testOk;
				/* Test des les rouges */
				com.testLedsRouges();
				Popup.reponseRecu = false;
				Platform.runLater(() -> {try {
					popupLedsRouges();
				} catch (IOException e) {
				}});
				while (!Popup.reponseRecu) {
					Thread.sleep(100);
				}
				testLeds[1] = Popup.testOk;
				/* Test des leds éteintes */
				com.testLedsOff();
				Popup.reponseRecu = false;
				Platform.runLater(() -> {try {
					popupLedsOff();
				} catch (IOException e) {
				}});
				while (!Popup.reponseRecu) {
					Thread.sleep(100);
				}
				testLeds[2] = Popup.testOk;

				com.fermerConnexion();
			} catch (IOException e) {
				testLeds[0] = false;
				testLeds[1] = false;
				testLeds[2] = false;
			} catch (InterruptedException e) {
				testLeds[0] = false;
				testLeds[1] = false;
				testLeds[2] = false;
			} catch (JSchException e) {
				testLeds[0] = false;
				testLeds[1] = false;
				testLeds[2] = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { progressLeds.setProgress(1.0); });
			if (testOK(testLeds)) {
				Platform.runLater(() -> { progressLeds.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressLeds.setStyle("-fx-color: #FF3715"); });
			}

			PV.testLeds = testLeds;
			detailsLeds.setVisible(true);
			
			App.gestionTest.finTest();
			bouttonTous.setDisable(false);
			bouttonLeds.setMouseTransparent(false);
			bouttonLeds.setFocusTraversable(true);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}


	/**
	 * Popup demandant si les leds vertes sont fonctionnelles.
	 * @throws IOException
	 */
	private void popupLedsVertes() throws IOException {
		Popup.reponseRecu = false;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/popupLedsVertes.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		stage.setTitle("Vérification des leds");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Popup demandant si les leds rouges sont fonctionnelles.
	 * @throws IOException
	 */
	private void popupLedsRouges() throws IOException {
		Popup.reponseRecu = false;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/popupLedsRouges.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		stage.setTitle("Vérification des leds");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Popup demandant si les leds éteintes sont fonctionnelles.
	 * @throws IOException
	 */
	private void popupLedsOff() throws IOException {
		Popup.reponseRecu = false;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/popupLedsOff.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		stage.setTitle("Vérification des leds");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Popup demandant si les leds du port RJ45 face avant sont fonctionnelles.
	 * @throws IOException
	 */
	private void popupLedsRJ45() throws IOException {
		Popup.reponseRecu = false;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/popupLedsRJ45.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		stage.setTitle("Vérification des leds");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Popup demandant si les leds du port M12 face avant sont fonctionnelles.
	 * @throws IOException
	 */
	private void popupLedsM12() throws IOException {
		Popup.reponseRecu = false;
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/popupLedsM12.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		stage.setTitle("Vérification des leds");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.showAndWait();
	}


	/**
	 * Lance le test des leds D1 et D4.
	 */
	@FXML
	private void testLedsD(ActionEvent e) {
		bouttonLedsD.setMouseTransparent(true);
		bouttonLedsD.setFocusTraversable(false);
		progressLedsD.setStyle("-fx-color: #2AA5FF");
		progressLedsD.setVisible(true);
		progressLedsD.setProgress(-1);
		Thread test = new ThreadLedsD();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test des leds D1 et D4.
	 */
	public class ThreadLedsD extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean[] testLeds;
			try {
				Chrono chrono = new Chrono(10000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressLedsD.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				testLeds = com.testSeqRecorder(PV.com);
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testLeds = new boolean[] {false, false};
			} catch (IOException e) {
				testLeds = new boolean[] {false, false};
			} catch (JSchException e) {
				testLeds = new boolean[] {false, false};
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressLedsD.progressProperty().unbind();
				progressLedsD.setProgress(1.0);
			});
			if (testOK(testLeds)) {
				Platform.runLater(() -> { progressLedsD.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressLedsD.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testledsD = testLeds;
			detailsLedsD.setVisible(true);
			
			bouttonTous.setDisable(false);
			bouttonLedsD.setMouseTransparent(false);
			bouttonLedsD.setFocusTraversable(true);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test des switchs.
	 */
	@FXML
	private void testSwitchs(ActionEvent e) {
		bouttonSwitchs.setMouseTransparent(true);
		bouttonSwitchs.setFocusTraversable(false);
		progressSwitchs.setStyle("-fx-color: #2AA5FF");
		progressSwitchs.setVisible(true);
		progressSwitchs.setProgress(-1);
		Thread test = new ThreadSwitchs();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test des switchs.
	 */
	public class ThreadSwitchs extends Thread {
		public void run() {
			try {
				bouttonTous.setDisable(true);
				bouttonGauche.setDisable(true);
				bouttonDroite.setDisable(true);
				App.gestionTest.debutTest();

				boolean[] testSwitch;


				try {
					Chrono chrono = new Chrono(40000);
					Thread th = new Thread(chrono);
					Platform.runLater(() -> { progressSwitchs.progressProperty().bind(chrono.progressProperty()); });
					th.start();
					if (PV.codeModule.equals("X130379150")) { // code carte tvetav
						testSwitch = com.testSwitchs(switchsAffichageTveTav, switchsABougerTveTav, PV.com);
					} else {
						testSwitch = com.testSwitchs(switchsAffichageHubview, switchsABougerHubview, PV.com);
					}

					com.fermerConnexion();
				} catch (IOException e) {
					testSwitch = new boolean[30];
				} catch (JSchException e) {
					testSwitch = new boolean[30];
					Platform.runLater(() -> { alerteConnexion(); });
				}

				Platform.runLater(() -> { 
					progressSwitchs.progressProperty().unbind();
					progressSwitchs.setProgress(1.0);
				});
				if (testOK(testSwitch)) {
					Platform.runLater(() -> { progressSwitchs.setStyle("-fx-color: #00ae4e"); });
				} else {
					Platform.runLater(() -> { progressSwitchs.setStyle("-fx-color: #FF3715"); });
				}

				App.gestionTest.finTest();

				PV.testSwitchs = testSwitch;
				detailsSwitchs.setVisible(true);
				
				bouttonTous.setDisable(false);
				bouttonSwitchs.setMouseTransparent(false);
				bouttonSwitchs.setFocusTraversable(true);
				bouttonGauche.setDisable(false);
				bouttonDroite.setDisable(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Lance le test des ports SATA.
	 */
	@FXML
	private void testSATA(ActionEvent e) {
		bouttonSATA.setMouseTransparent(true);
		bouttonSATA.setFocusTraversable(false);
		progressSATA.setStyle("-fx-color: #2AA5FF");
		progressSATA.setVisible(true);
		progressSATA.setProgress(-1);
		Thread test = new ThreadSATA();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test des ports SATA.
	 */
	public class ThreadSATA extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean testSata1, testSata2 = true;
			try {
				if (!PV.codeModule.equals("X130379150")) {
					Chrono chrono = new Chrono(32000);
					Thread th = new Thread(chrono);
					Platform.runLater(() -> { progressSATA.progressProperty().bind(chrono.progressProperty()); });
					th.start();
				} else {
					Chrono chrono = new Chrono(16000);
					Thread th = new Thread(chrono);
					Platform.runLater(() -> { progressSATA.progressProperty().bind(chrono.progressProperty()); });
					th.start();
				}
				testSata1 = com.testSataPort1();
				if (!PV.codeModule.equals("X130379150")) {
					testSata2 = com.testSataPort2();
				}
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testSata1 = false;
				testSata2 = false;
			} catch (IOException e) {
				testSata1 = false;
				testSata2 = false;
			} catch (JSchException e) {
				testSata1 = false;
				testSata2 = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressSATA.progressProperty().unbind();
				progressSATA.setProgress(1.0);
			});
			if (testSata1 && testSata2) {
				Platform.runLater(() -> { progressSATA.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressSATA.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testSATA1 = testSata1;
			PV.testSATA2 = testSata2;
			detailsSATA.setVisible(true);

			bouttonSATA.setMouseTransparent(false);
			bouttonSATA.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test des ports Ethernet.
	 */
	@FXML
	private void testEthernet(ActionEvent e) {
		bouttonEthernet.setMouseTransparent(true);
		bouttonEthernet.setFocusTraversable(false);
		progressEthernet.setStyle("-fx-color: #2AA5FF");
		progressEthernet.setVisible(true);
		progressEthernet.setProgress(-1);
		Thread test = new ThreadEthernet();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test des ports Ethernet.
	 */
	public class ThreadEthernet extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			float[] test;
			float[] debits = new float[4];
			float[] pertes = new float[4];
			boolean[] ledsOk = new boolean[2];
			boolean testOk;
			try {
				actionEffectuee = false;
				Platform.runLater(() -> {popupActionCarte("Brancher le cable LAN sur l'accès (RJ45 J20 carte FDP).");});
				while (!actionEffectuee) {
					Thread.sleep(100);
				}
				Chrono chrono1 = new Chrono(29000);
				Thread th1 = new Thread(chrono1);
				Platform.runLater(() -> { progressEthernet.progressProperty().bind(chrono1.progressProperty()); });
				th1.start();
				test = com.testEthernet();
				testOk = test[0] >= 200 && test[1] <= 1;
				debits[0] = test[0];
				pertes[0] = test[1];
				if (!PV.codeModule.equals("X130360340")) { // >= 2 ports sauf esclave
					actionEffectuee = false;
					if (PV.codeModule.equals("X130360306")) {
						Platform.runLater(() -> {popupActionCarte("Brancher le cable LAN sur l'accès (RJ45 J19 carte FDP).");});
					} else {
						Platform.runLater(() -> {popupActionCarte("Brancher le cable LAN sur l'accès (RJ45 en face avant).");});
					}
					while (!actionEffectuee) {
						Thread.sleep(100);
					}
					if (!PV.codeModule.equals("X130360306")) {
						Platform.runLater(() -> {try {
							popupLedsRJ45();
							ledsOk[0] = Popup.testOk;
						} catch (IOException e) {
						}});
					} else {
						ledsOk[0] = true;
					}
					Chrono chrono2 = new Chrono(29000);
					Thread th2 = new Thread(chrono2);
					Platform.runLater(() -> { progressEthernet.progressProperty().bind(chrono2.progressProperty()); });
					th2.start();
					test = com.testEthernet();
					testOk = testOk && test[0] >= 200 && test[1] <= 1 && ledsOk[0];
					debits[1] = test[0];
					pertes[1] = test[1];
					if (!PV.codeModule.equals("X130379150")) { // >= 3 ports sauf TVE-TAV
						actionEffectuee = false;
						Platform.runLater(() -> {popupActionCarte("Brancher le cable LAN sur l'accès (M12 en face avant).");});
						while (!actionEffectuee) {
							Thread.sleep(100);
						}
						Platform.runLater(() -> {try {
							popupLedsM12();
							ledsOk[1] = Popup.testOk;
						} catch (IOException e) {
						}});
						
						Chrono chrono3 = new Chrono(29000);
						Thread th3 = new Thread(chrono3);
						Platform.runLater(() -> { progressEthernet.progressProperty().bind(chrono3.progressProperty()); });
						th3.start();
						test = com.testEthernet();
						testOk = testOk &&  test[0] >= 200 && test[1] <= 1 && ledsOk[1];
						debits[2] = test[0];
						pertes[2] = test[1];
						if (!PV.codeModule.equals("X130360306")) { // 4 ports pour hubview maitre
							actionEffectuee = false;
							Platform.runLater(() -> {popupActionCarte("Brancher le cable LAN sur l'accès (RJ45 J19 carte FDP).");});
							while (!actionEffectuee) {
								Thread.sleep(100);
							}
							Chrono chrono4 = new Chrono(29000);
							Thread th4 = new Thread(chrono4);
							Platform.runLater(() -> { progressEthernet.progressProperty().bind(chrono4.progressProperty()); });
							th4.start();
							test = com.testEthernet();
							testOk = testOk &&  test[0] >= 200 && test[1] <= 1;
							debits[3] = test[0];
							pertes[3] = test[1];
						}
					}
				}
				com.fermerConnexion();
			} catch (Exception e) {
				Platform.runLater(() -> {alerteConnexion();});
				testOk = false;
			}


			Platform.runLater(() -> { 
				progressEthernet.progressProperty().unbind();
				progressEthernet.setProgress(1.0); });
			if (testOk) {
				Platform.runLater(() -> { progressEthernet.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressEthernet.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testEthernetDebit = debits;
			PV.testEthernetPerte = pertes;
			PV.testEthernetLeds = ledsOk;
			detailsEthernet.setVisible(true);

			bouttonTous.setDisable(false);
			bouttonEthernet.setMouseTransparent(false);
			bouttonEthernet.setFocusTraversable(true);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test de la DATAFLASH.
	 */
	@FXML
	private void testDATAFLASH(ActionEvent e) {
		bouttonDATAFLASH.setMouseTransparent(true);
		bouttonDATAFLASH.setFocusTraversable(false);
		progressDATAFLASH.setStyle("-fx-color: #2AA5FF");
		progressDATAFLASH.setVisible(true);
		progressDATAFLASH.setProgress(-1);
		Thread test = new ThreadDATAFLASH();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test de la DATAFLASH.
	 */
	public class ThreadDATAFLASH extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean testOk;
			try {
				Chrono chrono = new Chrono(30000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressDATAFLASH.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				testOk = com.testDataflash();
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testOk = false;
			} catch (IOException e) {
				testOk = false;
			} catch (JSchException e) {
				testOk = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressDATAFLASH.progressProperty().unbind();
				progressDATAFLASH.setProgress(1.0);
			});
			if (testOk) {
				Platform.runLater(() -> { progressDATAFLASH.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressDATAFLASH.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testDATAFLASH = testOk;
			detailsDATAFLASH.setVisible(true);

			bouttonDATAFLASH.setMouseTransparent(false);
			bouttonDATAFLASH.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test du RTC.
	 */
	@FXML
	private void testRTC(ActionEvent e) {
		bouttonRTC.setMouseTransparent(true);
		bouttonRTC.setFocusTraversable(false);
		progressRTC.setStyle("-fx-color: #2AA5FF");
		progressRTC.setVisible(true);
		progressRTC.setProgress(-1);
		Thread test = new ThreadRTC();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test du RTC.
	 */
	public class ThreadRTC extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean[] testRTC = new boolean[2];
			boolean testOk;
			try {
				Chrono chrono = new Chrono(28000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressRTC.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				testRTC = com.testRTC();
				testOk = testRTC[0] && testRTC[1];
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testOk = false;
			} catch (IOException e) {
				testOk = false;
			} catch (JSchException e) {
				testOk = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressRTC.progressProperty().unbind();
				progressRTC.setProgress(1.0);
			});
			if (testOk) {
				Platform.runLater(() -> { progressRTC.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressRTC.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testRTC = testRTC;
			detailsRTC.setVisible(true);

			bouttonTous.setDisable(false);
			bouttonRTC.setMouseTransparent(false);
			bouttonRTC.setFocusTraversable(true);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test du FRAM.
	 */
	@FXML
	private void testFRAM(ActionEvent e) {
		bouttonFRAM.setMouseTransparent(true);
		bouttonFRAM.setFocusTraversable(false);
		progressFRAM.setStyle("-fx-color: #2AA5FF");
		progressFRAM.setVisible(true);
		progressFRAM.setProgress(-1);
		Thread test = new ThreadFRAM();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test du FRAM.
	 */
	public class ThreadFRAM extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			boolean testOk;
			try {
				Chrono chrono = new Chrono(6000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressFRAM.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				testOk = com.testFram();
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testOk = false;
			} catch (IOException e) {
				testOk = false;
			} catch (JSchException e) {
				testOk = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressFRAM.progressProperty().unbind();
				progressFRAM.setProgress(1.0);
			});
			if (testOk) {
				Platform.runLater(() -> { progressFRAM.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressFRAM.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testFRAM = testOk;
			detailsFRAM.setVisible(true);

			bouttonFRAM.setMouseTransparent(false);
			bouttonFRAM.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test de la température.
	 */
	@FXML
	private void testTemperature(ActionEvent e) {
		bouttonTemperature.setMouseTransparent(true);
		bouttonTemperature.setFocusTraversable(false);
		progressTemperature.setStyle("-fx-color: #2AA5FF");
		progressTemperature.setVisible(true);
		progressTemperature.setProgress(-1);
		Thread test = new ThreadTemperature();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test de la température.
	 */
	public class ThreadTemperature extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			float[] temp = new float[2];
			boolean testOk;
			try {
				Chrono chrono = new Chrono(2500);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressTemperature.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				temp = com.testTemperature();
				testOk = 20 <= temp[0] && temp[0] <= 40 && 20 <= temp[1] && temp[1] <= 55 && temp[1] >= temp[0];
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testOk = false;
			} catch (IOException e) {
				testOk = false;
			} catch (JSchException e) {
				testOk = false;
				Platform.runLater(() -> {alerteConnexion();});
			}

			if (testOk) {
				Platform.runLater(() -> { progressTemperature.setStyle("-fx-color: #00ae4e");});
			} else {
				Platform.runLater(() -> { progressTemperature.setStyle("-fx-color: #FF3715"); });
			}

			Platform.runLater(() -> { 
				progressTemperature.progressProperty().unbind();
				progressTemperature.setProgress(1.0);
			});

			App.gestionTest.finTest();

			PV.testTempDS = temp[0];
			PV.testTempCPU = temp[1];
			detailsTemperature.setVisible(true);

			bouttonTemperature.setMouseTransparent(false);
			bouttonTemperature.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test du watchdog.
	 */
	@FXML
	private void testWatchdog(ActionEvent e) {
		bouttonWatchdog.setMouseTransparent(true);
		bouttonWatchdog.setFocusTraversable(false);
		progressWatchdog.setStyle("-fx-color: #2AA5FF");
		progressWatchdog.setVisible(true);
		progressWatchdog.setProgress(-1);
		Thread test = new ThreadWatchdog();
		App.gestionTest.add(test);
	}

	/** Processus permettant de charger la barre de progression des tests. */
	public class Chrono extends Task<Void> {

		private int temps;

		public Chrono(int temps) {
			this.temps = temps;
		}

		@Override
		protected Void call() throws Exception {
			for (int i = 0; i < temps/2; i++) {
				TimeUnit.MILLISECONDS.sleep(1);
				updateProgress(i, temps/2);
			}
			return null;
		}

	}

	/**
	 * Processus exécutant le test du watchdog.
	 */
	public class ThreadWatchdog extends Thread {
		public void run() {
			Platform.runLater(() -> {
				final Alert nePasDebrancher = new Alert(Alert.AlertType.WARNING);
				nePasDebrancher.setTitle("Test du Watchdog");
				nePasDebrancher.setResizable(false);
				nePasDebrancher.setHeaderText("Ne pas débrancher le cable LAN");
				Stage stage  = (Stage) nePasDebrancher.getDialogPane().getScene().getWindow();
				stage.getIcons().add(
						new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
						);
				nePasDebrancher.setContentText("La carte doit rester connectée jusqu'à\n"
											 + "la fin du test du Watchdog.");
				nePasDebrancher.show();
			});
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();

			int temps;
			boolean testOk;
			try {
				Chrono chrono = new Chrono(160000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressWatchdog.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				temps = com.testRemoveWatchdog();
				testOk = temps < 165;
				com.fermerConnexion();
			} catch (InterruptedException e) {
				testOk = false;
				temps = 165;
			} catch (IOException e) {
				testOk = false;
				temps = 165;
			} catch (JSchException e) {
				testOk = false;
				temps = 165;
				Platform.runLater(() -> {alerteConnexion();});
			}

			Platform.runLater(() -> { 
				progressWatchdog.progressProperty().unbind();
				progressWatchdog.setProgress(1.0);
			});
			if (testOk) {
				Platform.runLater(() -> { progressWatchdog.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressWatchdog.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testWatchdog = temps;
			detailsWatchdog.setVisible(true);

			bouttonWatchdog.setMouseTransparent(false);
			bouttonWatchdog.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}

	/**
	 * Lance le test du RS.
	 */
	@FXML
	private void testRS(ActionEvent e) {
		bouttonRS.setMouseTransparent(true);
		bouttonRS.setFocusTraversable(false);
		progressRS.setStyle("-fx-color: #2AA5FF");
		progressRS.setVisible(true);
		progressRS.setProgress(-1);
		Thread test = new ThreadRS();
		App.gestionTest.add(test);
	}

	/**
	 * Processus exécutant le test du RS.
	 */
	public class ThreadRS extends Thread {
		public void run() {
			bouttonTous.setDisable(true);
			bouttonGauche.setDisable(true);
			bouttonDroite.setDisable(true);
			App.gestionTest.debutTest();


			boolean testOk;
			try {
				Chrono chrono = new Chrono(14000);
				Thread th = new Thread(chrono);
				Platform.runLater(() -> { progressRS.progressProperty().bind(chrono.progressProperty()); });
				th.start();
				testOk = com.testRsOn();
				com.fermerConnexion();
			} catch (JSchException e) {
				testOk = false;
			} catch (IOException e) {
				testOk = false;
			} catch (InterruptedException e) {
				testOk = false;
				Platform.runLater(() -> { alerteConnexion(); });
			}


			Platform.runLater(() -> { 
				progressRS.progressProperty().unbind();
				progressRS.setProgress(1.0);
			});
			if (testOk) {
				Platform.runLater(() -> { progressRS.setStyle("-fx-color: #00ae4e"); });
			} else {
				Platform.runLater(() -> { progressRS.setStyle("-fx-color: #FF3715"); });
			}

			App.gestionTest.finTest();

			PV.testRS = testOk;
			detailsRS.setVisible(true);

			bouttonRS.setMouseTransparent(false);
			bouttonRS.setFocusTraversable(true);
			bouttonTous.setDisable(false);
			bouttonGauche.setDisable(false);
			bouttonDroite.setDisable(false);
		}
	}
	
	/**
	 * Affiche les details du test des leds.
	 * @param e
	 */
	@FXML
	private void afficherDetailsLeds(ActionEvent e) {
		String[] leds = new String[] {"Leds vertes", "Leds rouges", "Leds éteintes"};
		String message = "";
		for (int i = 0; i < leds.length; i++) {
			message += leds[i];
			message += PV.testLeds[i] ? " : OK\n" : " : KO\n";
		}
		popupDetail(message);
	}

	/**
	 * Affihce les details du test des leds D1 et D4.
	 * @param e
	 */
	@FXML
	private void afficherDetailsLedsD(ActionEvent e) {
		String[] leds = new String[] {"Led D1", "Led D4"};
		String message = "";
		for (int i = 0; i < leds.length; i++) {
			message += leds[i];
			message += PV.testledsD[i] ? " : OK\n" : " : KO\n";
		}
		popupDetail(message);
	}

	/**
	 * Affiche les details du test des switchs.
	 * @param e
	 */
	@FXML
	private void afficherDetailsSwitchs(ActionEvent e) {
		String message = "";
		String[] switchs;
		if (PV.codeModule.equals("X130379150")) { // code carte tvetav
			switchs = switchsAffichageTveTav;
		} else {
			switchs = switchsAffichageHubview;
		}
		for (int i = 0; i < switchs.length; i++) {
			message += switchs[i];
			message += PV.testSwitchs[i] ? " : OK\n" : " : KO\n";
		}
		popupDetail(message);
	}

	/**
	 * Affiche les details du test des ports SATA.
	 * @param e
	 */
	@FXML
	private void afficherDetailsSATA(ActionEvent e) {
		String message = "SATA Port 1 : " + (PV.testSATA1 ? "OK" : "KO");
		if (!PV.codeModule.equals("X130379150")) {
			message += "\nSATA Port 2 : " + (PV.testSATA2 ? "OK" : "KO");
		}
		popupDetail(message);
	}

	/**
	 * Affiche le details du test des accès Ethernet.
	 * @param e
	 */
	@FXML
	private void afficherDetailsEthernet(ActionEvent e) {
		String message = "RJ45 J20 carte FDP :\n";
		message += "\t- Debit : " + PV.testEthernetDebit[0] + " Mbits/s\n";
		message += "\t- Perte : " + PV.testEthernetPerte[0] + " %\n";

		if (!PV.codeModule.equals("X130360340")) { // >= 2 ports sauf esclave
			message += PV.codeModule.equals("X130360306") ? "RJ45 J19 carte FDP :\n" : "RJ45 en face avant :\n";
			message += "\t- Debit : " + PV.testEthernetDebit[1] + " Mbits/s\n";
			message += "\t- Perte : " + PV.testEthernetPerte[1] + " %\n";
			message += PV.codeModule.equals("X130360306") ? "" : ("\t- Led : " + (PV.testEthernetLeds[0] ? "OK" : "KO") + "\n");
			if (!PV.codeModule.equals("X130379150")) { // >= 3 ports sauf TVE-TAV
				message += "M12 en face avant :\n";
				message += "\t- Debit : " + PV.testEthernetDebit[2] + " Mbits/s\n";
				message += "\t- Perte : " + PV.testEthernetPerte[2] + " %\n";
				message += "\t- Led : " + (PV.testEthernetLeds[PV.testEthernetLeds.length-1] ? "OK" : "KO") + "\n";
				if (!PV.codeModule.equals("X130360306")) { // 4 ports pour hubview maitre
					message += "RJ45 J19 carte FDP :\n";
					message += "\t- Debit : " + PV.testEthernetDebit[3] + " Mbits/s\n";
					message += "\t- Perte : " + PV.testEthernetPerte[3] + " %\n";
				}
			}
		}
		popupDetail(message);
	}

	/**
	 * Affiche les details du test de la DATAFLASH.
	 * @param e
	 */
	@FXML
	private void afficherDetailsDATAFLASH(ActionEvent e) {
		String message = "DATAFLASH : " + (PV.testDATAFLASH ? "OK" : "KO");
		popupDetail(message);
	}

	/**
	 * Affiche les details du test du RTC.
	 * @param e
	 */
	@FXML
	private void afficherDetailsRTC(ActionEvent e) {
		String message = "Zone RAM : " + (PV.testRTC[0] ? "OK" : "KO");
		message += "\nEcriture/Lecture heure " + (PV.testRTC[1] ? "OK" : "KO");
		popupDetail(message);
	}

	/**
	 * Affiche les details du test du FRAM.
	 * @param e
	 */
	@FXML
	private void afficherDetailsFRAM(ActionEvent e) {
		String message = "FRAM : " + (PV.testFRAM ? "OK" : "KO");
		popupDetail(message);
	}

	/**
	 * Affiche les details du test de la température.
	 * @param e
	 */
	@FXML
	private void afficherDetailsTemperature(ActionEvent e) {
		String message = "Température DS1621 : " + PV.testTempDS + " °C";
		message += "\nTempérature CPU : " + PV.testTempCPU + " °C";
		popupDetail(message);
	}

	/**
	 * Affiche les details du test du watchdog.
	 * @param e
	 */
	@FXML
	private void afficherDetailsWatchdog(ActionEvent e) {
		String message = "Watchdog : " + (PV.testWatchdog < 165 ? PV.testWatchdog + " s" : "KO");
		popupDetail(message);
	}

	/**
	 * Affiche les details du test de la liaison RS.
	 * @param e
	 */
	@FXML
	private void afficherDetailsRS(ActionEvent e) {
		String message = "Liaison RS485 : " + (PV.testRS ? "OK" : "KO");
		popupDetail(message);
	}

	/**
	 * Lance tous les tests.
	 * @param e
	 */
	@FXML
	private void testTous(ActionEvent e) {
		progressLeds.setVisible(true);
		bouttonLeds.setMouseTransparent(true);
		bouttonLeds.setFocusTraversable(false);
		progressLeds.setProgress(-1);
		progressLeds.setStyle("-fx-color: #2AA5FF");
		progressLedsD.setVisible(true);
		bouttonLedsD.setMouseTransparent(true);
		bouttonLedsD.setFocusTraversable(false);
		progressLedsD.setProgress(-1);
		progressLedsD.setStyle("-fx-color: #2AA5FF");
		progressSwitchs.setVisible(true);
		bouttonSwitchs.setMouseTransparent(true);
		bouttonSwitchs.setFocusTraversable(false);
		progressSwitchs.setProgress(-1);
		progressSwitchs.setStyle("-fx-color: #2AA5FF");
		progressSATA.setVisible(true);
		bouttonSATA.setMouseTransparent(true);
		bouttonSATA.setFocusTraversable(false);
		progressSATA.setProgress(-1);
		progressSATA.setStyle("-fx-color: #2AA5FF");
		progressEthernet.setVisible(true);
		bouttonEthernet.setMouseTransparent(true);
		bouttonEthernet.setFocusTraversable(false);
		progressEthernet.setProgress(-1);
		progressEthernet.setStyle("-fx-color: #2AA5FF");
		progressDATAFLASH.setVisible(true);
		bouttonDATAFLASH.setMouseTransparent(true);
		bouttonDATAFLASH.setFocusTraversable(false);
		progressDATAFLASH.setProgress(-1);
		progressDATAFLASH.setStyle("-fx-color: #2AA5FF");
		progressRTC.setVisible(true);
		bouttonRTC.setMouseTransparent(true);
		bouttonRTC.setFocusTraversable(false);
		progressRTC.setProgress(-1);
		progressRTC.setStyle("-fx-color: #2AA5FF");
		progressFRAM.setVisible(true);
		bouttonFRAM.setMouseTransparent(true);
		bouttonFRAM.setFocusTraversable(false);
		progressFRAM.setProgress(-1);
		progressFRAM.setStyle("-fx-color: #2AA5FF");
		progressTemperature.setVisible(true);
		bouttonTemperature.setMouseTransparent(true);
		bouttonTemperature.setFocusTraversable(false);
		progressTemperature.setProgress(-1);
		progressTemperature.setStyle("-fx-color: #2AA5FF");
		progressWatchdog.setVisible(true);
		bouttonWatchdog.setMouseTransparent(true);
		bouttonWatchdog.setFocusTraversable(false);
		progressWatchdog.setProgress(-1);
		progressWatchdog.setStyle("-fx-color: #2AA5FF");
		App.gestionTest.add(new ThreadLeds());
		App.gestionTest.add(new ThreadLedsD());
		App.gestionTest.add(new ThreadSwitchs());
		App.gestionTest.add(new ThreadSATA());
		App.gestionTest.add(new ThreadEthernet());
		App.gestionTest.add(new ThreadDATAFLASH());
		App.gestionTest.add(new ThreadRTC());
		App.gestionTest.add(new ThreadFRAM());
		App.gestionTest.add(new ThreadTemperature());
		if (!PV.codeModule.equals("X130379150")) {
			progressRS.setVisible(true);
			bouttonRS.setMouseTransparent(true);
			bouttonRS.setFocusTraversable(false);
			progressRS.setProgress(-1);
			progressRS.setStyle("-fx-color: #2AA5FF");
			App.gestionTest.add(new ThreadRS());
		}
		App.gestionTest.add(new ThreadWatchdog());
	}
}

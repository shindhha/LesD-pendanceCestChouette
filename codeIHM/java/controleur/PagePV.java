package controleur;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modele.CreationPV;
import modele.PV;

/**
 * Controleur de la page pagePV.
 * Permet de gérer la génération du PV de test.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class PagePV implements Initializable {

	/** Barre de progression de la génération du PV. */
	@FXML
	ProgressBar progress;

	/** Bouutton permettant d'enregistrer le PV. */
	@FXML
	Button bouttonEnregistrer;

	/** Boutton permettant d'accèder à la page précédente. */
	@FXML
	Button bouttonGauche;
	
	/** Boutton permettant de débuter le test d'une nouvelle carte. */
	@FXML
	Button btnNouvelleCarte;
	
	/** Label indiquant que l'enregistrement a été réussi. */
	@FXML
	Label labelEnregistrement;

	/** Label indiquant l'état de la génération (en cours / terminée). */
	@FXML
	Label labelCreation;

	/** Générateur de PV. */
	private CreationPV pv;

	/** Control permettant de choisir l'emplacement du fichier à sauvegarder. */
	FileChooser fileChooser;

	/** Génération du PV */
	public class GenerationPV extends Task<Void> {

		@Override
		protected Void call() throws Exception {
			String template;
			int nbSwitchs, nbSata, nbEthernet;
			boolean rs = true;
			if (PV.codeModule.equals("X130379150")) {
				template = "./pv/tvetav.docx";
				nbSwitchs = 14;
				nbSata = 1;
				nbEthernet = 2;
				rs = false;
			} else if (PV.codeModule.equals("X130360340")) {
				template = "./pv/esclave.docx";
				nbSwitchs = 18;
				nbSata = 2;
				nbEthernet = 1;
			} else if (PV.codeModule.equals("X130360305")) {
				template = "./pv/maitre.docx";
				nbSwitchs = 18;
				nbSata = 2;
				nbEthernet = 4;
			} else {
				template = "./pv/maitrem12.docx";
				nbSwitchs = 18;
				nbSata = 2;
				nbEthernet = 3;
			}

			int i = 0;
			final int PROGRESS_MAX = 35;

			updateProgress(++i, PROGRESS_MAX); 
			pv = new CreationPV(template);

			/* date */
			pv.remplacer("[date]", PV.date);

			/* operateur */
			pv.remplacer("[operateur]", PV.operateur);

			/* numero */
			pv.remplacer("[numero]", PV.numModule);

			/* version du bios */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[bios_version]", PV.versionBIOS);

			/* leds vertes */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[leds_vertes]", PV.testLeds[0] ? "OK" : "KO");

			/* leds rouges */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[leds_rouges]", PV.testLeds[1] ? "OK" : "KO");

			/* leds off */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[leds_off]", PV.testLeds[2] ? "OK" : "KO");

			/* led D1 */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[led_D1]", PV.testledsD[0] ? "OK" : "KO");

			/* led D4 */
			updateProgress(++i, PROGRESS_MAX);  
			pv.remplacer("[led_D4]", PV.testledsD[1] ? "OK" : "KO");

			/* Switchs */
			for (int j = 0; j < nbSwitchs; j++) {
				pv.remplacer("[switch" + (j+1) + "]", PV.testSwitchs[j] ? "OK" : "KO");
			}
			i += 10;
			updateProgress(i, PROGRESS_MAX); 

			/* rs */
			updateProgress(++i, PROGRESS_MAX);
			if (rs) {
				pv.remplacer("[rs]", PV.testRS ? "OK" : "KO");
			}

			/* Sata */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[sata1]", PV.testSATA1 ? "OK" : "KO");
			if (nbSata == 2) {
				pv.remplacer("[sata2]", PV.testSATA2 ? "OK" : "KO");
			}

			/* Ethernet */
			updateProgress(++i, PROGRESS_MAX);  
			pv.remplacer("[debit1]", String.format("%.2f", PV.testEthernetDebit[0]) + " Mbits/s");
			pv.remplacer("[perte1]", String.format("%.2f", PV.testEthernetPerte[0]) + " %");
			if (nbEthernet >= 2) {
				pv.remplacer("[debit2]", String.format("%.2f", PV.testEthernetDebit[1]) + " Mbits/s");
				pv.remplacer("[perte2]", String.format("%.2f", PV.testEthernetPerte[1]) + " %");
				pv.remplacer("[led1]", PV.testEthernetLeds[0] ? "OK" : "KO");
			}
			if (nbEthernet >= 3) {
				pv.remplacer("[debit3]", String.format("%.2f", PV.testEthernetDebit[2]) + " Mbits/s");
				pv.remplacer("[perte3]", String.format("%.2f", PV.testEthernetPerte[2]) + " %");
			}
			if (nbEthernet >= 4) {
				pv.remplacer("[debit4]", String.format("%.2f", PV.testEthernetDebit[3]) + " Mbits/s");
				pv.remplacer("[perte4]", String.format("%.2f", PV.testEthernetPerte[3]) + " %");
				pv.remplacer("[led2]", PV.testEthernetLeds[1] ? "OK" : "KO");
			}

			/* dataflash */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[dataflash]", PV.testDATAFLASH ? "OK" : "KO");

			/* rtc */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[rtc1]", PV.testRTC[0] ? "OK" : "KO");
			pv.remplacer("[rtc2]", PV.testRTC[1] ? "OK" : "KO");

			/* fram */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[fram]", PV.testFRAM ? "OK" :"KO");

			/* température */
			updateProgress(++i, PROGRESS_MAX); 
			pv.remplacer("[ds]", String.format("%.2f", PV.testTempDS) + " °C");
			pv.remplacer("[cpu]", String.format("%.2f", PV.testTempCPU) + " °C");

			/* watchdog */
			updateProgress(PROGRESS_MAX, PROGRESS_MAX); 
			pv.remplacer("[watchdog]", PV.testWatchdog < 165 ? PV.testWatchdog + " s" : "KO");

			Platform.runLater(() -> {
				bouttonEnregistrer.setDisable(false);
				labelCreation.setText("Generation terminée.");
			});

			return null;
		}

	}

	/**
	 * Initialisation de la page.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word (*.docx)", "*.docx"));

		bouttonEnregistrer.setDisable(true);
		labelCreation.setText("Pv en cours de création ...");
		GenerationPV gen = new GenerationPV();
		Thread th = new Thread(gen);
		progress.progressProperty().bind(gen.progressProperty());
		th.start();
	}

	/**
	 * Enregistre le PV.
	 * @param e
	 */
	@FXML
	private void enregistrer(ActionEvent e) {
		Stage stage = (Stage) bouttonEnregistrer.getScene().getWindow();
		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			if (!file.getName().contains(".docx")) {
				pv.sauvegarder(file.getAbsolutePath() + ".docx");
			} else {
				pv.sauvegarder(file.getAbsolutePath());
			}
			labelEnregistrement.setVisible(true);
		}
		btnNouvelleCarte.setVisible(true);
	}

	/**
	 * Affiche la page précédente.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void stagePrecedent(ActionEvent e) throws IOException {
		Stage stage = (Stage) bouttonGauche.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/pageTest.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Tests Unitaires");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Démarre la procédure de test d'une nouvelle carte.
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void nouvelleCarte(ActionEvent e) throws IOException {
		Stage stage = (Stage) btnNouvelleCarte.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/choixCarte.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Choix de la carte");
		stage.setScene(scene);
		stage.show();
	}
}

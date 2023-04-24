package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modele.PV;

/**
 * Controleur de la page de configuration du BIOS.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class ConfigBIOS implements Initializable {

	/** Nombre d'objet à afficher sur la page. */
	private final int NB_CONTROLS = 10;
	
	/** Le textfield où l'utilisateur rentre la version du BIOS. */
	@FXML
	TextField tfVersion;
	
	/** Label demandant à l'utilisateur de rentrer la version du BIOS. */
	@FXML
	Label lbVersion;
	
	/** Label demandant de configurer la variable User I2C Support. */
	@FXML
	Label lbUser1;
	
	/** Label indiquant la valeur à mettre à la variable User I2C Support. */
	@FXML
	Label lbUser2;
	
	/** Label demandant de configurer la variable SD-Card/GPIO selection. */
	@FXML
	Label lbCard1;
	
	/** Label indiquant la valeur à mettre à la variable SD-Card/GPIO selection. */
	@FXML
	Label lbCard2;
	
	/** Label demandant de configurer la variable HD-Audio Support. */
	@FXML
	Label lbAudio1;
	
	/** Label indiquant la valeur à mettre à la variable HD-Audio Support. */
	@FXML
	Label lbAudio2;
	
	/** Label demandant de configurer la variable QuietBoot. */
	@FXML
	Label lbBoot1;
	
	/** Label indiquant la valeur à mettre à la variable QuietBoot. */
	@FXML
	Label lbBoot2;
	
	/** Boutton permettant d'accèder à la page précédente. */
	@FXML
	Button bouttonGauche;
	
	/** Boutton permettant d'accèder à la page suivante. */
	@FXML
	Button bouttonDroite;
	
	/** Racine de la page. */
	@FXML
	Pane pane;
	
	/** Les objets à afficher actuellement. */
	private int controlActuel = 0;
	
	/** Tableau contenant les objets de la page. */
	private Control[] controls = new Control[NB_CONTROLS];
	
	/**
	 * Initialise les informations de la page.
	 * Cette méthode est exécuté à chaque fois que la page
	 * est chargée par l'application.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfVersion.setVisible(true);
		lbVersion.setVisible(true);
		lbUser1.setVisible(false);
		lbUser2.setVisible(false);
		lbCard1.setVisible(false);
		lbCard2.setVisible(false);
		lbAudio1.setVisible(false);
		lbAudio2.setVisible(false);
		lbBoot1.setVisible(false);
		lbBoot2.setVisible(false);
		
		controls[0] = tfVersion;
		controls[1] = lbVersion;
		controls[2] = lbUser1;
		controls[3] = lbUser2;
		controls[4] = lbCard1;
		controls[5] = lbCard2;
		controls[6] = lbAudio1;
		controls[7] = lbAudio2;
		controls[8] = lbBoot1;
		controls[9] = lbBoot2;
		
		/* On initialise avec les valeurs que l'on a déjà */
		if (PV.versionBIOS != null && !PV.versionBIOS.isEmpty()) {
			tfVersion.setText(PV.versionBIOS);
		}
	}
	
	/**
	 * Affiche les objets suivant présents sur la page.
	 */
	private void afficherControlSuivant() {
		/* On cache les controls actuel */
		controls[controlActuel].setVisible(false);
		controls[controlActuel+1].setVisible(false);
		/* On met à jour le PV */
		if (controls[controlActuel] == lbUser1) {
			PV.userBIOS = true;
		} else if (controls[controlActuel] == lbCard1) {
			PV.cardBIOS = true;
		} else if (controls[controlActuel] == lbAudio1) {
			PV.audioBIOS = true;
		} else if (controls[controlActuel] == lbBoot1) {
			PV.bootBIOS = true;
		}
		/* On affiche les controls suivant */
		if (controlActuel + 2 >= NB_CONTROLS) {
			controlActuel = 0;
		} else {
			controlActuel += 2;
		}
		controls[controlActuel].setVisible(true);
		controls[controlActuel+1].setVisible(true);
	}
	
	/**
	 * Lorsque la touche ENTREE est appuyée exécute
	 * la méthode afficherControlSuivant().
	 */
	@FXML
	private void continuer(KeyEvent e) {
		if (e.getCode().equals(KeyCode.ENTER)) {
			afficherControlSuivant();
			pane.requestFocus();
		}
	}
	
	/**
	 * Affiche la page précédente (infoPV).
	 * @throws IOException
	 */
	@FXML
	private void stagePrecedent() throws IOException {
		/* On met à jour le PV avec les infos de la page */
		PV.versionBIOS = tfVersion.getText();
		
		/* On affiche la page précédente */
		Stage stage = (Stage) bouttonGauche.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/infoPV.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Information pour le PV");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Affiche la page suivante (connexionCarte).
	 * @throws IOException
	 */
	@FXML
	private void stageSuivant() throws IOException {
		/* On met à jour le PV avec les infos de la page */
		PV.versionBIOS = tfVersion.getText();
		PV.userBIOS = true;
		PV.cardBIOS = true;
		PV.audioBIOS = true;
		PV.bootBIOS = true;
		/* On affiche la page précédente */
		Stage stage = (Stage) bouttonDroite.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/connexionCarte.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Connexion à la carte");
		stage.setScene(scene);
		stage.show();
	}
}

package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.JSchException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modele.CommunicationSSH;
import modele.PV;

/**
 * Controleur de la pge de connexion à la carte.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class ConnexionCarte implements Initializable {

	/** Textfield où il faut entrer l'identfiant de connexion. */
	@FXML
	private TextField identifiant;
	
	/** Textfield où il faut entrer le numéro de COM. */
	@FXML
	private TextField com;

	/** Textfield où il faut entrer le mot de passe. */
	@FXML
	private PasswordField mdp;

	/** Message qui est affiché en cas d'erreur de connexion. */
	@FXML
	private Label labelErreur;

	/** Boutton permettant d'accèder à la page précédente. */
	@FXML
	private Button bouttonGauche;
	
	/** Boutton permettant d'accèder à la page suivante. */
	@FXML
	private Button btnConnexion;
	
	/** Un indicateur qui est affiché lors du chargement de la connexion à la carte. */
	@FXML
	private ProgressIndicator progress;

	/**
	 * Passe à la vue suivante (pageTest).
	 * @param stage
	 * @throws IOException
	 */
	private void stageSuivant(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/pageTest.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Tests unitaires");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Affiche la page précédente (configBIOS).
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void stagePrecedent(ActionEvent e) throws IOException {
		/* On met à jour le PV avec les infos de la page */
		PV.login = identifiant.getText();
		PV.mdp = mdp.getText();
		/* On affiche la page précédente */
		Stage stage = (Stage) btnConnexion.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/configBIOS.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Configuration du BIOS");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Essaye de se connecter à la carte avec les informations saisies.
	 * Affiche les indicateur correspondant au résultat de la connexion.
	 */
	@FXML
	private void connexion(ActionEvent e) {
		Thread th = new Thread(() -> {
			PV.login = identifiant.getText();
			PV.mdp = mdp.getText();
			String comText = com.getText().trim();
			Pattern pattern = Pattern.compile("\\d");
			Matcher m = pattern.matcher(comText);
			if (m.find()) {
				PV.com = Integer.parseInt(comText);
			}
			PageTest.com = new CommunicationSSH(PV.login, PV.ip, PV.port, PV.mdp);
			try {
				labelErreur.setVisible(false);
				progress.setVisible(true);
				btnConnexion.setDisable(true);
				bouttonGauche.setDisable(true);
				PageTest.com.etablirConnexion();
				PageTest.com.fermerConnexion();
				bouttonGauche.setDisable(false);
				progress.setVisible(false);
				btnConnexion.setDisable(false);
				Stage stage = (Stage) mdp.getScene().getWindow();
				Platform.runLater(() -> { try {
					stageSuivant(stage);
				} catch (IOException e1) {
				} });
			} catch (JSchException | IOException | InterruptedException e1) {
				labelErreur.setVisible(true);
				progress.setVisible(false);
				btnConnexion.setDisable(false);
				bouttonGauche.setDisable(false);
			}
		});
		th.start();
	}

	/** Si la touche ENTREE est pressée, clique sur le boutton connexion. */
	@FXML
	private void declancherBoutton(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			btnConnexion.fire();
		}
	}

	/**
	 * Initialise la page avec les infos du PV.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (PV.login != null) {
			identifiant.setText(PV.login);
		}
		if (PV.mdp != null) {
			mdp.setText(PV.mdp);
		}
		if (PV.com != 0) {
			com.setText("" + PV.com);
		}
		
		final Alert verifCable = new Alert(Alert.AlertType.INFORMATION);
		verifCable.setTitle("Vérification des cables");
		verifCable.setResizable(false);
		verifCable.setHeaderText("Vérification du branchement des cables");
		Stage stage  = (Stage) verifCable.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
				);
		verifCable.setContentText("- Cable LAN branché sur le port RJ45 J20 carte FDP.\n"
								+ "- Cable USB branché sur le port J18.");
		verifCable.show();
	}
}

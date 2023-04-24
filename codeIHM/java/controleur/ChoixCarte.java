package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import modele.PV;
import modele.TFTPD;

/**
 * Controleur de la page qui demande à l'utilisateur la carte
 * qu'il souhaite tester.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class ChoixCarte implements Initializable {

	@FXML
	ComboBox<String> listTAV;
	
	@FXML
	ComboBox<String> listHubview;
	
	/**
	 * Passe à la vue suivante (infoPV).
	 * @param stage
	 * @throws IOException
	 */
	private void stageSuivant(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/infoPV.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Information pour le PV");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Cette méthode est lancée dès que la page est chargée par l'application.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* redémarre tftpd32 pour la nouvelle carte */
		TFTPD.stop();
		TFTPD.reinitialiserInit();
		TFTPD.run();
		/* remise à zero des infos du PV */
		PV.initPvVide();
		/* Initialisation des choice box */
		ObservableList<String> optionsTav = 
		        FXCollections.observableArrayList(
		            "SE-TVE-TAV-CPU Bloc Enfichable CPU (X130379150)"
		        );
		listTAV.setItems(optionsTav);
		
		ObservableList<String> optionsHubview = 
		        FXCollections.observableArrayList(
		            "HUBVIEW-CPU-Maitre-Module (X130360305)",
		            "HUBVIEW-CPU-Maitre-Module-M12 (X130360306)",
		            "HUBVIEW-CPU-ESCLAVE-Module (X130360340)"
		        );
		listHubview.setItems(optionsHubview);
	}
	
	/**
	 * Récupère le numéro de serie du module choisie.
	 * @param choix
	 */
	private String recupSN(String choix) {
		return choix.split("\\(|\\)")[1];
	}
	
	@FXML
	private void choixTAV(ActionEvent e) throws IOException {
		PV.codeModule = recupSN(listTAV.getValue());
		Stage stage = (Stage) listTAV.getScene().getWindow();
		stageSuivant(stage);
	}
	
	@FXML
	private void choixHubview(ActionEvent e) throws IOException {
		PV.codeModule = recupSN(listHubview.getValue());
		Stage stage = (Stage) listHubview.getScene().getWindow();
		stageSuivant(stage);
	}
}

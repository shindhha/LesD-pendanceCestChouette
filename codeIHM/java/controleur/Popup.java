package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Cette classe permet de lancer des popups et de vérifier
 * les réponses de l'utilisateur.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class Popup {
	
	/** Vrai si l'utilisateur à répondu OUI. */
	public static boolean testOk;
	
	/** Vrai lorsque l'utilisateur a donné une réponse. */
	public static boolean reponseRecu;
	
	/** Racine de la popup. */
	@FXML
	AnchorPane pane;
	
	/** Réaction à la réponse OUI. */
	@FXML
	public void reponseOui(ActionEvent e) {
		testOk = true;
		Stage stage = (Stage) pane.getScene().getWindow();
		reponseRecu = true;
		stage.close();
	}
	
	/** Réaction à la réponse NON. */
	@FXML
	public void reponseNon(ActionEvent e) {
		testOk = false;
		Stage stage = (Stage) pane.getScene().getWindow();
		reponseRecu = true;
		stage.close();
	}
}

package vue;

import java.io.IOException;

import controleur.GestionTest;
import controleur.PageTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modele.TFTPD;

/**
 * Classe de chargement de la vue de l'application.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class App extends Application {

	/**
	 * Gestionnaire des processus exécutants les tests de la carte.
	 */
	public static GestionTest gestionTest;
	
	/**
	 * Chargement de la vue.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		gestionTest = new GestionTest();
		gestionTest.start();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/choixCarte.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream("images/icone.png"))
		);
		stage.setTitle("Choix de la carte");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
	
	/**
	 * Fonction exécutée lors de la fermeture de l'application.
	 */
	@SuppressWarnings("removal")
	@Override
	public void stop(){
		/* Stoppe le déroulement du processus gestionnaire des tests */
		gestionTest.stop();
		/* Stoppe l'application permettant d'attribuer une ip dynamiquement à la carte */
		TFTPD.stop();
		
		try {
			if (PageTest.com != null) {
				/* fermeture de la communication SSH */
				PageTest.com.fermerConnexion();
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

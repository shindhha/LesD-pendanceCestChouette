package modele;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Cette classe permet de générer des PV de test à partir
 * des informations stockées dans la classe PV.
 * @author Loris Gaven
 * @version 0.0.0.1
 */
public class CreationPV {

	/** Chemin vers le modele du PV. */
	private String template;
	
	/** Image du document Word template. */
	private XWPFDocument doc;
	
	/**
	 * Initialise les informations nécessaires à la création du PV.
	 * @param template
	 */
	public CreationPV(String template) {
		this.template = template;
		try {
			doc = new XWPFDocument(OPCPackage.open(this.template));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de remplacer une valeur contenu dans le PV par une autre.
	 * @param aRemplacer
	 * @param valeur
	 */
	public void remplacer(String aRemplacer, String valeur) {
		for (XWPFParagraph p : doc.getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					String text = r.getText(0);
					if (text != null && text.contains(aRemplacer)) {
						text = text.replace(aRemplacer, valeur);
						r.setText(text, 0);
					}
				}
			}
		}

		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							//System.out.println(text);
							if (text != null && text.contains(aRemplacer)) {
								text = text.replace(aRemplacer, valeur);
								r.setText(text, 0);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Sauvegarde le PV de test à l'endroit demandé.
	 * @param chemin Le chemin où l'on veut sauvegarder le PV.
	 */
	public void sauvegarder(String chemin) {
		try {
			doc.write(new FileOutputStream(chemin));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

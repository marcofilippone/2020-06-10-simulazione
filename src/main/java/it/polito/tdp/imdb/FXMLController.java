/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean creato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	if(!creato) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	Actor a = boxAttore.getValue();
    	if(a==null) {
    		txtResult.setText("Selezionare un attore dalla tendina");
    		return;
    	}
    	List<Actor> lista = model.trovaCollegati(a);
    	txtResult.setText("ATTORI SIMILI A "+a+":\n\n");
    	for(Actor att : lista) {
    		txtResult.appendText(att+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String s = boxGenere.getValue();
    	if(s==null || s.equals("")) {
    		txtResult.setText("Selezionare un genere dalla tendina");
    		return;
    	}
    	if(creato) {
    		boxAttore.getItems().removeAll(model.getVertici());
    	}
    	model.creaGrafo(s);
    	creato = true;
    	txtResult.setText("Grafo creato:\n# vertici: "+model.getVertici().size()+"\n# archi: "+model.getArchi().size());
    	boxAttore.getItems().addAll(model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	if(!creato) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	String txt = txtGiorni.getText();
    	Integer n;
    	try {
    		n = Integer.parseInt(txt);
    	} catch(NumberFormatException e) {
    		txtResult.setText("Devi inserire un intero n di giorni");
    		return;
    	}
    	if(n<1) {
    		txtResult.setText("Devi inserire un intero n maggiore o uguale a 1");
    		return;
    	}
    	model.simula(n);
    	txtResult.setText("SIMULAZIONE EFFETTUATA PER GIORNI "+n+"\nGiorni di pausa: "+model.getPause()+"\nAttori Intervistati:\n\n");
    	for(Actor att : model.getIntervistati()) {
    		txtResult.appendText(att+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxGenere.getItems().addAll(model.generi());
    }
}

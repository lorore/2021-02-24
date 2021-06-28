
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.GiocatoreMigliore;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnGiocatoreMigliore"
    private Button btnGiocatoreMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMatch"
    private ComboBox<Match> cmbMatch; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	Match m=this.cmbMatch.getValue();
    	if(m==null) {
    		this.txtResult.setText("Inserire un match!");
    		return;
    	}
    	String res=this.model.creaGrafo(m);
    	this.txtResult.appendText(res);
    	this.btnGiocatoreMigliore.setDisable(false);
    	this.btnSimula.setDisable(false);
    }

    @FXML
    void doGiocatoreMigliore(ActionEvent event) { 
    	this.txtResult.clear();
    	GiocatoreMigliore res=this.model.getGiocatoreMigliore();
    	if(res==null) {
    		this.txtResult.setText("Non c'è nessun giocatore migliore");
    		return;
    	}
    	else {
    		this.txtResult.setText(res.toString());
    		return;
    	}
    	
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	String n=this.txtN.getText().trim();
    	if(n.isBlank()) {
    		this.txtResult.setText("Nessun n inserito");
    		return;
    	}
    	
    	Integer N;
    	try {
    		N=Integer.parseInt(n);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Quello inserito non è un numero!");
    		return;
    	}
    	
    	this.model.avviaSimulazione(N);
    	int golCasa=this.model.getGolCasa();
    	int golOspiti=this.model.getGolOspite();
    	this.txtResult.appendText("Risultato: "+golCasa+"-"+golOspiti+"\n");
    	this.txtResult.appendText("Espulsi: "+this.model.getEspulsiCasa()+"-"+this.model.getEspulsiO());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGiocatoreMigliore != null : "fx:id=\"btnGiocatoreMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMatch != null : "fx:id=\"cmbMatch\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnSimula.setDisable(true);
    	List<Match> matches=this.model.getMatches();
    	Collections.sort(matches);
    	this.cmbMatch.getItems().addAll(matches);
    	this.btnGiocatoreMigliore.setDisable(true);
    }
}

package com.example.zd_lab1_javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class NewPassPhrase {

    private Stage stagePassPhrase;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField confirmPassph;

    @FXML
    private PasswordField Passph;

    @FXML
    private Button DoneButton;

    @FXML
    private Label errorLable;

    @FXML
    void OnActionDoneButton(ActionEvent event) throws IOException {
        String passphrase = Passph.getText();
        if(passphrase.equals(confirmPassph.getText())){
            MainApplication.encoder = new FileEncrypt(passphrase);
            MainApplication.encoder.Encrypt(MainController.FileName, MainController.FileNameEnc);

            Stage stage = (Stage) DoneButton.getScene().getWindow();
            stage.close();

            stagePassPhrase = new Stage();
            MainApplication.startPage(stagePassPhrase, "PassPhrase.fxml", "Password Phrase Control", 600, 194);
        }
        else{
            errorLable.setText("Passphrases are not equal!\nPlease, try again");
        }
    }

    @FXML
    void initialize() {
        assert confirmPassph != null : "fx:id=\"confirmPassph\" was not injected: check your FXML file 'NewPassPhrase.fxml'.";
        assert Passph != null : "fx:id=\"Passph\" was not injected: check your FXML file 'NewPassPhrase.fxml'.";
        assert DoneButton != null : "fx:id=\"DoneButton\" was not injected: check your FXML file 'NewPassPhrase.fxml'.";
        assert errorLable != null : "fx:id=\"errorLable\" was not injected: check your FXML file 'NewPassPhrase.fxml'.";

    }
}


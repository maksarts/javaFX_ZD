package com.example.zd_lab1_javafx;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class PassPhrase {

    private Stage stageMain;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passphrase;

    @FXML
    private Button DoneButton;

    @FXML
    void OnActionDoneButton(ActionEvent event) throws FileNotFoundException {
        MainApplication.encoder = new FileEncrypt(passphrase.getText());
        MainApplication.encoder.Decrypt(MainController.FileNameEnc);

        try(FileReader data = new FileReader(MainController.FileName))
        {
            BufferedReader reader = new BufferedReader(data); // для построчного считывания
            String line = reader.readLine();
            if (line != null) {
                String[] buffer = line.split(String.valueOf(MainController.border));
                if(buffer[0].equals("admin")){
                    Stage stage = (Stage) DoneButton.getScene().getWindow();
                    stage.close();

                    stageMain = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    stageMain.setTitle("Арцишевский А-13-18 ЗД_Лаб_4");
                    stageMain.setScene(scene);
                    stageMain.show();
                }
                else{
                    data.close();
                    File file = new File(MainController.FileName);
                    System.out.println("Error delete: ".concat(Boolean.toString(file.delete())));

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error passphrase");
                    alert.setHeaderText(null);
                    alert.setContentText("Error passphrase!\nProgram will be closed");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    alert.showAndWait();

                    Stage stage = (Stage) DoneButton.getScene().getWindow();
                    stage.close();
                }
            }
            else{
                data.close();
                File file = new File(MainController.FileName);
                System.out.println("Error delete: ".concat(Boolean.toString(file.delete())));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error passphrase");
                alert.setHeaderText(null);
                alert.setContentText("Error passphrase!\nProgram will be closed");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                java.awt.Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();

                Stage stage = (Stage) DoneButton.getScene().getWindow();
                stage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert passphrase != null : "fx:id=\"passphrase\" was not injected: check your FXML file 'PassPhrase.fxml'.";
        assert DoneButton != null : "fx:id=\"DoneButton\" was not injected: check your FXML file 'PassPhrase.fxml'.";

    }
}

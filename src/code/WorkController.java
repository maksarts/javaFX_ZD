package com.example.zd_lab1_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkController {

    private Stage stageChangePass;
    private Stage stageListOfAccounts;
    private Stage stageAbout;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private static Menu AdminMenu;
    private java.awt.Toolkit Toolkit;

    public static void DisableAdminMenu(){
        AdminMenu = new Menu();
        AdminMenu.setDisable(Boolean.TRUE);
    }

    @FXML
    void OnActionAdminMenu(ActionEvent event) {
        if(!MainApplication.onlineUser.is_admin){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ACCESS DENIED\nThis feature only for ADMIN mode");

            java.awt.Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        }
    }

    @FXML
    private Button Close_button;

    @FXML
    private MenuItem Change_pass;

    @FXML
    private MenuItem Close_menu;

    @FXML
    private MenuItem List_of_accs;

    @FXML
    private MenuItem About;

    @FXML
    private AnchorPane main_form;

    @FXML
    void OnActionClose_button(ActionEvent event) throws IOException {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Арцишевский А-13-18 ЗД_Лаб_4");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void OnActionChange_pass(ActionEvent event) throws IOException {
        stageChangePass = new Stage();
        MainApplication.startPage(stageChangePass, "ChangePass.fxml", "Change password", 400, 304);
    }

    @FXML
    void OnActionClose_menu(ActionEvent event) throws IOException {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Арцишевский А-13-18 ЗД_Лаб_3");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void OnActionAbout(ActionEvent event) throws IOException {
        stageAbout = new Stage();
        MainApplication.startPage(stageAbout, "About.fxml", "About", 392, 441);
    }

    @FXML
    void OnActionList_of_accs(ActionEvent event) throws IOException {
        if(MainApplication.onlineUser.is_admin) {
            stageListOfAccounts = new Stage();
            MainApplication.startListPage(stageListOfAccounts, "ListOfAccounts.fxml", "Accounts settings", 600, 400);
        }
    }

    @FXML
    void initialize() throws IOException {
        assert Close_button != null : "fx:id=\"Close_button\" was not injected: check your FXML file 'Work.fxml'.";
        assert AdminMenu != null : "fx:id=\"AdminMenu\" was not injected: check your FXML file 'Work.fxml'.";
        assert main_form != null : "fx:id=\"main_form\" was not injected: check your FXML file 'Work.fxml'.";
        assert Change_pass != null : "fx:id=\"Change_pass\" was not injected: check your FXML file 'Work.fxml'.";
        assert Close_menu != null : "fx:id=\"Close_menu\" was not injected: check your FXML file 'Work.fxml'.";
        assert List_of_accs != null : "fx:id=\"List_of_accs\" was not injected: check your FXML file 'Work.fxml'.";
        assert About != null : "fx:id=\"About\" was not injected: check your FXML file 'Work.fxml'.";

    }
}

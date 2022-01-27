package com.example.zd_lab1_javafx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class MainController {

    private Stage stageWork;
    private Stage stageChangePass;
    private Stage stageAbout;
    public static char border = '%';
    private String emptyPass = ";";
    public static String FileName = "input.txt";
    public static String FileNameEnc = "encinput.txt";
    private int counter = 0;

    public void PassAlert() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
        alert.setHeaderText(null);
        alert.setContentText("You must change your password!\nNow your password is an empty string, but it's not safe.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        java.awt.Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }

    public void showWindow(){
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.show();
    }

    @FXML
    private MenuItem AboutButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LogIn_button;

    @FXML
    void OnActionLogin_button(ActionEvent event) throws IOException {
        String login = login_textfield.getText();
        String password = password_textfield.getText().toLowerCase(Locale.ROOT);

        if(password.equals("")){
            password = emptyPass;
        }

        try(FileReader data = new FileReader(FileName))
        {
            error_output.setText("");

            BufferedReader reader = new BufferedReader(data); // для построчного считывания
            String line = reader.readLine();

            while (line != null) {
                // ниже проверяем на соответствие логинов
                String[] buffer = line.split(String.valueOf(border));
                if(buffer.length < 5){
                    line = reader.readLine();
                    continue;
                }
                String checkLogin = buffer[0];
                String checkPassword = "";
                if(buffer[1].equals(emptyPass)){
                    checkPassword = emptyPass;
                }
                else {
                    checkPassword = Encoder.Decrypt(buffer[1], checkLogin);
                }


                if(checkLogin.equals(login) && (checkPassword.equals(password) ||
                                                (password.isEmpty() && checkPassword.equals(emptyPass)))
                    ){
                    if(buffer[4].equals("1")){ // вход админа
                        MainApplication.onlineUser = new Account(login, password, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);

                        stageWork = new Stage();
                        MainApplication.startPage(stageWork, "Work.fxml", "WORK: ADMIN mode", 600, 400);
                        Stage stage = (Stage) main_form.getScene().getWindow();
                        stage.hide();

                        if(MainApplication.onlineUser.checkPassword(";")){
                            PassAlert();
                            stageWork.close();
                            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                            stage.setTitle("Арцишевский А-13-18 ЗД_Лаб_4");
                            stage.setScene(scene);
                            stage.show();

                            stageChangePass = new Stage();
                            MainApplication.startPage(stageChangePass, "ChangePass.fxml", "Change password", 400, 304);
                        }

                        break;
                    }
                    else{ // вход обычного пользователя
                        Boolean isBlocked = buffer[2].equals("1") ? Boolean.TRUE : Boolean.FALSE;
                        Boolean isRestr = buffer[3].equals("1") ? Boolean.TRUE : Boolean.FALSE;

                        if(!isBlocked){
                            MainApplication.onlineUser = new Account(login, password, isBlocked, isRestr, Boolean.FALSE);

                            stageWork = new Stage();
                            MainApplication.startPage(stageWork, "Work.fxml",
                                    "WORK: USER mode. current user - " + login,
                                    600, 400);
                            WorkController.DisableAdminMenu();

                            Stage stage = (Stage) main_form.getScene().getWindow();
                            stage.hide();

                            if(MainApplication.onlineUser.checkPassword(";")){
                                PassAlert();
                                stageWork.close();
                                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
                                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                                stage.setTitle("Арцишевский А-13-18 ЗД_Лаб_4");
                                stage.setScene(scene);
                                stage.show();

                                stageChangePass = new Stage();
                                MainApplication.startPage(stageChangePass, "ChangePass.fxml", "Change password", 400, 304);
                            }

                        }
                        else{
                            error_output.setText("This account is blocked!");
                        }

                        break;
                    }
                }
                else{
                    // проверяем дальше
                    line = reader.readLine();
                }
            }
            if(line == null){
                counter++;
                error_output.setText("Incorrect login or password\nPlease try again!\n"+(3-counter)+" try left");

                if(counter >= 3){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("No tries are left!\nPlease try again later\nProgram will be canceled");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    alert.showAndWait();

                    Stage stage = (Stage) main_form.getScene().getWindow();
                    stage.close();

                    MainApplication.encoder.Encrypt(FileName, FileNameEnc);
                }
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public AnchorPane main_form;

    @FXML
    private MenuItem Close;

    @FXML
    private TextField login_textfield;

    @FXML
    private Label error_output;

    @FXML
    private PasswordField password_textfield;

    @FXML
    void OnActionClose(ActionEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.close();

        MainApplication.encoder.Encrypt(FileName, FileNameEnc);
    }

    @FXML
    void OnActionAboutButton(ActionEvent event) throws IOException {
        stageAbout = new Stage();
        MainApplication.startPage(stageAbout, "About.fxml", "About", 392, 441);
    }

    @FXML
    void initialize() {
        assert AboutButton != null : "fx:id=\"AboutButton\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert LogIn_button != null : "fx:id=\"LogIn_button\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert main_form != null : "fx:id=\"main_form\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert Close != null : "fx:id=\"Close\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert login_textfield != null : "fx:id=\"login_textfield\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert error_output != null : "fx:id=\"error_output\" was not injected: check your FXML file 'MainForm.fxml'.";
        assert password_textfield != null : "fx:id=\"password_textfield\" was not injected: check your FXML file 'MainForm.fxml'.";

    }
}

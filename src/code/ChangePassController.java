package com.example.zd_lab1_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChangePassController {
    private char border = '%';
    private String emptyPass = ";";

    Set<Character> math_operands = new HashSet<Character>() {{
        add('+');
        add('-');
        add('*');
        add('/');
        add('=');
        add('^');
    }};

    private Boolean CheckPasswordRestr(String new_pass){
        char[] new_pass_arr = new_pass.toCharArray();
        int i = 0;
        while(i < new_pass_arr.length){
            if((new_pass_arr[i] > '0' && new_pass_arr[i] < '9') || math_operands.contains(new_pass_arr[i])){
                i++;
            }
            else{
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private void ChangePassword(String new_pass, String old_pass){
        MainApplication.onlineUser.setPassword(new_pass, old_pass);
        new_pass = Encoder.Encrypt(new_pass, MainApplication.onlineUser.getName());

        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(MainController.FileName))))
        {
            String line;
            while((line = br.readLine()) != null){
                String[] buffer = line.split(String.valueOf(border));
                if(buffer[0].equals(MainApplication.onlineUser.getName())){
                    sb.append(line.replace(line, MainApplication.onlineUser.getName()+border+new_pass+border));
                    for(int i = 2; i<buffer.length; i++){
                        sb.append(buffer[i]).append(border);
                    }
                    sb.append("\r\n");
                }
                else{
                    sb.append(line).append("\r\n");
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        try(FileWriter fw = new FileWriter(MainController.FileName)){
            fw.write(sb.toString());
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        ErrorLabel.setTextFill(Paint.valueOf("GREEN"));
        ErrorLabel.setText("Done!");
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button HelpButton;

    @FXML
    private Button ChangeButton;

    @FXML
    private PasswordField new_pass_field;

    @FXML
    private PasswordField old_pass_field;

    @FXML
    private Button CloseButton;

    @FXML
    private PasswordField confirm_new_pass_field;

    @FXML
    private Label ErrorLabel;

    @FXML
    void OnActionChangeButton(ActionEvent event) {

        String old_pass = old_pass_field.getText().equals("") ? emptyPass : old_pass_field.getText();;
        String new_pass = new_pass_field.getText().equals("") ? emptyPass : new_pass_field.getText();;
        String confirm_pass = confirm_new_pass_field.getText().equals("") ? emptyPass : confirm_new_pass_field.getText();;

        if(!(new_pass.equals(old_pass) && new_pass.equals(";"))) {
            if (MainApplication.onlineUser.checkPassword(old_pass)) {
                if (new_pass.equals(confirm_pass)) {
                    ErrorLabel.setText("");

                    // проверка на ограничения
                    if (MainApplication.onlineUser.is_restr_enabled) {
                        if (CheckPasswordRestr(new_pass)) {

                            // смена пароля
                            ChangePassword(new_pass, old_pass);
                        } else {
                            ErrorLabel.setTextFill(Paint.valueOf("RED"));
                            ErrorLabel.setText("New password does not meet the requirements\nPlease, use only allowed symbols");
                        }
                    } else {
                        ChangePassword(new_pass, old_pass);
                    }

                } else {
                    ErrorLabel.setTextFill(Paint.valueOf("RED"));
                    ErrorLabel.setText("New passwords are not equal");
                }
            } else {
                ErrorLabel.setTextFill(Paint.valueOf("RED"));
                ErrorLabel.setText("Old password is incorrect");
            }
        }
        else{
            ErrorLabel.setTextFill(Paint.valueOf("RED"));
            ErrorLabel.setText("You cannot use empty password\nPlease, try again");
        }
    }

    @FXML
    void OnActionCloseButton(ActionEvent event) {
        Stage stage = (Stage) ErrorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void OnActionHelpButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Instruction");
        alert.setHeaderText(null);
        alert.setContentText("USE ONLY THE SYMBOLS BELOW\nNumbers 0-9,\nMath operators: +, -, *, /, =, ^");

        alert.showAndWait();
    }

    @FXML
    void initialize() {
        assert HelpButton != null : "fx:id=\"HelpButton\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert ChangeButton != null : "fx:id=\"ChangeButton\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert new_pass_field != null : "fx:id=\"new_pass_field\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert old_pass_field != null : "fx:id=\"old_pass_field\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert CloseButton != null : "fx:id=\"CloseButton\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert confirm_new_pass_field != null : "fx:id=\"confirm_new_pass_field\" was not injected: check your FXML file 'ChangePass.fxml'.";
        assert ErrorLabel != null : "fx:id=\"ErrorLabel\" was not injected: check your FXML file 'ChangePass.fxml'.";

        if(MainApplication.onlineUser.is_restr_enabled){
            HelpButton.setDisable(false);
        }
        else{
            HelpButton.setDisable(true);
        }
    }
}

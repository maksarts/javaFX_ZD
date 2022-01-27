package com.example.zd_lab1_javafx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;

public class ListOfAccounts implements Initializable {

    private char border = '%';
    private String emptyPass = ";";

    private HashMap<String, Boolean> IsBlocked;
    private HashMap<String, Boolean> IsRestr;
    private HashMap<String, Boolean> IsAdmin;

    @FXML
    private ResourceBundle resources;

    @FXML
    private RadioButton RB_block;

    @FXML
    private Button ButtonApplyChanges;

    @FXML
    private RadioButton RB_button_restr;

    @FXML
    private URL location;

    @FXML
    private Button EditButton;

    @FXML
    private ListView<String> ListAccs;

    @FXML
    private Button CloseButton;

    @FXML
    private TextField NewUser;

    @FXML
    void OnActionCloseButton(ActionEvent event) {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void OnActionEditButton(ActionEvent event) { // добавление пользователя
        if(!NewUser.getText().equals("")){
            boolean flag = false;
            try(FileReader data = new FileReader(MainController.FileName)) {

                BufferedReader reader = new BufferedReader(data); // для построчного считывания
                String line = reader.readLine();

                while (line != null) {
                    // ниже проверяем на соответствие логинов
                    String[] buffer = line.split(String.valueOf(border));
                    if (buffer.length < 5) {
                        line = reader.readLine();
                        continue;
                    }
                    if(buffer[0].equals(NewUser.getText())){
                        flag = true;
                        break;
                    }

                    line = reader.readLine();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(flag){
                ErrorLabel.setTextFill(Paint.valueOf("RED"));
                ErrorLabel.setText("This name is already registered!");
            }
            else {
                ListAccs.getItems().add(NewUser.getText());
                IsBlocked.put(NewUser.getText(), false);
                IsRestr.put(NewUser.getText(), false);
                IsAdmin.put(NewUser.getText(), false);
                String newName = NewUser.getText();
                NewUser.setText("");
                ListAccs.scrollTo(ListAccs.getItems().size() - 1);
                ListAccs.layout();
                ListAccs.edit(ListAccs.getItems().size() - 1);

                try (FileWriter fw = new FileWriter(MainController.FileName, true)) {
                    fw.write(newName + border + emptyPass + border + 0 + border + 0 + border + 0 + "\r\n");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    @FXML
    private Label curUserTextField;

    @FXML
    private Label ErrorLabel;

    /*@FXML
    void RB_block_OnClick(ActionEvent event) {

    }

    @FXML
    void RB_button_restr_OnClick(ActionEvent event) {

    }*/

    @FXML
    void OnActionButtonApplyChanges(ActionEvent event) throws InterruptedException {
        String curUser = curUserTextField.getText();

        if(RB_block.isSelected()){
            IsBlocked.put(curUser, true);
        } else { IsBlocked.put(curUser, false); }

        if(RB_button_restr.isSelected()){
                    IsRestr.put(curUser, true);
                } else { IsRestr.put(curUser, false); }

        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(MainController.FileName))))
        {
            String line;
            while((line = br.readLine()) != null) {
                String[] buffer = line.split(String.valueOf(border));

                String newBlocked = IsBlocked.get(curUser) ? "1" : "0";
                String newRestr = IsRestr.get(curUser) ? "1" : "0";

                if(buffer[0].equals(curUser)) {
                    sb.append(buffer[0]+border+buffer[1]+border).append(newBlocked).append(border);
                    sb.append(newRestr).append(border).append(buffer[4]).append("\r\n");
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

        //ErrorLabel.setText("");
    }

    @FXML
    void initialize() {
        assert EditButton != null : "fx:id=\"EditButton\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert RB_block != null : "fx:id=\"RB_block\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert ButtonApplyChanges != null : "fx:id=\"ButtonApplyChanges\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert curUserTextField != null : "fx:id=\"curUserTextField\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert ListAccs != null : "fx:id=\"ListAccs\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert RB_button_restr != null : "fx:id=\"RB_button_restr\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert CloseButton != null : "fx:id=\"CloseButton\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert NewUser != null : "fx:id=\"NewUser\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";
        assert ErrorLabel != null : "fx:id=\"ErrorLabel\" was not injected: check your FXML file 'ListOfAccounts.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IsBlocked = new HashMap<String, Boolean>();
        IsRestr = new HashMap<String, Boolean>();
        IsAdmin = new HashMap<String, Boolean>();

        try (FileReader data = new FileReader(MainController.FileName)) {

            LinkedList<String> users = new LinkedList<String>();

            BufferedReader reader = new BufferedReader(data); // для построчного считывания
            String line = reader.readLine();
            while (line != null) {
                String[] buffer = line.split(String.valueOf(border));
                if (buffer.length < 5) {
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                    }
                    continue;
                }

                String curLogin = buffer[0];

                String curIsAdmin;
                //if(buffer[4].equals("1")) { curIsAdmin = "(admin)"; } else { curIsAdmin = "(user)"; }
                if(buffer[4].equals("1")) { IsAdmin.put(curLogin, true); } else { IsAdmin.put(curLogin, false); }

                users.add(curLogin); // +" | "+curIsBlocked+" "+curIsRestrictPass);

                if(buffer[2].equals("1")){
                    IsBlocked.put(curLogin, true);
                } else { IsBlocked.put(curLogin, false); }

                if(buffer[3].equals("1")) {
                    IsRestr.put(curLogin, true);
                } else { IsRestr.put(curLogin, false); }
                if(buffer[4].equals("1")) {
                    IsAdmin.put(curLogin, true);
                } else { IsAdmin.put(curLogin, false); }

                // идем дальше по файлу
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ListAccs.getItems().addAll(users);
            ListAccs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    /*String[] curNames = ListAccs.getSelectionModel().getSelectedItem().split(" ");
                    String[] newCurNames = Arrays.copyOfRange(curNames, 0, curNames.length - 2);
                    String curName = String.join(" ", newCurNames);*/

                    String curName = ListAccs.getSelectionModel().getSelectedItem();

                    String toField = !IsAdmin.get(curName) ? curName : curName+" (admin)";

                    curUserTextField.setText(toField);

                    if(IsBlocked.get(curName)){
                        RB_block.setSelected(true);
                    }
                    else{
                        RB_block.setSelected(false);
                    }

                    if(IsRestr.get(curName)){
                        RB_button_restr.setSelected(true);
                    }
                    else{
                        RB_button_restr.setSelected(false);
                    }
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


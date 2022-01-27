package com.example.zd_lab1_javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainApplication extends Application {

    public static Account onlineUser;
    public static FileEncrypt encoder;
    //public static Encoder encoder;

    @Override
    public void start(Stage stage) throws IOException {
        /*
        // Тест шифратора
        Encoder enc = new Encoder();
        String pass = "andreymartyn";
        String cryptpass = enc.Encrypt(pass, "bebra");
        System.out.println("cryptpass = ");
        System.out.println(cryptpass);
        System.out.println("decrypt pass = ");
        System.out.println(enc.Decrypt(cryptpass, "bebra"));
        //
        */

        //тест для лр4
        //FileEncrypt encoder = new FileEncrypt("bebra");
        //encoder.Encrypt("Test.txt", "EncTest.txt");
        //encoder.Decrypt("EncTest.txt");
        //

        File inputFile = new File(MainController.FileNameEnc);
        if(!inputFile.isFile()){
            try(FileWriter fw = new FileWriter(MainController.FileName)){
                fw.write("admin%;%0%0%1\r\n");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("First launch information");
                alert.setHeaderText(null);
                alert.setContentText("Привет!\nПохоже, это первый запуск программы. В корневой папке только что был создан" +
                        " файл input.txt, в котором будет храниться все необходимая информация о зарегистрированных аккаунтах.\n" +
                        "В данный момент зарегистрированный аккаунт только один - логин: admin, пароль: пустая строка. В дальнейшем" +
                        " при работе с программой ты сможешь добавить новых пользователей, заблокировать кого-то из них, включить ограничения " +
                        "на пароли и изменить пароль в любом из аккаунтов. Информация о программе и варианте задания доступна в окне About.\n" +
                        "Приятного использования!"
                );
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                java.awt.Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();

            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }

            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("NewPassPhrase.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 318);
            stage.setTitle("Create Passphrase");
            stage.setScene(scene);
            stage.show();
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("PassPhrase.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 194);
            stage.setTitle("Password Phrase Control");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static void startPage(Stage stage, String name, String title, int v_, int v1_) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(name));
        Scene scene = new Scene(fxmlLoader.load(), v_, v1_);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void startListPage(Stage stage, String name, String title, int v_, int v1_) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(name));
        Scene scene = new Scene(fxmlLoader.load(), v_, v1_);
        stage.setScene(scene);
        stage.setTitle(title);

        stage.show();
    }
}


package com.example.zd_lab1_javafx;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Random;
//import oracle.security.crypto.core.MD4;

public class FileEncrypt {
    private String passphrase;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    FileEncrypt (String newPassphrase){
        passphrase = newPassphrase;
    }

    public void Encrypt(String fileName, String encryptedFileName){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest((passphrase).getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
            KeySpec spec = new PBEKeySpec(new String(messageDigest).toCharArray(), salt, 100, 128);
            SecretKey tmp = keyFactory.generateSecret(spec);
            SecretKeySpec secureKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

            //System.out.println("Secret key = ".concat(new String(secureKey.getEncoded())));

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey);

            /*Чтение файла*/
            String result = "";
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("The file doesn't exist");
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    while (reader.ready()) {
                        result = result.concat(reader.readLine());
                        result = result.concat("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Encrypt delete: ".concat(Boolean.toString(file.delete())));

                /*Шифрование файла*/
                try (FileOutputStream fileOut = new FileOutputStream(encryptedFileName);
                     CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
                        //fileOut.write(iv);
                        cipherOut.write(result.getBytes());
                     }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void Decrypt(String fileName) {
        String content = "";

        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest((passphrase).getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
            KeySpec spec = new PBEKeySpec(new String(messageDigest).toCharArray(), salt, 100, 128);
            SecretKey tmp = keyFactory.generateSecret(spec);
            SecretKeySpec secureKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

            //System.out.println("Secret key = ".concat(new String(secureKey.getEncoded())));


            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secureKey);

            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader)
            ) {

                try (FileWriter fileWriter = new FileWriter(MainController.FileName)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileWriter.write(line.concat("\n"));
                    }
                    fileWriter.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

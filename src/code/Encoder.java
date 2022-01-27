package com.example.zd_lab1_javafx;

import java.util.Arrays;
import java.util.LinkedList;

public class Encoder {
    private static LinkedList<Integer> key;

    private static void CreateKey(String login, int length){
        if(login.length() > length){
            login = login.substring(0, length);
        }

        key = new LinkedList<Integer>();
        key.clear();
        char[] temp = login.toCharArray();
        Arrays.sort(temp);
        String sorted_pass = String.valueOf(temp);

        for(int i=0; i<login.length(); i++){
            char c = login.charAt(i);
            key.add(sorted_pass.indexOf(c));
            sorted_pass = sorted_pass.replaceFirst(String.valueOf(c), String.valueOf('\1'));
        }
    }

    public static String Encrypt(String password, String key_){
        CreateKey(key_, password.length());

        String crypt_pass = "";
        int i = 0; int l = 0;

        while(password.length() % key.size() != 0){
            password = password.concat(" ");
        }

        while(i < password.length()){

            crypt_pass = crypt_pass.concat( String.valueOf(password.charAt(key.get((i % key.size())) + l)) );

            i++;
            if(i % key.size() == 0 && i > 0){
                l += key.size();
            }

        }

        //System.out.println("First encode: ".concat(crypt_pass));

        String crypt_pass_2 = "";  int bytes;
        for(int j=1; j<crypt_pass.length(); j++){
            bytes = (crypt_pass.charAt(j)) ^ (crypt_pass.charAt(j-1));
            crypt_pass_2 = crypt_pass_2.concat("#").concat(String.valueOf(bytes));

            //crypt_pass_2 = crypt_pass_2.concat("#").concat(new String(new byte[]{ Byte.parseByte(String.valueOf(bytes)) }));
            //System.out.println(new String(new byte[]{ Byte.parseByte(String.valueOf(bytes)) }));

        }
        //System.out.println(crypt_pass_2);
        String[] temp = crypt_pass_2.split("#");
        crypt_pass_2 =
                String.valueOf(
                                (crypt_pass.charAt(0)) ^
                                Byte.parseByte(temp[temp.length - 1])
                                ).concat(crypt_pass_2);

        //System.out.println("Second encode: ".concat(crypt_pass_2));
        return crypt_pass_2;
    }

    public static String Decrypt(String crypt_pass_2, String key_){
        CreateKey(key_, crypt_pass_2.length());
        String[] crypt_list_2 = crypt_pass_2.split("#");
        LinkedList<Byte> crypt_list = new LinkedList<Byte>();

        crypt_list.add(
                (byte) (Byte.parseByte(crypt_list_2[0]) ^
                        Byte.parseByte(crypt_list_2[crypt_list_2.length-1]))
        );

        for(int j=1; j<crypt_list_2.length; j++){
            crypt_list.add(
                    (byte) (Byte.parseByte(crypt_list_2[j]) ^
                            crypt_list.get(crypt_list.size()-1))
            );
        }

        String crypt_pass = "";

        for(int k=0; k<crypt_list.size(); k++){
            //crypt_pass = crypt_pass.concat(String.valueOf(crypt_list.get(k)));
            crypt_pass = crypt_pass.concat(new String(new byte[]{ crypt_list.get(k) }));
        }

        //System.out.println("First decode: ".concat(crypt_pass));

        char[] original_pass = crypt_pass.toCharArray();

        int i = 0; int l = 0;
        while(i < crypt_pass.length()){
            original_pass[key.get(i % key.size()) + l] = crypt_pass.charAt(i);
            i++;
            if(i % key.size() == 0 && i > 0){
                l += key.size();
            }
        }
        String password = String.valueOf(original_pass).trim();

        return password;
    }
}

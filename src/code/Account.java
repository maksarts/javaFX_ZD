package com.example.zd_lab1_javafx;

public class Account {
    private String name = "";
    private String password;
    private char border = '%';
    private char emptyPass = ';';

    public Boolean is_block; // = Boolean.FALSE;
    public Boolean is_restr_enabled; // = Boolean.FALSE;
    public Boolean is_admin; // = Boolean.FALSE;

    public String getName(){
        return name;
    }

    // public String getPassword(){ return password; }

    public void setPassword(String password, String current_password) {
        if(current_password.equals(this.password)){
            this.password = password;
        }
    }

    public Boolean checkPassword(String pass_){
        if(pass_.equals(this.password)){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    public Account(String name_, String password_, Boolean is_block_, Boolean is_restr_enabled_, Boolean is_admin_){
        name = name_;
        password = password_;
        is_block = is_block_;
        is_restr_enabled = is_restr_enabled_;
        is_admin = is_admin_;
    }
}

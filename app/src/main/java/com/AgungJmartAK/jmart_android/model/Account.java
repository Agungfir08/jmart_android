package com.AgungJmartAK.jmart_android.model;

public class Account extends Serializable{
    public double balance;
    public String email;
    public String name;
    public String password;
    public Store store;

    public Account(double balance, String email, String name, String password, Store store, int id){
        this.id = id;
        this.balance = balance;
        this.email = email;
        this.name = name;
        this.password = password;
        this.store = store;
    }

    @Override
    public String toString() {
        return ""+balance;
    }
}

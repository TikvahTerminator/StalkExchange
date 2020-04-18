package com.example.stalkexchange;

public class Users {
    private String id;
    private String name;
    private String island;
    private String dodoCode;

    public Users(String i, String nom, String isl, String dodo){
        id = i;
        name = nom;
        island = isl;
        dodoCode = dodo;
    }

    public Users(String i, String nom, String isl){
        id = i;
        name = nom;
        island = isl;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIsland() {
        return island;
    }

    public String getDodoCode() {
        return dodoCode;
    }
}

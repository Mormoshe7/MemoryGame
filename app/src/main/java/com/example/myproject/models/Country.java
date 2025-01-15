package com.example.myproject.models;

public class Country {
    private String name;
    private String nativeName;
    private String flag;

    public Country(String name, String nativeName, String flag) {
        this.name = name;
        this.nativeName = nativeName;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
package com.example.myproject.models;

public class ImageButtonClass {
    private int id;
    private int arrId;

    public ImageButtonClass(int id, int arrId) {
        this.id = id;
        this.arrId = arrId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrId() {
        return arrId;
    }

    public void setArrId(int arrId) {
        this.arrId = arrId;
    }
}

package com.example.bilstop.Classes;

public class Car {

    private String brand;
    private String licencePlate;
    private String color;
    private String model;
    private String pp;

    public Car(){

    }

    public Car(String brand, String licencePlate, String color, String model, String pp) {
        this.brand = brand;
        this.licencePlate = licencePlate;
        this.color = color;
        this.model = model;
        this.pp = pp;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", color='" + color + '\'' +
                ", model='" + model + '\'' +
                ", pp='" + pp + '\'' +
                '}';
    }
}

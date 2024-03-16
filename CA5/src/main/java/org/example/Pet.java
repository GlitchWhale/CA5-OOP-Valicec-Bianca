package org.example;

public class Pet {
    private int id;
    private int petId;
    private double price;
    private String name;
    private String petType;

    // Constructor
    public Pet(int petId, double price, String name, String petType) {
        this.petId = petId;
        this.price = price;
        this.name = name;
        this.petType = petType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    // toString() method to represent org.example.Pet object as a String
    @Override
    public String toString() {
        return "org.example.Pet{" +
                "id=" + id +
                ", petId=" + petId +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", petType='" + petType + '\'' +
                '}';
    }
}

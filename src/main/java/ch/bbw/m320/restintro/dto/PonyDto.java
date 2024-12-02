package ch.bbw.m320.restintro.dto;

import java.time.LocalDate;

public class PonyDto {
    public String name;
    public int age;
    public int id;
    public double height;
    public double weight;
    public boolean isRidden;
    public LocalDate birthday;
    public String favouriteFood;


    public PonyDto() {}

    public PonyDto(String name, int age, int id, double height, double weight, boolean isRidden, LocalDate birthday, String favouriteFood) { // Geändert zu birthday
        this.name = name;
        this.age = age;
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.isRidden = isRidden;
        this.birthday = birthday;
        this.favouriteFood = favouriteFood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isRidden() {
        return isRidden;
    }

    public void setRidden(boolean ridden) {
        isRidden = ridden;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public void setFavouriteFood(String favouriteFood) {
        this.favouriteFood = favouriteFood;
    }

    // ... Getter und Setter ...
}
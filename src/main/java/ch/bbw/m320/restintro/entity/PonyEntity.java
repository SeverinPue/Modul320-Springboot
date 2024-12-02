package ch.bbw.m320.restintro.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class PonyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public int age;
    public double height;
    public double weight;
    public boolean isRidden;
    public LocalDate birthday;
    public String favouriteFood;

    public PonyEntity() {}

    public PonyEntity(String name, int age, double height, double weight, boolean isRidden, LocalDate birthday, String favouriteFood) { // Geändert zu birthday
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.isRidden = isRidden;
        this.birthday = birthday;
        this.favouriteFood = favouriteFood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
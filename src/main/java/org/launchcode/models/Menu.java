package org.launchcode.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters.")
    private String name;

    @ManyToMany
    private List<Cheese> cheeses;

    public Menu(String name) {
        this.name = name;
    }

    public Menu() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Cheese item) {
        cheeses.add(item);
    }

    public void removeItem(Cheese item) {
        cheeses.remove(item);
    }

    public List<Cheese> getItems() {
        return cheeses;
    }

}

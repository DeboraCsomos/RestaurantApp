package com.codecool.restauratio.models;

import com.codecool.restauratio.models.users.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`order`")
public class Order extends Request{
    private String adress;
    @ManyToMany
    @JoinTable(
            name = "Order_Food",
            joinColumns = { @JoinColumn(name = "order_id")},
            inverseJoinColumns = { @JoinColumn(name = "food_id")}
    )
    private List<Food> foodList;

    @ManyToOne
    @JoinColumn(name = "target_order_restaurant_id")
    private Restaurant orderRestaurant;

    @ManyToOne
    private User user;

    public Order(Date date, String address, List<Food> foodList, User user, Restaurant restaurant) {
        super(date);
        this.adress = address;
        this.foodList = foodList;
        this.orderRestaurant = restaurant;
        this.user = user;

    }

    public Order() {
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public Restaurant getOrderRestaurant() {
        return orderRestaurant;
    }

    public void setOrderRestaurant(Restaurant orderRestaurant) {
        this.orderRestaurant = orderRestaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

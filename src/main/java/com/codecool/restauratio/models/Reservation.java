package com.codecool.restauratio.models;

import com.codecool.restauratio.models.users.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllReservations",
                query = "SELECT r FROM Reservation r"),
        @NamedQuery(
                name = "getReservationById",
                query = "select r FROM Reservation r where r.id = :id")
})
public class Reservation extends Request{

    @Column(name = "number_of_people")
    private int numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "reservation_restaurant_id")
    private Restaurant reservationRestaurant;

    @ManyToOne
    private User guest;


    public Reservation(Date date, int numberOfPeople, Restaurant r, User g) {
        super(date);
        this.numberOfPeople = numberOfPeople;
        this.reservationRestaurant = r;
        this.guest = g;
    }


    public Reservation() {
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    public Restaurant getReservationRestaurant() {
        return reservationRestaurant;
    }

    public void setReservationRestaurant(Restaurant reservationRestaurant) {
        this.reservationRestaurant = reservationRestaurant;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    @Override
    public String toString() {
        return "Reservation = " +
                "numberOfPeople: " + numberOfPeople +
                ", Restaurant: " + reservationRestaurant.getName() +
                ", guest: " + guest.getUserName();
    }
}

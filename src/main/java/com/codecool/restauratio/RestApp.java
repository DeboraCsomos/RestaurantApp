package com.codecool.restauratio;

import com.codecool.restauratio.controller.LoginController;
import com.codecool.restauratio.controller.RestaurantController;
import com.codecool.restauratio.customException.ConnectToDBFailed;
import com.codecool.restauratio.models.Food;
import com.codecool.restauratio.models.Order;
import com.codecool.restauratio.models.Restaurant;
import com.codecool.restauratio.services.UserService;
import com.codecool.restauratio.utils.DatabaseUtility;
import com.codecool.restauratio.models.Reservation;
import com.codecool.restauratio.models.users.User;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class RestApp {

    public static void populateDb(EntityManager em) {

        EntityTransaction transaction = em.getTransaction();

        User user1 = new User("józsi", "hurka", true, false);
        User user2 = new User("bodri", "mecska", true, true);

        Date date = new Date();

        Food f = new Food("Melák Menü", 1500, "szenya, rántotthus, rántottsajt", "király");
        Food f2 = new Food("buja burger", 200, "burger", "jó");
        Food f3 = new Food("Tele Tál Falafel", 4000, "minden ami blefér", "ragya");

        List<Food> list = new ArrayList<>();
        list.add(f);
        List<Food> list2 = new ArrayList<>();
        list2.add(f);
        list2.add(f2);
        List<Food> list3 = new ArrayList<>();
        list3.add(f);
        list3.add(f2);
        list3.add(f3);

        Restaurant r = new Restaurant("Halászcsárda", "good", "here", list, 100, user1, "/img/halasz_image.jpg");
        Restaurant r2 = new Restaurant("Csirkés", "pretty", "Mány", list2, 50, user2, "/img/csirkes_image.jpeg");
        Restaurant r3 = new Restaurant("Titiz", "bad", "Mány", list3, 10, user2, "/img/titiz_image.jpg");

        Order o1 = new Order(date, "here", list, user1, r);
        Order o2 = new Order(date, "there", list3, user2, r2);

        Reservation reservation = new Reservation(date, 100, r, user2);

        transaction.begin();
        em.persist(user1);
        em.persist(user2);
        em.persist(f);
        em.persist(f2);
        em.persist(f3);
        em.persist(r);
        em.persist(r2);
        em.persist(r3);
        em.persist(o1);
        em.persist(o2);
        em.persist(reservation);
        transaction.commit();
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        EntityManager em = DatabaseUtility.getEntityManager("restaurantPU");
        populateDb(em);


        enableDebugScreen();

        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);


        get("/", (req, res) -> {
            try {
                return new ThymeleafTemplateEngine().render(RestaurantController.renderRestaurants(req, res));
            } catch (ConnectToDBFailed e) {
                res.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return "<html><body><h1>" + res.raw().getStatus() + "</h1><p>SERVICE UNAVAILABLE</p></body></html>";
            }
        });

        // LOGIN ROUTES

        get("/login", (request, response) -> new ThymeleafTemplateEngine().render( LoginController.renderLogin( request, response, true ) ));

        get("/register", (request, response) -> new ThymeleafTemplateEngine().render( LoginController.renderRegister( request, response, true ) ));

        post("/user/register", (Request req, Response res) -> {

            if(!userService.doesUserExist(req.queryParams("username"))){

                int userId = userService.registerUser(req.queryParams("username"), req.queryParams("password"), false, false);
                req.session().attribute("id",userId);
                req.session().attribute("username",req.queryParams("username"));
                res.redirect("/");
            }else{
                res.redirect("/register?inuse=true");
            }
            return null;
        });

        post("/user/login", (Request req, Response res) -> {

            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(userService.login(username, password)){

                req.session().attribute("id",userService.getUserId(username));
                req.session().attribute("username", username);
                System.out.println("sessionId: " + req.session().attribute("id"));
                res.redirect("/");
            }else{
                res.redirect("/login?incorrect=true");
            }
            return null;
        });

        get("/logout", (Request req, Response res) -> {
            req.session().removeAttribute("id");
            req.session().removeAttribute("username");
            res.redirect("/");
            return null;
        });

        post("/api/get_restaurant_by_location", RestaurantController::restaurantBrowseByLocation);

        // RESTAURANT ROUTE

        get( "/restaurants/:restId", (request, response) -> new ThymeleafTemplateEngine().render( RestaurantController.renderRestaurant(request, response, request.params( ":restId" )) ));

    }
}

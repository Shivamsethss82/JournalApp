package com.shivam.journalApp.controller;

import com.shivam.journalApp.api.response.WeatherResponse;
import com.shivam.journalApp.entity.User;
import com.shivam.journalApp.service.UserService;
import com.shivam.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Autowired
    private WeatherService weatherService;

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName) {
        User userInDb = userService.findByUserName(userName);

        if (userInDb != null) {
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            User updatedUser = userService.saveNewUser(userInDb);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/greeting")
    public ResponseEntity<?> greeting() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        WeatherResponse haryana = weatherService.getWeather("Farrukhabad");

        String greeting = "Hi " + authentication.getName();
        if (haryana != null && haryana.getCurrent() != null) {
            greeting += ", weather feels like " + haryana.getCurrent().getFeelslike() + "°C";
        }

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

}
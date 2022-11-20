package org.RestAssured.example;

import org.RestAssured.example.model.PlayerRq;

import java.util.Base64;
import java.util.UUID;

public class DataGenerator {

    public static PlayerRq randomPlayerRq() {

        String userName = UUID.randomUUID().toString();
        String userPassword = Base64.getEncoder().encodeToString(userName.substring(0, 16).getBytes());

        PlayerRq player = PlayerRq.builder()
                .username(userName)
                .email(userName + "@gmail.com")
                .password_repeat(userPassword)
                .password_change(userPassword)
                .build();

        return player;
    }
}
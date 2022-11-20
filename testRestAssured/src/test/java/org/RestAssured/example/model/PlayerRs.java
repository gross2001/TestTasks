package org.RestAssured.example.model;

import lombok.Data;

@Data
public class PlayerRs {

    private String id;
    private String country_id;
    private String timezone_id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String gender;
    private String phone_number;
    private String birthdate;
    private boolean bonuses_allowed;
    private boolean is_verified;

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }
}

package org.RestAssured.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRq {
    private String username;
    private String password_change;
    private String password_repeat;
    private String email;

}

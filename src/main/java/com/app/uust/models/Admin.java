package com.app.uust.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@ToString
@Document(collection = "Admin")
public class Admin {

    @Id
    private String id;

    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

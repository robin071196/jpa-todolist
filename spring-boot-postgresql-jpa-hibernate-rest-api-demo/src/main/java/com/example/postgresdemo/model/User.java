package com.example.postgresdemo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
public class User extends AuditModel {
    @Id
    @NotBlank
    private String username;
    public String getUsername() { return username;}
    public void setUsername(String username){this.username = username;}

    @NotBlank
    @Column(name = "full_name")
    private String name;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
}
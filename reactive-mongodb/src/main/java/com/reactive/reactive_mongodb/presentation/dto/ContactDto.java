package com.reactive.reactive_mongodb.presentation.dto;

public class ContactDto {

    private String id;
    private String name;
    private String email;
    private String phone;
    
    public ContactDto() {
    }

    public ContactDto(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ContactDto [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + "]";
    }
    
}

package com.project.reactor;

public class UserEntity {

    private String name;
    private String lastName;
    
    public UserEntity() {}

    public UserEntity(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", lastName=" + lastName + "]";
    }
    
}

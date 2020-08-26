package com.example.cluster;

public class User {

    //User Info
    private String email;
    private String password;
    private String name;

    public User() {
    }

    /**
     * Constructor for User
     * @param email user's email
     * @param password user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor for User
     * @param email user's email
     * @param password user's password
     * @param name user's name
     */
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    /**
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email
     * @param email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password for the user
     * @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the user
     * @param name User's name
     */
    public void setName(String name) {
        this.name = name;
    }
}

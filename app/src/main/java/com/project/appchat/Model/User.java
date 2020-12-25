package com.project.appchat.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String sex;
    private String phone;
    private String status;
    private String search;

    public User() {
    }

    public User(String id, String username, String imageURL, String sex, String phone, String status, String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.sex = sex;
        this.phone = phone;
        this.status = status;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}

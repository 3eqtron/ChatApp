package com.example.chat_bknj.model;

public class users {
    private String id;
    private String name;
    private String imageURL;
    private String status;

    public users(String id, String name, String imageURL,String status) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
         this.status=status;;
    }

    public users() {
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
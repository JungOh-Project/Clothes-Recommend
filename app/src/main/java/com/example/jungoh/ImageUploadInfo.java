package com.example.jungoh;

public class ImageUploadInfo {


    public String email;
    public String imageURL;
    public String type;

    public String getImageURL(){
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }


    public ImageUploadInfo(String email, String imageURL, String type) {
        this.imageURL = imageURL;
        this.type = type;
        this.email=email;
    }
}
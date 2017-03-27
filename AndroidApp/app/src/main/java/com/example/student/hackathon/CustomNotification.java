package com.example.student.hackathon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by student on 3/26/2017.
 */

public class CustomNotification implements Serializable
{
    @SerializedName("text")
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.example.student.hackathon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by student on 3/25/2017.
 */

public class UserAccount implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private ArrayList<ClassInfo> classInfoList;

    public UserAccount(int id, String username, ArrayList<ClassInfo> classInfoList)
    {
        this.id = id;
        this.username = username;
        this.classInfoList = classInfoList;
    }

    public String getUsername()
    {
        return this.username;
    }


    public ClassInfo getClass(int i)
    {
        return classInfoList.get(i);
    }

    public ArrayList<ClassInfo> getAllClasses()
    {
        return this.classInfoList;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", classInfoList=" + classInfoList +
                '}';
    }
}

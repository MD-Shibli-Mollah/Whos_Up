package com.example.student.hackathon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by student on 3/25/2017.
 */

public class UsersOnline implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ArrayList<UserAccount> userAccountList;

    public UsersOnline(ArrayList<UserAccount> userAccountList)
    {
        this.userAccountList = userAccountList;
        for( int i = 0 ; i < userAccountList.size ( ) ; i++)
        {
        	System.out.println( userAccountList.get(i).getUsername());
        }
    }

    public int getUserOnlineCount()
    {
        return this.userAccountList.size();
    }

    public UserAccount getUser(int i)
    {
        return this.userAccountList.get(i);
    }


}


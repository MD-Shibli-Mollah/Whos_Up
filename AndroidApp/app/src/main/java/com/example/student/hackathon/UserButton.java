package com.example.student.hackathon;

import android.content.Context;
import android.widget.Button;

/**
 * Created by student on 3/25/2017.
 */

public class UserButton extends Button
{

    private final Context context;
    private final String action;
    private final UserAccount user;

    public UserButton(Context context, String action, UserAccount user)
    {
        super(context);


        this.context = context;
        this.action = action;
        this.user = user;
        super.setText(action);
    }

    public String getUsername()
    {
        return this.user.getUsername();
    }
}

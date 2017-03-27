package com.example.student.hackathon;

/**
 * Created by student on 3/26/2017.
 */

public class TextFragmentThread implements Runnable
{

    public static String message = "NO_MESSAGE";

    @Override
    public void run()
    {
        while (true)
        {
            if (!(message.equals("NO_MESSAGE") || message == null))
            {
                TextFragment.updateText(message);
                message = "NO_MESSAGE";
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

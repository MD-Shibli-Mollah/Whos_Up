package com.example.student.hackathon;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 3/25/2017.
 */

public class NetworkThread implements Runnable
{

    public static volatile ArrayList<Command> commandArrayList = new ArrayList<>();
    private Socket client;
    private long lastEchoCall;


    @Override
    public void run()
    {
        while(true)
        {
            try {
                Log.w("SERVER", "Trying to log in");
                client = new Socket("10.25.30.113", 25801);


                Log.w("SERVER", "Client connection " + (client.isConnected() ? "true" : "false"));
                Log.w("SERVER", "Connected");

                lastEchoCall = System.currentTimeMillis();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try
        {
            while (true) {


                long currentTime = System.currentTimeMillis();



                System.out.println(commandArrayList.size() != 0 ? commandArrayList.size() : "No commands on queue");
                for (int i = 0; i < commandArrayList.size(); i++) {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                        commandArrayList.get(i).addCommand(client.getInetAddress().toString());
                        oos.writeObject(commandArrayList.get(i));
                        System.out.println("Wrote command to server " + commandArrayList.get(i).getCommand());
                        commandArrayList.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (client.getInputStream().available() != 0)
                {

                    Log.w("SERVER", "SERVER SEND AN OBJECT");
                    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

                    Object buffer = ois.readObject();


                    if (buffer instanceof Command)
                    {
                        Command cBuffer = ((Command)buffer);
                        Log.w("SERVER", cBuffer.getCommand().equals("echo") ? "" : cBuffer.getCommand());

                        ArrayList<String> commandList = cBuffer.toArrayList();
                        String commandFlag = commandList.get(0);
                        if (commandFlag.equals("loginError"))
                        {
                            MainActivity.createToast("Wrong Credentials");
                        }
                        else if (commandFlag.equals("creatingAccountError"))
                        {
                            MainActivity.createToast("Username already used");
                        }
                        else if (commandFlag.equals("roomID"))
                        {
                            TextFragment.roomID = commandList.get(1);
                        }
                        else if (commandFlag.equals("smsMessage"))
                        {
                            Gson gson = new Gson();
                            CustomNotification notifs = gson.fromJson(commandList.get(1), CustomNotification.class);
                            TextFragmentThread.message = notifs.getMessage();

                            return;
                        }
                    }
                    else if (buffer instanceof UserAccount)
                    {
                        MainActivity.userAccount = ((UserAccount) buffer);
                        Log.w("SERVER", "UserAccount set");

                        MainActivity.killLoginPage();
                    }
                    else if (buffer instanceof UsersOnline)
                    {
                        MainActivity.userOnlineList = ((UsersOnline) buffer);
                        Log.w("SERVER", "UserOnlineList set");
                    }



                }

                Thread.sleep(1000);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

}

package com.example.student.hackathon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by student on 3/25/2017.
 */

public class Command implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String commandList = "";

    public Command()
    {

    }

    public Command(String command)
    {
        commandList = command;
    }


    public void addCommand(String command)
    {
        commandList += command + "#splitchar#";
    }


    public String getCommand()
    {
        return this.commandList;
    }

    public ArrayList<String> toArrayList()
    {
        List<String> bufferList = Arrays.asList(this.getCommand().split("#splitchar#"));
        ArrayList<String> commandList = new ArrayList<>();
        commandList.addAll(bufferList);

        return commandList;
    }


}

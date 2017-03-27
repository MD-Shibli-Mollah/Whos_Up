package com.example.student.hackathon;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionWorker implements Runnable
{
	
	private ServerSocket ss;
	private Socket cs;

	public ServerConnectionWorker(ServerSocket ss) 
	{
		this.ss = ss;
	}

	
	public void addServerSocket(ServerSocket ss)
	{
		this.ss = ss;
	}
	
	@Override
	public void run() 
	{
		
		System.out.println("waiting for connection");
		while (true)
		{
			try
			{
				cs = ss.accept();
				OpenConnectionHandler.getInstance().addPlayerSocket(cs);
				System.out.println("Added " + this.cs);
				
			} 
			catch (Exception e)
			{
				System.out.println ("Exception in run") ;
			}
		}
		
	}

}
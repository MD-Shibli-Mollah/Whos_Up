package com.example.student.hackathon;

import java.io.IOException;
import java.net.ServerSocket;



public class Server
{
	
	private int port;
	private ServerSocket ss;
	private Thread connectionWorker;
	public static Thread openConnections;
	
	
	public static void main(String[] args)
	{
		//UserAccount bruno = DBHandler.getLogin("bruno", "password") ;
		//System.out.println( "User name = " + bruno.getUsername ( ));
	
		new Server(25801).start();
	}
	
	public Server(int port)
	{
		this.port = port;
	}
	
	public void start()
	{
		openConnections = new Thread(OpenConnectionHandler.getInstance());
		openConnections.start();
		try 
		{
			this.ss = new ServerSocket(this.port);
			connectionWorker = new Thread(new ServerConnectionWorker(ss));
			connectionWorker.start();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

}

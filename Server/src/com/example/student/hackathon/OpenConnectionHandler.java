package com.example.student.hackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenConnectionHandler implements Runnable
{

	private volatile static ArrayList<PlayerSocket> currentUsers = new ArrayList<>();
	
	
	private static OpenConnectionHandler singleton;
	
	public static OpenConnectionHandler getInstance()
	{
		if (singleton == null)
		{
			singleton = new OpenConnectionHandler();
		}
		
		return singleton;
	}
	
	private OpenConnectionHandler() 
	{
		
	}

	
	public void addPlayerSocket(Socket s)
	{
		PlayerSocket buff = new PlayerSocket(s);
		currentUsers.add(buff);
	}
	
	@Override
	public void run() 
	{
		while (true)
		{
			try 
			{
				//System.out.println("Users online " + currentUsers.size());
				
				for (int i = 0; i < currentUsers.size(); i++)
				{
				
					//System.out.println("---Processing IP " + currentUsers.get(i).getSocket().getInetAddress() + "---");
					
					//I think this feature works
					if (!currentUsers.get(i).isAlive())
					{
						System.out.println("user not alive");
						currentUsers.get(i).close();
						currentUsers.remove(i);
						continue;
					}
										
					if (currentUsers.get(i).isReady() != 0)
					{
						System.out.println("Reading command from " + currentUsers.get(i));
						ObjectInputStream ois = new ObjectInputStream(currentUsers.get(i).getSocket().getInputStream());
						Command buffer = (Command) ois.readObject();
						System.out.println(buffer.getCommand());
						String commandFlag = buffer.toArrayList().get(0);
						ArrayList<String> bufferList = buffer.toArrayList();
				
						System.out.println( commandFlag );
						if (commandFlag.contains("login"))
						{
							ArrayList<String> commandList = buffer.toArrayList();
							
						    System.out.println(commandList.get(1));
							ObjectOutputStream obs = new ObjectOutputStream(currentUsers.get(i).getSocket().getOutputStream());
							UserAccount account = DBHandler.getLogin(commandList.get(1), commandList.get(2));
							
							
							if (account != null)
							{
								obs.writeObject(account);
								System.out.println("User logged in " + account.getUsername());
								currentUsers.get(i).addUserAccount(account);
							}
							else
							{
								Command error = new Command();
								error.addCommand("loginError");
								error.addCommand("wrongCredentials");
								obs.writeObject(error);
							}
							
							
						}
						else if (commandFlag.contains(("echo")))
						{
							System.out.println("Got echo from client");
							//now we have to send an echo back to the client
						}
						else if (commandFlag.contains("usersOnline"))
						{
							//Command usersOnline = new Command();
							//usersOnline.addCommand("usersOnline");
							System.out.println ( "in userOnline") ;
							ArrayList<UserAccount> userAccountList = new ArrayList<>();
							for (int j = 0; j < currentUsers.size();j++)
							{
								if (currentUsers.get(j).getUserAccount()!= null)
								{
									userAccountList.add(currentUsers.get(j).getUserAccount());
									System.out.println(currentUsers.get(j).getUserAccount());
								}
							}
							UsersOnline userOnline = new UsersOnline(userAccountList);
							ObjectOutputStream obs = new ObjectOutputStream(currentUsers.get(i).getSocket().getOutputStream());
							obs.writeObject(userOnline);
						}
						else if (commandFlag.contains("createSmsRoom"))
						{
							JSONObject message = new JSONObject();
							System.out.println("got sms");
							message.put("title", bufferList.get(1) + bufferList.get(2));
							
							CloseableHttpClient httpClient = HttpClientBuilder.create().build();

							try {
							    HttpPost request = new HttpPost("https://api.ciscospark.com/v1/rooms");
							    StringEntity params = new StringEntity(message.toString());
							    request.addHeader("content-type", "application/json; charset=utf-8");
							    request.addHeader("authorization", "auth token");
							    request.setEntity(params);
							    HttpResponse response = httpClient.execute(request);
							    
							    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							    String output;
							    System.out.println("JSON Response");
						
							    output = br.readLine();
							    
							    System.out.println(output);
							    
							    
							   
							    JSONObject res = new JSONObject(output);
							    
							   
							    //JSONObject jb1 = res.getJSONObject("id");
							    //System.out.println("jb1 "  + jb1);
							    //String ourId = jb1.getString("id");
							    //System.out.println("ourId" + ourId);
							   // System.out.println("ID " + obj);
							  
								ObjectOutputStream obs = new ObjectOutputStream(currentUsers.get(i).getSocket().getOutputStream());
								Command sendRoomId = new Command();
								sendRoomId.addCommand("roomID");
								sendRoomId.addCommand(res.getString("id"));
								obs.writeObject(sendRoomId);
								
							} catch (Exception ex) {
							    ex.printStackTrace();
							} finally {
							    httpClient.close();
							}
						}
						else if (commandFlag.contains("sendMessage"))
						{
							JSONObject message = new JSONObject();
							System.out.println("send message");
							message.put("roomId", bufferList.get(1));
							message.put("text", "randomText :D");
							System.out.println(message);
							
							
							CloseableHttpClient httpClient = HttpClientBuilder.create().build();
							String url = "https://api.ciscospark.com/v1/messages";
							try {
							    HttpPost request = new HttpPost(url);
							    StringEntity params = new StringEntity(message.toString());
							    request.addHeader("content-type", "application/json; charset=utf-8");
							    request.addHeader("authorization", "auth token");
							    request.addHeader("cache-control", "no-cache");
							    request.setEntity(params);
							    
							    
							    HttpResponse response = httpClient.execute(request);
							    
							    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							    String output;
							    System.out.println("JSON Response");
						
							    
							    output = br.readLine();
							    
							    System.out.println(output);
							    
							    
							   
							    //JSONObject res = new JSONObject(output);
							    
							   
							    //JSONObject jb1 = res.getJSONObject("id");
							    //System.out.println("jb1 "  + jb1);
							    //String ourId = jb1.getString("id");
							    //System.out.println("ourId" + ourId);
							   // System.out.println("ID " + obj);
							  
								ObjectOutputStream obs = new ObjectOutputStream(currentUsers.get(i).getSocket().getOutputStream());
								Command sendRoomId = new Command();
								sendRoomId.addCommand("smsMessage");
								sendRoomId.addCommand(output);
								obs.writeObject(sendRoomId);
								
							} catch (Exception ex) {
							    ex.printStackTrace();
							} finally {
							    httpClient.close();
							}
							
							
						}
						else if (commandFlag.contains("askMessage"))
						{
							JSONObject message = new JSONObject();
							System.out.println("Asking message");
							message.put("roomId", bufferList.get(1));
							System.out.println("roomId = " + bufferList.get(1));
							
							CloseableHttpClient httpClient = HttpClientBuilder.create().build();
							String url = "https://api.ciscospark.com/v1/messages" +"?roomId=" +bufferList.get(1);
							try {
							    HttpGet request = new HttpGet(url);
							    StringEntity params = new StringEntity(message.toString());
							    request.addHeader("content-type", "application/json; charset=utf-8");
							    request.addHeader("authorization", "auth token");
							    request.addHeader("cache-control", "no-cache");
							    
							    System.out.println("Url " + url + "...");
							    HttpResponse response = httpClient.execute(request);
							    
							    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							    String output;
							    System.out.println("JSON Response");
						
							    
							    output = br.readLine();
							    
							    System.out.println(output);
							    
							    
							   
							    //JSONObject res = new JSONObject(output);
							    
							   
							    //JSONObject jb1 = res.getJSONObject("id");
							    //System.out.println("jb1 "  + jb1);
							    //String ourId = jb1.getString("id");
							    //System.out.println("ourId" + ourId);
							   // System.out.println("ID " + obj);
							  
								ObjectOutputStream obs = new ObjectOutputStream(currentUsers.get(i).getSocket().getOutputStream());
								Command sendRoomId = new Command();
								sendRoomId.addCommand("smsMessage");
								sendRoomId.addCommand(output);
								obs.writeObject(sendRoomId);
								
							} catch (Exception ex) {
							    ex.printStackTrace();
							} finally {
							    httpClient.close();
							}
						}
						
						
						
						
					}
				}
				
				
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				
				e.printStackTrace();
			}
		}
		
	}
	

}

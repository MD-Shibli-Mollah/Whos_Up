package com.example.student.hackathon;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBHandler {
	
	private static boolean isConnected = false ;
	static ResultSet rs = null;
	
	static String crn ;
	static int user_id ;
	static String userName ;
	
	public static UserAccount getLogin ( String user, String pass ) 
	{
		ArrayList<ClassInfo> cinfo = new ArrayList<ClassInfo> ( ) ;
		// check for connection, connect if needed
		if ( true )
		{
			
			Connection conn = null;

			try {
			    //conn =
			       //DriverManager.getConnection("jdbc:mysql://localhost/whos_up?" +
			       //                            "user=root&password=root");
			    		
			    Class.forName("org.sqlite.JDBC" );
			    conn = DriverManager.getConnection("jdbc:sqlite:whos_up.db") ;

			    // Do something with the Connection
			    Statement stmt = null;
			    try {
			        stmt = conn.createStatement();
			        String sqlString = "select " +
					        "users.user_id, " +
					        "users.nick, " +
					        "sections.cname, " +
					        "contact_perms.sms_ok, " +
					        "contact_perms.voice_ok, " +
					        "contact_perms.video_ok, " + 
					        "sections.crn " +
					      "from " +
					        "users " +
					      "inner join enrollments on users.user_id = enrollments.user_id " +
					      "inner join sections on enrollments.crn = sections.crn " +
					      "inner join contact_perms on contact_perms.user_id = users.user_id " +
					       "  and contact_perms.crn = sections.crn " +
					       "where " + 
					       "users.nick = '" + user + "' and users.password = '" + pass + "'"
					       ;
			        System.out.println( "sql string = " + sqlString );
			        rs = stmt.executeQuery( sqlString );
			       
			        // Now do something with the ResultSet ....
			        System.out.println( rs.isBeforeFirst());
			        while ( rs.next ( ) )
			        {
			        	crn = rs.getInt( 7 ) + "";
			        	user_id = rs.getInt(1) ;
			        	userName = rs.getString(2) ;
			        	System.out.println("user name =" + userName );
			        	String cname = rs.getString(3) ;
			        	
			        	boolean smsOk = rs.getString(4 ).equals("Y") ;
			        	boolean voiceOk = rs.getString(5).equals("Y") ;
			        	boolean videoOk = rs.getString(6).equals("Y") ;
			        	
			        	ClassInfo cResult = new ClassInfo ( crn, cname, smsOk, voiceOk, videoOk ) ;
			        	cinfo.add ( cResult) ;
			        }
			    }
			   
			    catch (SQLException ex){
			        // handle any errors
			        System.out.println("SQLException: " + ex.getMessage());
			        System.out.println("SQLState: " + ex.getSQLState());
			        System.out.println("VendorError: " + ex.getErrorCode());
			    }
			    finally {
			        // it is a good idea to release
			        // resources in a finally{} block
			        // in reverse-order of their creation
			        // if they are no-longer needed

			        if (rs != null) {
			            try {
			                rs.close();
			            } catch (SQLException sqlEx) { } // ignore

			            rs = null;
			        }

			        if (stmt != null) {
			            try {
			                stmt.close();
			            } catch (SQLException sqlEx) { } // ignore

			            stmt = null;
			        }
			    }
			    
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println( "sqlite driver problem");
				e.printStackTrace();
			}
			isConnected = true ;
		}
		
		return new UserAccount ( user_id, userName, cinfo ) ;
	}
	
	
}

package app;

import org.junit.Test;

public class MySQLJDBCDriverTest {
	
	public static void main(String[] args) {
	    try {
	    	// Up-To-Date Treiber: com.mysql.cj.jdbc.Driver //
	      Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	      System.out.println("Good to go");
	    } catch (Exception E) {
	      System.out.println("JDBC Driver error");
	    }
	  }
	}
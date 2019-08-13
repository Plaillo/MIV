package paketDatenbankzugriff;

public class MySQLJDBCDriverTest {
	  public static void main(String[] args) {
	    try {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      System.out.println("Good to go");
	    } catch (Exception E) {
	      System.out.println("JDBC Driver error");
	    }
	  }
	}
package tools.db;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Database {
	
	private DataSource mysql_ds;
	private static DB mongo_db;
	private static String username = "gr3_husseini";
	private static String password = "gr3_husseini$";
	private static String mysql_dBname = "gr3_husseini";
	private static String mongo_dBname = "gr3_husseini";
	
	
	public Database(String jndiname) throws SQLException {
		try {
			mysql_ds = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
		} catch (NamingException e) {
			throw new SQLException(jndiname + " is missing in JNDI! : " + e.getMessage());
		}
	}
	
	private Connection getConnection() throws SQLException {
		return mysql_ds.getConnection();
	}
	
	public static Connection getMySQLConnection() throws SQLException, InstantiationException, IllegalAccessException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return (DriverManager.getConnection("jdbc:mysql://132.227.201.129:33306/" + Database.mysql_dBname,
					Database.username, Database.password));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static DB getMongoDB() {
		Mongo m;
		try {
			m = new Mongo("132.227.201.129", 27130);
			mongo_db = m.getDB(Database.mongo_dBname);
			mongo_db.authenticate(Database.username, Database.password.toCharArray());
			return mongo_db;
		} catch (UnknownHostException | MongoException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DBCollection getMongoCollection(String collection) {
		DBCollection coll = mongo_db.getCollection(collection);
		return coll;
	}
}

package tools.db;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Database {
	
	private DataSource mysql_ds;
	private static DB mongo_db;
	private static String username = "gr3_husseini";
	private static String password = "/*...*/";
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
			// DB-as-a-service by MongoLab
			m = new Mongo("ds057000.mongolab.com", 57000);
			// li328.lip6 server
			//m = new Mongo("132.227.201.129", 27130);
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
	
	public static void createCollection (String name) {
		DB dbs = Database.getMongoDB();
		//DBObject o = {capped:true, size:5242880, max:5000};
		BasicDBObject o = new BasicDBObject();
		o.put("capped", true);
		o.put("size", 5242880);
		o.put("max", 5000);
		dbs.createCollection(name, o);
	}
	
	public static Set<String> getAllCollections () {
		DB dbs = Database.getMongoDB();
		Set<String> colls = dbs.getCollectionNames();
		return colls;
	}
}

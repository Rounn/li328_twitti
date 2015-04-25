package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class DBSearchEngineTools {
	
	public static void mapReduceWord (DBObject filterQuery) throws SQLException {
		String m = DBSearchEngineTools.mapWord();
		String r = DBSearchEngineTools.reduceWord();
		
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		MapReduceCommand cmd = new MapReduceCommand (comments, m, r, null, MapReduceCommand.OutputType.INLINE, filterQuery);
		MapReduceOutput out = comments.mapReduce(cmd);
		
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			
			for (DBObject o:out.results()) {
				// Not sure of o.keySet...
				String q = "insert into worddf (word, df) values ('" + o.toString() + "', '" + o + "');";
				s.executeUpdate(q);
			}
			
			s.close();
			c.close();			
		} catch (SQLException e) {
			throw new SQLException("key already exists" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static String mapWord () {
		String m = "function  m () {";
		m += "var words = this.comment.match(/\\w+/g);";
		m += "df=[]";
		m += "for(var=i;i<words.length;i++){";
		m += "if(df[word[i]]==null)";
		m += "df[word[i]]=1}";
		m += "for(k in df.keys())";
		m += "emit(k, {df=1});}";
		return m;
	}
	
	public static String reduceWord () {
		String r = "function r (key, values)";
		r += "var s = 0;";
		r += "for (v in values)";
		r += "s = s + v;";
		r += "return({word:key, df:s});";
		return r;
	}
}

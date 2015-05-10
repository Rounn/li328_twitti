package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class DBSearchEngineTools {
	
	public static JSONObject mapReduceDFWord (DBObject filterQuery) throws SQLException, JSONException {
		JSONObject js = new JSONObject();
		String m = DBSearchEngineTools.mapDFWord();
		String r = DBSearchEngineTools.reduceDFWord();
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		MapReduceCommand cmd = new MapReduceCommand (comments, m, r, null, MapReduceCommand.OutputType.INLINE, null);
		MapReduceOutput out = comments.mapReduce(cmd);
		
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			int i=0;
			for (DBObject o:out.results()) {
				i++;
				// Not sure of o.keySet...
				js.put("word"+i, o.toString());
				js.put("word"+i, o);
				String q = "insert into word_df (word, df) values (" + o.get("_id") + ", " + ((DBObject) o.get("value")).get("df") + ");";
				s.executeUpdate(q);
			}
			
			s.close();
			c.close();	
			return js;
		} catch (SQLException e) {
			throw new SQLException("SQL Error for df: " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return js;
	}
	
	public static JSONObject mapReduceTFWord (DBObject filterQuery) throws SQLException, JSONException {
		JSONObject js = new JSONObject();
		String m = DBSearchEngineTools.mapTFWord();
		String r = DBSearchEngineTools.reduceTFWord();
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		js.put("1", "1");
		MapReduceCommand cmd = new MapReduceCommand (comments, m, r, null, MapReduceCommand.OutputType.INLINE, null);
		MapReduceOutput out = comments.mapReduce(cmd);
		js.put("2", "2");
		try {
			int df = 0;
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			for (DBObject o:out.results()) {
				String q = "select * from word_df where word = " + o.get("_id");
				s.executeQuery(q);
				ResultSet rs = s.getResultSet();
				if (rs.next()) {
					df = rs.getInt("df");
					js.put("In tf df =", df);
				}
				if (df == 0)
					return js;
				else if (df == 1) {
					String qtf = "insert into word_tf VALUES (" + o.get("_id") + ", '"
							+ ((DBObject) o.get("value")).get("document") + "', "
							+ ((DBObject) o.get("value")).get("tf") + ")";
					js.put("TF: qtf", qtf);

					s.executeUpdate(qtf);
				}
				else {
					BasicDBList o_list = (BasicDBList) ((DBObject) o.get("value"))
							.get("tfs");
					for (Object tmp : o_list) {
						DBObject obj = (DBObject) tmp;
						String qtf = "INSERT INTO word_tf VALUES(" + o.get("_id")
								+ ", '" + obj.get("document") + "', " + obj.get("tf")
								+ ")";
						js.put("TF: qtf else", qtf);
						s.executeUpdate(qtf);
					}
				}
				rs.close();
			}
			s.close();
			c.close();	
			return js;
		} catch (SQLException e) {
			throw new SQLException("SQL Error for tf:" + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return js;
	}
	
	public static String mapDFWord () {
		String m = "function mapdf() {"
				+ "var text = this.comment;"
				+ "var mots = text.match(/\\w+/g);"
				+ "if (mots == null)return;"
				+ "var df = [];"
				+ "for ( var i = 0; i < mots.length; i++) {"
				+ "m =\"\\\"\" + mots[i] + \"\\\"\";"
				+ "df[m] = 1;"
				+ "}"
				+ "for ( var mot in df)"
				+ "emit(mot, {df : 1});}";
		return m;
	}
	
	public static String reduceDFWord () {
		String r = "function reducedf(key, values) {"
				+ "var total = 0;"
				+ "for ( var i = 0; i < values.length; i++)"
				+ "total += values[i].df;"
				+ "return {df : total};"
				+ "}";
		return r;
	}
	
	public static String mapTFWord () {
		String m = "function maptf() {"
				+ "var mots = this.comment.match(/\\w+/g);"
				+ "var tf = [];"
				+ "for ( var i = 0; i < mots.length; i++) {"
				+ "m = \"\\\"\" + mots[i] + \"\\\"\";"
				+ "if (tf[m] != null)"
				+ "tf[m]++;"
				+ "else "
				+ "tf[m] = 1;"
				+ "}"
				+ "for ( var w in tf)"
				+ "emit (w, {document : this._id,tf : tf[w]});"
				+ "}";
		return m;
	}
	
	public static String reduceTFWord () {
		String r = "function reducetf(key, values) {"
				+ "return {tfs : values};"
				+ "}";
		return r;
	}

	public static JSONObject computeRSVWord (long count) {
		JSONObject js = new JSONObject();
		Connection c;
		try {
			c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			
			String q = "select * from word_tf;";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				String word = rs.getString("word");
				js.put("word", word);
				String id = rs.getString("doc");
				js.put("doc", id);
				double rsv = rs.getInt("tf");
				js.put("tf", rsv);
				String qtd = "select * from word_df where word = '" + word + "'";
				Statement stf = c.createStatement();
				ResultSet rstf = stf.executeQuery(qtd);
				rstf.next();
				rsv -= Math.log10(count/rstf.getInt("df"));
				js.put("rsv", rsv);
				String newq = "insert into word_RSV values ('" + id + "', '" + word + "', "+ rsv + ")";
				
				stf.executeUpdate(newq);
				rstf.close();
				stf.close();
			}
			
			rs.close();
			s.close();
			c.close();	
			return js;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return js;
	}
	
	public static long countComments () {
		long count = 0;
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		count = comments.count();
		
		return count;
	}
	
	public static void cleanTables () {
		Connection c;
		try {
			c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			
			String q = "delete from word_df;";
			s.execute(q);
			
			q = "delete from word_tf;";
			s.execute(q);
			
			q = "delete from word_RSV;";
			s.execute(q);
			
			s.close();
			c.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}

package edu.umd.umiacs.tn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import edu.umd.umiacs.tn.model.Algorithm;
import edu.umd.umiacs.tn.model.AlgorithmTopic;
import edu.umd.umiacs.tn.model.DocTopic;
import edu.umd.umiacs.tn.model.DocumentId;
import edu.umd.umiacs.tn.model.Press;
import edu.umd.umiacs.tn.model.SDocument;
import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.model.SenatorTopic;
import edu.umd.umiacs.tn.model.Topic;
import edu.umd.umiacs.tn.model.WordProp;

public class DatabaseUtil {

	private static final HashMap<String, String> docId_actorId = new HashMap<String, String>();
	static {
		getDocIdActorId();
	}

	public static List<DocumentId> getDocumentIds() {

		Connection c = null;
		Statement stmt = null;
		List<DocumentId> outputs = new ArrayList<DocumentId>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT distinct(DOCID) from DOCUMENT  ORDER BY date(DOCTIME) DESC;");
			String docid = "";
			while (rs.next()) {
				docid = rs.getString("DOCID");
				DocumentId di = new DocumentId();
				di.setName(docid);
				di.setDescription("docid");
				outputs.add(di);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return outputs;
	}

	public static List<SDocument> getDocuments(String senatorId) {

		Connection c = null;
		Statement stmt = null;
		List<SDocument> outputs = new ArrayList<SDocument>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT DOCID, DOCCONTENT from DOCUMENT where ACTORID='"
							+ senatorId + "'  ORDER BY date(DOCTIME) DESC;");

			String docId;
			String docContent;
			String summary;
			while (rs.next()) {
				docId = rs.getString("DOCID");
				docContent = rs.getString("DOCCONTENT").replaceAll("[\\r\\n]+",
						" ");
				summary = docContent.substring(0,
						Math.min(500, docContent.length()));

				SDocument s = new SDocument();
				s.setDocId(docId);
				s.setSummary(summary);

				outputs.add(s);

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return outputs;
	}

	/**
	 * get all senators
	 * 
	 * @return
	 */
	public static List<Senator> getSenators() {

		Connection c = null;
		Statement stmt = null;
		List<Senator> senators = new ArrayList<Senator>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ACTORID, ACTORNAME from ACTOR order by ACTORID;");
			while (rs.next()) {
				String senatorId = rs.getString("ACTORID");
				String senatorName = rs.getString("ACTORNAME");
				Senator s = new Senator(senatorId, senatorName);
				senators.add(s);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return senators;
	}

	public static List<DocTopic> getDocTopic(String senatorName,
			String algorithmName) {
		List<DocTopic> output = new ArrayList<DocTopic>();
		List<String> topicIdList = getTopicIdList(algorithmName);
		List<String> docIdList = getDocIdList(senatorName); // sorted by title

		for (int i = 0; i < docIdList.size(); i++) {
			HashMap<String, String> topicPropHash = getTopicProp(docIdList
					.get(i));
			for (int j = 0; j < topicIdList.size(); j++) {
				String topic_name[] = topicIdList.get(j).split("\\|");
				if (topicPropHash.containsKey(topic_name[0])) {
					DocTopic dt = new DocTopic();
					dt.setDocId(docIdList.get(i));

					dt.setTopicId(topic_name[0]);
					dt.setTopicName(topic_name[1]);
					dt.setProp(topicPropHash.get(topic_name[0]));

					dt.setDocIndex(i);
					output.add(dt);
					break;
				}
			}
		}

		return output;
	}

	private static HashMap<String, String> getTopicProp(String docId) {
		Connection c = null;
		Statement stmt = null;
		HashMap<String, String> output = new HashMap<String, String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOPICID, TOPICPROP from DOC_TOPIC where DOCID='"
							+ docId + "';");

			String topicId = "", prop = "";

			while (rs.next()) {
				topicId = rs.getString("TOPICID");
				prop = rs.getString("TOPICPROP");
				output.put(topicId, prop);

			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return output;

	}

	public static Press getDoc(String docId) {

		Connection c = null;
		Statement stmt = null;
		Press p = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT DOCCONTENT from DOCUMENT where DOCID='"
							+ docId + "';");

			String docContent = "";

			if (rs.next()) {
				docContent = rs.getString("DOCCONTENT");
				p = new Press();
				p.setDocId(docId);
				p.setDocContent(docContent);

			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return p;
	}

	private static String getTopicId(String topicName) {
		Connection c = null;
		Statement stmt = null;
		String topicId = "";
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOPICID from TOPIC where TOPICNAME='"
							+ topicName + "';");

			if (rs.next()) {
				topicId = rs.getString("TOPICID");
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return topicId;
	}

	public static Topic getTopic(String topicName) {
		String topicId = getTopicId(topicName);
		Connection c = null;
		Statement stmt = null;
		Topic t = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT WORDID, CAST(WORDPROP as DECIMAL(9,2)) _WORDPROP from TOPIC_WORD where TOPICID='"
							+ topicId + "' order by _WORDPROP desc limit 20;");

			String wordid;
			double wordprop;
			List<WordProp> wpList = new ArrayList<WordProp>();
			while (rs.next()) {
				wordid = rs.getString("WORDID");
				wordprop = rs.getDouble("_WORDPROP");

				WordProp wp = new WordProp();
				wp.setWord(wordid);
				wp.setProp(wordprop + "");
				wpList.add(wp);
			}
			if (wpList.size() > 0) {
				t = new Topic();
				t.setTopicId(topicId);
				t.setWp(wpList);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return t;
	}

	private static List<String> getTopicIdList(String algorithm) {
		Connection c = null;
		Statement stmt = null;
		List<String> output = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOPICID, TOPICNAME from TOPIC where ALGORITHM='"
							+ algorithm + "' order by TOPICNAME;");

			String topicId = "";
			String topicName = "";
			while (rs.next()) {
				topicId = rs.getString("TOPICID");
				topicName = rs.getString("TOPICNAME");
				output.add(topicId + "|" + topicName);

			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return output;
	}

	private static void getDocIdActorId() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT DOCID, ACTORID from DOCUMENT;");

			String docId = "";
			String actorId = "";

			while (rs.next()) {
				docId = rs.getString("DOCID");
				actorId = rs.getString("ACTORID");
				docId_actorId.put(docId, actorId);
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	private static List<String> getDocIdList(String actorId) {
		Connection c = null;
		Statement stmt = null;
		List<String> output = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			// TODO: this is slow, next version will convert all to date time
			// cos it's in right format
			ResultSet rs = stmt
					.executeQuery("SELECT DOCID from DOCUMENT where ACTORID='"
							+ actorId + "' ORDER BY date(DOCTIME) DESC;");

			String docId = "";
			while (rs.next()) {
				docId = rs.getString("DOCID");
				output.add(docId);
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return output;
	}

	private static TreeMap<String, Double> getSenatorProp(String topicId) {
		Connection c = null;
		Statement stmt = null;
		TreeMap<String, Double> output = new TreeMap<String, Double>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT DOCID, TOPICPROP from DOC_TOPIC where TOPICID = '"
							+ topicId + "';");

			String docId = "";
			String topicProp = "";
			String actorId = "";
			while (rs.next()) {
				docId = rs.getString("DOCID");
				topicProp = rs.getString("TOPICPROP");
				actorId = docId_actorId.get(docId);
				if (output.containsKey(actorId)) {
					output.put(actorId,
							output.get(actorId) + Double.parseDouble(topicProp));
				} else {

					output.put(actorId, Double.parseDouble(topicProp));
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return output;
	}

	/**
	 * 
	 * @param algorithm
	 * @return
	 */
	public static List<SenatorTopic> getSenatorTopic(String algorithm) {

		List<SenatorTopic> output = new ArrayList<SenatorTopic>();

		List<String> topicIdList = getTopicIdList(algorithm);

		for (int i = 0; i < topicIdList.size(); i++) {
			String topic_name[] = topicIdList.get(i).split("\\|");
			TreeMap<String, Double> senator_prop = getSenatorProp(topic_name[0]); // TODO:
																					// fix
																					// this
			for (String senator : senator_prop.keySet()) {
				SenatorTopic st = new SenatorTopic();
				st.setSenatorId(senator);
				st.setTopicId(topic_name[0]);
				st.setTopicName(topic_name[1]);
				st.setTimeStamp("050607");
				st.setFreq((int) Math.floor(senator_prop.get(senator)));
				output.add(st);

			}
		}

		return output;

	}

	/**
	 * 
	 * @return
	 */
	public static List<Algorithm> getAlgorithms() {
		// TODO need to create a table for this
		List<Algorithm> algs = new ArrayList<Algorithm>();

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ALGORITHM, ALGORITHMNAME  from ALGORITHM order by ALGORITHMNAME;");

			String algorithm;
			String algorithmName;
			while (rs.next()) {
				algorithm = rs.getString("ALGORITHM");
				algorithmName = rs.getString("ALGORITHMNAME");
				Algorithm algo = new Algorithm();
				algo.setDescription(algorithm);
				algo.setName(algorithmName);
				algs.add(algo);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return algs;

	}

	public static List<AlgorithmTopic> getAlgorithmTopic(String docId) {
		List<AlgorithmTopic> outputs = new ArrayList<AlgorithmTopic>();
		List<String> topicIds = getTopicIds(docId);
		for (int i = 0; i < topicIds.size(); i++) {
			String algorithmName_topicName = getAlgorithmName_TopicName(topicIds
					.get(i));
			String[] values = algorithmName_topicName.split("\\|");
			if (values != null && values.length == 2) {
				AlgorithmTopic at = new AlgorithmTopic();
				at.setAlgorithmName(values[0]);
				at.setTopicName(values[1]);
				outputs.add(at);
			}
		}

		return outputs;
	}

	private static String getAlgorithmName_TopicName(String topicId) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ALGORITHM, TOPICNAME from TOPIC where TOPICID='"
							+ topicId + "';");
			if (rs.next()) {
				return rs.getString("ALGORITHM") + "|"
						+ rs.getString("TOPICNAME");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	private static List<String> getTopicIds(String docId) {
		Connection c = null;
		Statement stmt = null;
		List<String> outputs = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOPICID  from DOC_TOPIC where DOCID='"
							+ docId + "' order by TOPICID;");
			String topicId;
			while (rs.next()) {
				topicId = rs.getString("TOPICID");
				outputs.add(topicId);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return outputs;
	}

	public static void createTable(String database, String sqlScript) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + database);

			stmt = c.createStatement();

			stmt.executeUpdate(sqlScript);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void insertLabel(String docid, String label) {
		String insertScript = "INSERT INTO DOC_LABEL (DOCID, LABEL) VALUES (?, ?)";
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			PreparedStatement ps = c.prepareStatement(insertScript);
			ps.setString(1, docid);
			ps.setString(2, label);
			ps.addBatch();
			ps.executeBatch();
			c.commit();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static List<String> getUniqLabels() {
		Connection c = null;
		Statement stmt = null;
		List<String> outputs = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(false);
			// System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT distinct(LABEL)  from DOC_LABEL order by LABEL;");
			String label;
			while (rs.next()) {
				label = rs.getString("LABEL");
				outputs.add(label);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return outputs;
	}

	public static void removeLabel() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/newpress.db");
			c.setAutoCommit(true);
			// System.out.println("Opened database successfully");

			stmt = c.createStatement();
			stmt.executeUpdate("DELETE from DOC_LABEL;");
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}
}

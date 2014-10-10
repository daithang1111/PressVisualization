package edu.umd.umiacs.tn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.umd.umiacs.tn.model.Algorithm;
import edu.umd.umiacs.tn.model.Press;
import edu.umd.umiacs.tn.model.PressData;
import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.model.SenatorTopic;
import edu.umd.umiacs.tn.model.Topic;

public class DatabaseUtil {

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
			c = DriverManager.getConnection("jdbc:sqlite:dbs/press.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT SENATORID from TOPIC_RESULT group by SENATORID order by SENATORID;");
			while (rs.next()) {
				String senatorId = rs.getString("SENATORID");
				Senator s = new Senator(senatorId, senatorId);
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

	public static List<PressData> getPressData(String senatorName,
			String algorithmName) {

		Connection c = null;
		Statement stmt = null;
		List<PressData> pressData = new ArrayList<PressData>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/press.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * from TOPIC_RESULT where SENATORID='"
							+ senatorName
							+ "' and TOPICID like '"
							+ algorithmName + "%' order by TOPICID, TIMESTAMP;");

			String senatorId = "";
			String timestamp = "";
			String docId = "";

			String topicId = "";
			String prop = "";

			int count = 1;
			while (rs.next()) {
				senatorId = rs.getString("SENATORID");
				timestamp = rs.getString("TIMESTAMP");
				docId = rs.getString("DOCID");

				topicId = rs.getString("TOPICID");
				prop = rs.getString("PROP");

				//
				PressData pd = new PressData();
				pd.setDocId(docId);
				pd.setDocIndex(count + "");
				pd.setProp(Double.parseDouble(prop));
				pd.setSenatorId(senatorId);
				pd.setTimeStamp(timestamp);
				pd.setTopicId(topicId);
				pressData.add(pd);
				count++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return pressData;
	}

	public static Press getDoc(String docId) {

		Connection c = null;
		Statement stmt = null;
		Press p = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/press.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT DOCID, DOCCONTENT from PRESS_DATA where DOCID='"
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

	public static Topic getTopic(String topicId) {

		Connection c = null;
		Statement stmt = null;
		Topic t = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/press.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOPICID, TOPICCONTENT from TOPIC where TOPICID='"
							+ topicId + "';");

			String topicContent = "";

			if (rs.next()) {
				topicContent = rs.getString("TOPICCONTENT");
				t = new Topic();
				t.setTopicId(topicId);
				t.setTopicContent(topicContent);
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

	public static List<SenatorTopic> getSenatorTopic(String algorithm) {
		Connection c = null;
		Statement stmt = null;
		List<SenatorTopic> output = new ArrayList<SenatorTopic>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dbs/press.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT SENATORID, TOPICID, count(DOCID) as FREQ  from TOPIC_RESULT where TOPICID like '"
							+ algorithm
							+ "%' group by SENATORID, TOPICID order by SENATORID, TOPICID;");

			String senatorId = "";
			String topicId = "";
			int freq = 0;

			String timeStamp = "2014"; // TODO, need to slice times
			while (rs.next()) {
				senatorId = rs.getString("SENATORID");
				topicId = rs.getString("TOPICID");
				freq = rs.getInt("FREQ");
				SenatorTopic st = new SenatorTopic();
				st.setFreq(freq);
				st.setSenatorId(senatorId);
				st.setTimeStamp(timeStamp);
				st.setTopicId(topicId);
				output.add(st);
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
	 * @return
	 */
	public static List<Algorithm> getAlgorithms() {
		// TODO need to create a table for this
		List<Algorithm> algs = new ArrayList<Algorithm>();
		Algorithm mrlda = new Algorithm();
		mrlda.setDescription("mrlda desc");
		mrlda.setName("mrlda");

		Algorithm seededLDA = new Algorithm();
		seededLDA.setDescription("seededLDA desc");
		seededLDA.setName("seededLDA");

		Algorithm codebook = new Algorithm();
		codebook.setDescription("codebook desc");
		codebook.setName("codebook");

		algs.add(mrlda);
		algs.add(seededLDA);
		algs.add(codebook);
		return algs;

	}
}

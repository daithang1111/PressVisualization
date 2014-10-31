package edu.umd.umiacs.tn.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.umd.umiacs.tn.model.AlgorithmTopic;
import edu.umd.umiacs.tn.util.DatabaseUtil;

public class Test {

	public static void main(String[] args) throws Exception {

//		DatabaseUtil.insertLabel("testDoc2", "fun label");
//		String t = "ABC";
//		System.out.println(t.substring(0, 100));
		
//		String a ="x\takd\tf";
//		
//		System.out.println(a.replace("\t"," "));
		
		//DatabaseUtil.removeLabel();
		List<String> labels = DatabaseUtil.getUniqLabels();
		for(int i=0;i<labels.size();i++){
			System.out.println(labels.get(i));
		}
		
		// String sqlScript = "PRAGMA encoding = \"UTF-8\";"
		// + " DROP TABLE IF EXISTS DOC_LABEL;"
		// + " CREATE TABLE DOC_LABEL"
		// + " (   "
		// + "rowID       INTEGER PRIMARY KEY AUTOINCREMENT,    "
		// + "DOCID	varchar(100),    "
		// + "LABEL      varchar(500));   "
		// + "CREATE INDEX X_LABEL ON DOC_LABEL (LABEL);";
		//
		// DatabaseUtil.createTable("dbs/newpress.db", sqlScript);
		
		// List<SenatorTopic>
																																																																																				// sts
																																																																																				// =
																																																																																				// DatabaseUtil.getSenatorTopic();
		// for(int i=0;i<100;i++){
		// sts.get(i).print();
		// }

		// List<Senator> sens = DatabaseUtil.getSenators();
		// System.out.println(sens.size());
		//

		// Topic t = DatabaseUtil.getTopic("SeededLDA1");
		// List<WordProp> list = t.getWp();
		// for(int i=0;i<list.size();i++){
		// System.out.println(list.get(i).getProp());
		// }

		// List<AlgorithmTopic> ats =
		// DatabaseUtil.getAlgorithmTopic("10apr2007akaka175.txt");
		//
		// for(int i=0;i<ats.size();i++){
		// System.out.println(ats.get(i).getTopicName()+":"+ats.get(i).getAlgorithmName());
		// }

		// List<DocTopic> dts = DatabaseUtil.getDocTopic("leahy", "codebook");
		// System.out.println(dts.size());

		// for (int i = 0; i < sens.size(); i++) {
		// System.out.println(sens.get(i).getName());
		// }
		//

		// long start =System.currentTimeMillis();
		// List<SenatorTopic> sts = DatabaseUtil.getSenatorTopic("codebook");
		// long end =System.currentTimeMillis();
		// System.out.println(end-start);
		//
		// for(int i =0;i<10;i++){
		// sts.get(i).print();
		// }

		// List<Algorithm> algs =DatabaseUtil.getAlgorithms();
		// for(int i=0;i<algs.size();i++){
		// System.out.println(algs.get(i).getName());
		// }
		//
		// List<PressData> pressList = DatabaseUtil.getPressData("akaka",
		// "mrlda");
		// for(int i=0;i<pressList.size();i++){
		// System.out.println(pressList.get(i).getDocId());
		// }

		// List<Algorithm> al = DatabaseUtil.getAlgorithms();
		// for (int i = 0; i < al.size(); i++) {
		// System.out.println(al.get(i).getName());
		// }
	}

	
}

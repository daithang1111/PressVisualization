package edu.umd.umiacs.tn.test;

import java.util.List;

import edu.umd.umiacs.tn.model.DocTopic;
import edu.umd.umiacs.tn.util.DatabaseUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		// List<SenatorTopic> sts = DatabaseUtil.getSenatorTopic();
		// for(int i=0;i<100;i++){
		// sts.get(i).print();
		// }

//		List<Senator> sens = DatabaseUtil.getSenators();
//		System.out.println(sens.size());
//		
		
		
		
		
//		Topic t = DatabaseUtil.getTopic("SeededLDA1");
//		List<WordProp> list = t.getWp();
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i).getProp());
//		}
		
		List<DocTopic> dts = DatabaseUtil.getDocTopic("leahy", "codebook");
		System.out.println(dts.size());
		
//		for (int i = 0; i < sens.size(); i++) {
//			System.out.println(sens.get(i).getName());
//		}
//		
		
//		long start =System.currentTimeMillis();
//		List<SenatorTopic> sts = DatabaseUtil.getSenatorTopic("codebook");
//		long end =System.currentTimeMillis();
//		System.out.println(end-start);
//		
//		for(int i =0;i<10;i++){
//			sts.get(i).print();
//		}
		
		
		
//		List<Algorithm> algs =DatabaseUtil.getAlgorithms();
//		for(int i=0;i<algs.size();i++){
//			System.out.println(algs.get(i).getName());
//		}
//		
//		List<PressData> pressList = DatabaseUtil.getPressData("akaka", "mrlda");
//		for(int i=0;i<pressList.size();i++){
//			System.out.println(pressList.get(i).getDocId());
//		}
		
//		List<Algorithm> al = DatabaseUtil.getAlgorithms();
//		for (int i = 0; i < al.size(); i++) {
//			System.out.println(al.get(i).getName());
//		}
	}

}

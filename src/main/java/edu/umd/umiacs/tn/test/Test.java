package edu.umd.umiacs.tn.test;

import java.util.List;

import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.util.DatabaseUtil;

public class Test {

	public static void main(String[] args)throws Exception {
//		List<SenatorTopic> sts = DatabaseUtil.getSenatorTopic();
//		for(int i=0;i<100;i++){
//			sts.get(i).print();
//		}
		
		
		List<Senator> sens = DatabaseUtil.getSenators();
		for(int i=0;i<sens.size();i++){
			System.out.println(sens.get(i).getName());
		}
	}

}

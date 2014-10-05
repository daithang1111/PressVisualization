package edu.umd.umiacs.tn.test;

import edu.umd.umiacs.tn.util.DatabaseUtil;

public class Test {

	public static void main(String[] args)throws Exception {
		System.out.println(DatabaseUtil.getTopic("mrlda1").getTopicContent());
	}

}

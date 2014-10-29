package edu.umd.umiacs.tn.model;

import java.util.List;

public class Topic {
	String topicId;
	List<WordProp> wp;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public List<WordProp> getWp() {
		return wp;
	}

	public void setWp(List<WordProp> wp) {
		this.wp = wp;
	}

	public void print(){
		System.out.println("topicId:"+topicId+", word0:"+wp.get(0).getWord()+", prop0:"+wp.get(0).getProp());
	}
}

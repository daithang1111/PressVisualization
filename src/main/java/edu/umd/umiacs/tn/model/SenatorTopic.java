package edu.umd.umiacs.tn.model;

public class SenatorTopic {
	String timeStamp;
	String senatorId;
	String topicId;
	String topicName;
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	int freq;

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSenatorId() {
		return senatorId;
	}

	public void setSenatorId(String senatorId) {
		this.senatorId = senatorId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public void print() {
		System.out.println("senator:" + senatorId + ", " + "topic:" + topicId
				+ ", frequency:" + freq);
	}
}

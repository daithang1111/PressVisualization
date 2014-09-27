package edu.umd.umiacs.tn.model;

public class PressData {
	String time;
	String senatorName;
	String topicName;
	double topicProp;

	public PressData(String time, String senatorName, String topicName,
			double topicProp) {
		this.time = time;
		this.senatorName = senatorName;
		this.topicName = topicName;
		this.topicProp = topicProp;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSenatorName() {
		return senatorName;
	}

	public void setSenatorName(String senatorName) {
		this.senatorName = senatorName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public double getTopicProp() {
		return topicProp;
	}

	public void setTopicProp(double topicProp) {
		this.topicProp = topicProp;
	}

}

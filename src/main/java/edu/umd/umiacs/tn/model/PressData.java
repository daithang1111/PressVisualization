package edu.umd.umiacs.tn.model;

public class PressData {
	String timeStamp;
	String senatorId;
	String docIndex;
	String docId;
	String topicId;
	double prop;

	public String getDocIndex() {
		return docIndex;
	}

	public void setDocIndex(String docIndex) {
		this.docIndex = docIndex;
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

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public double getProp() {
		return prop;
	}

	public void setProp(double prop) {
		this.prop = prop;
	}

}

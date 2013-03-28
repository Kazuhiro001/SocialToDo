package jp.sdnaKensyu.socialtodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
	private	String name;
	private Date deadLine;
	private int priority;
	private String infomation;
	private int group;
	private String[] userNames = new String[10];

	public Task(String name, Date deadLine, int priority, String userName) {
		this.name = name;
		this.deadLine = deadLine;
		this.priority = priority;
		this.userNames[0] = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public void setDeadLine(String deadLineString){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'/'MM'/'dd'");
		Date deadLine = new Date();
		try{
			deadLine = sdf1.parse(deadLineString);
			this.deadLine = deadLine;
		}catch(ParseException e) {
		}
	}

	public void setDeadLineTime(String deadLineString){
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH':'mm'");
		Date deadLine = new Date();
		try{
			deadLine = sdf1.parse(deadLineString);
			this.deadLine = deadLine;
		}catch(ParseException e) {
		}
	}
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getInfomation() {
		return infomation;
	}

	public void setInfomation(String infomation) {
		this.infomation = infomation;
	}

	public int getGroup(){
		return group;
	}

	public void setGroup(int group){
		this.group = group;
	}
}
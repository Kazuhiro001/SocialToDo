package jp.sdnaKensyu.socialtodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Pair;

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

	public String getDeadLineToString(){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf1.format(deadLine);

	}
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public void setDeadLine(String deadLineString){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		Date deadLine = new Date();
		try{
			deadLine = sdf1.parse(deadLineString);
			this.deadLine = deadLine;
		}catch(ParseException e) {
		}
	}

	public void setDeadLineTime(String deadLineString){
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
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
	
	public List<NameValuePair> requestTaskEntry(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("projectId", Integer.toString(this.getGroup())));
		params.add(new BasicNameValuePair("taskName", this.getName()));
		params.add(new BasicNameValuePair("taskContents", this.getInfomation()));
		params.add(new BasicNameValuePair("expectFinishTime", this.getDeadLineToString()));
		params.add(new BasicNameValuePair("Deadline", this.getDeadLineToString()));
		params.add(new BasicNameValuePair("importantDegree", this.getDeadLineToString()));
		return params;
	}

}
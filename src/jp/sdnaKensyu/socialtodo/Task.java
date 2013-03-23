package jp.sdnaKensyu.socialtodo;

import java.util.Date;

public class Task {
	private	String name;
	private Date deadLine;
	private int priority;

	public Task(String name, Date deadLine, int priority) {
		this.name = name;
		this.deadLine = deadLine;
		this.priority = priority;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}

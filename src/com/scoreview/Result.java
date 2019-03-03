package com.scoreview;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
	private String subid;
	private int score;
	private Date date;
	public Result(String subid, int score, Date date) {
		this.subid=subid;
		this.score=score;
		this.date=date;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Result() {
		
	}
	
}

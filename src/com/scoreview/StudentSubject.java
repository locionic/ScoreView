package com.scoreview;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StudentSubject {
	private String id;
	
	private String subid;
	public StudentSubject(String id, String subid) {
		this.id = id;
		this.subid = id;
	}
	public StudentSubject() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
}

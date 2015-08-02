package com.example.pojo;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

public class Headline {
	
	String title, link, source,sourceBy;
	
	
	public Headline(String title, String link) {
		super();
		this.title = title;
		this.link = link;
	}
	
	public Headline(){
		
	}
	
	public String getSourceBy() {
		return sourceBy;
	}

	public void setSourceBy(String sourceBy) {
		this.sourceBy = sourceBy;
	}


	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	

	@Override
	public String toString() {
		return title;
	}
	
	
	

}

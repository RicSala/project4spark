package com.imprender.project4spark.model;

import com.github.slugify.Slugify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Entry {
	private String slug;
	private String tittle;
	private Date date;
	private String body;
	private List<String> tags;
	private List<Comment> comments;

	public Entry(String tittle, Date date, String body, List<String> tags) {
		this.tittle = tittle;
		Slugify slugify = new Slugify();
		slug = slugify.slugify(tittle);
		this.date = Calendar.getInstance().getTime();
		this.body = body;
		this.tags = tags;
		comments = new ArrayList<>();
	}

	public String getTittle() {
		return tittle;
	}

	public String getSlug() {
		return slug;
	}

	public Date getDate() {
		return date;
	}

	public String getBody() {
		return body;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public List<String> getTags() {
		return tags;
	}

	public Entry update(Entry entry) {

		this.tittle = entry.tittle;
		this.slug = entry.slug;
		this.date = Calendar.getInstance().getTime();
		this.body = entry.body;
		this.tags = entry.tags;

		return this;
	}

//	public void addComment(Comment comment) {
//		comments.add(comment);
//	}

	//Todo: add equals and hashcode
}

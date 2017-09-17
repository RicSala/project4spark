package com.imprender.project4spark.model;

import com.github.slugify.Slugify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Entry {
	private String slug;
	private String title;
	private Date date;
	private String body;
	private List<String> tags;
	private List<Comment> comments;

	public Entry(String title, String body, List<String> tags) {
		this.title = title;
		Slugify slugify = new Slugify();
		slug = slugify.slugify(title);
		this.date = Calendar.getInstance().getTime();
		this.body = body;
		this.tags = tags;
		comments = new ArrayList<>();
	}

	public Entry(String title, Date date, String body, List<String> tags) {
		this.title = title;
		Slugify slugify = new Slugify();
		slug = slugify.slugify(title);
		this.date = date;
		this.body = body;
		this.tags = tags;
		comments = new ArrayList<>();
	}

	public String getTitle() {
		return title;
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

		this.title = entry.title;
		this.slug = entry.slug;
		this.date = Calendar.getInstance().getTime();
		this.body = entry.body;
		this.tags = entry.tags;

		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entry entry = (Entry) o;

		if (getSlug() != null ? !getSlug().equals(entry.getSlug()) : entry.getSlug() != null) return false;
		if (getTitle() != null ? !getTitle().equals(entry.getTitle()) : entry.getTitle() != null) return false;
		if (getDate() != null ? !getDate().equals(entry.getDate()) : entry.getDate() != null) return false;
		if (getBody() != null ? !getBody().equals(entry.getBody()) : entry.getBody() != null) return false;
		if (getTags() != null ? !getTags().equals(entry.getTags()) : entry.getTags() != null) return false;
		return getComments() != null ? getComments().equals(entry.getComments()) : entry.getComments() == null;
	}

	@Override
	public int hashCode() {
		int result = getSlug() != null ? getSlug().hashCode() : 0;
		result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
		result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
		result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
		result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
		result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
		return result;
	}
}

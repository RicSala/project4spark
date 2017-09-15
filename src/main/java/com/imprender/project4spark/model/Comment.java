package com.imprender.project4spark.model;

import java.util.Calendar;
import java.util.Date;

public class Comment {
	private String username;
	private String body;
	private Date date;

	public Comment(String username, String commentText) {
		this.username = username;
		this.body = commentText;
		this.date = Calendar.getInstance().getTime();
	}

	public String getUsername() {
		return username;
	}

	public String getBody() {
		return body;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Comment comment = (Comment) o;

		if (getUsername() != null ? !getUsername().equals(comment.getUsername()) : comment.getUsername() != null)
			return false;
		if (getBody() != null ? !getBody().equals(comment.getBody()) : comment.getBody() != null) return false;
		return date != null ? date.equals(comment.date) : comment.date == null;
	}

	@Override
	public int hashCode() {
		int result = getUsername() != null ? getUsername().hashCode() : 0;
		result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		return result;
	}
}

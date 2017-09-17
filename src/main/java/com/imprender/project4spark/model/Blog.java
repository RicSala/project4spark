package com.imprender.project4spark.model;

import com.imprender.project4spark.DAO.BlogEntriesDAOImp;

import java.util.List;

public class Blog {
	private BlogEntriesDAOImp dao;
	private String password;

	public Blog() {
		dao = new BlogEntriesDAOImp();
		password = "Ric2013";
	}

	public List<Entry> getEntries() {
		return dao.findAll();
	}

	public boolean remove(Entry entry) {
		return dao.remove(entry);
	}

	public boolean add(Entry entry) {
		return dao.add(entry);
	}

	public Entry findBySlug(String slug) {
		return dao.findBySlug(slug);
	}

	public boolean addCommentToEntry(Entry entry, Comment comment) {
		return dao.addComment(entry, comment);
	}

	public String getPassword() {
		return password;
	}


}

package com.imprender.project4spark.model;

import com.imprender.project4spark.DAO.BlogEntriesDAOImp;

import java.util.List;

public class Blog {
	private List<Entry> entries;
	private BlogEntriesDAOImp dao;
	private String password;

	public Blog() {
		dao = new BlogEntriesDAOImp();
		this.entries = dao.findAll();
		password = "Ric2013";
//		Todo: add conditions to password
	}

	public List<Entry> getEntries() {
		return entries;
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
		//Todo: this method should be in Entry class, but there I can access the dao...how could I do that?
		// Should I create another DAO? but they are related! That would create two copies of the same comments!
		return dao.addComment(entry, comment);
	}

	public boolean correctPassword(String password) {
		return password == this.password;
	}

	public String getPassword() {
		return password;
	}


}

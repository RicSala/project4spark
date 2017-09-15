package com.imprender.project4spark.DAO;

import com.imprender.project4spark.model.Comment;
import com.imprender.project4spark.model.Entry;
import com.imprender.project4spark.model.NotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BlogEntriesDAOImp implements BlogEntriesDAOInt {

	private List<Entry> entries;

	public BlogEntriesDAOImp() {

		List<String> tags = new ArrayList<>();
		tags.add("@badDays");
		tags.add("@arg");
		entries = new ArrayList<>();
		entries.add(new Entry("The absolute worst day I’ve ever had", Calendar.getInstance().getTime(), "I had a veeeery bad day!", tags));
		entries.add(new Entry("Lets make this a bit prettier adding other entries", Calendar.getInstance().getTime(), "I don't have a very creative day...!", tags));
		entries.add(new Entry("But today is thursday so we are partying!", Calendar.getInstance().getTime(), "At 9pm in the pub!", tags));
		entries.add(new Entry("And this is not gonna appear on my index page", Calendar.getInstance().getTime(), "So I can say whatever I want", tags));
		this.entries = entries;
	}

	@Override
	public List<Entry> findAll() {
		return new ArrayList<>(entries);
		//Todo: be carefull, this is a whole new List!
	}

	@Override
	public boolean add(Entry entry) {
		return entries.add(entry);
	}

	@Override
	public boolean remove(Entry entry) {
		return entries.remove(entry);
	}

	@Override
	public Entry findBySlug(String slug) {
		return entries.stream()
				.filter(entry -> entry.getSlug().equals(slug))
				.findFirst()
				.orElseThrow(NotFoundException::new);

		//Todo: Learn streams
	}

	public boolean addComment(Entry entry, Comment comment) {
		return entry.getComments().add(comment);
	}

}

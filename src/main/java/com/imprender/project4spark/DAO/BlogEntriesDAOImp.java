package com.imprender.project4spark.DAO;

import com.imprender.project4spark.model.Comment;
import com.imprender.project4spark.model.Entry;
import com.imprender.project4spark.model.NotFoundException;

import java.util.*;

public class BlogEntriesDAOImp implements BlogEntriesDAOInt {

	private List<Entry> entries;

	public BlogEntriesDAOImp() {

		List<String> tags = new ArrayList<>();
		tags.add("@badDays");
		tags.add("@arg");
		entries = new ArrayList<>();



		Calendar calendar = new Calendar.Builder().setDate(2017, 3, 11).setTimeOfDay(11, 11, 11).build();
		entries.add(new Entry("The absolute worst day I’ve ever had", calendar.getTime(), "I had a veeeery bad day!", tags));
		calendar.set(2, 5);
		entries.add(new Entry("Lets make this a bit prettier adding other entries", calendar.getTime(), "I don't have a very creative day...!", tags));
		calendar.set(2, 6);
		entries.add(new Entry("But today is thursday so we are partying!", calendar.getTime(), "At 9pm in the pub!", tags));
		calendar.set(2, 7);
		entries.add(new Entry("And this is not gonna appear on my index page", calendar.getTime(), "So I can say whatever I want", tags));
	}

	@Override
	public List<Entry> findAll() {
		return entries;
	}

	@Override
	public boolean add(Entry entry) {
		entries.add(0, entry);
		return true;
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

	@Override
	public void sort() {
		entries.sort(new Comparator<Entry>() {
			@Override
			public int compare(Entry entry, Entry anotherEntry) {
				return entry.getDate().compareTo(anotherEntry.getDate());
			}
		});
	}

	public boolean addComment(Entry entry, Comment comment) {
		return entry.getComments().add(comment);
	}

}

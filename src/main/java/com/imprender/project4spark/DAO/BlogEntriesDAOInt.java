package com.imprender.project4spark.DAO;

import com.imprender.project4spark.model.*;

import java.util.List;

public interface BlogEntriesDAOInt {

	List<Entry> findAll();

	boolean add(Entry entry);

	boolean remove(Entry entry);

	Entry findBySlug(String slug);
}

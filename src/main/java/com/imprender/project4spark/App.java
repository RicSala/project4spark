package com.imprender.project4spark;


import com.imprender.project4spark.model.Blog;
import com.imprender.project4spark.model.Comment;
import com.imprender.project4spark.model.Entry;
import com.imprender.project4spark.model.NotFoundException;
import com.imprender.project4spark.service.EntryUtils;
import spark.ModelAndView;
import spark.Request;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class App {

	public static void main(String[] args) {

		staticFileLocation("/public");
		Blog blog = new Blog();

		//FILTERs//

		//Check if there is a cookie "password" and in that case, put it in a session cookie
		before((request, response) -> {
			if (request.cookie("password") != null) {
				request.session().attribute("password", request.cookie("password"));
			}
		});

		//If the user is trying to edit an entry, check if has password (and ask for it if it is not the case)
		before("/entries/:slug/edit", (request, response) -> {
			String password = request.session().attribute("password");
			if (password == null || !password.equals(blog.getPassword())) {
				request.session().attribute("destinyUrl", "/entries/" + request.params("slug") + "/edit");
				response.redirect("/log-in");
				halt();
			}
		});

		//If the user is trying to create a new entry, check password
		before("/new", (request, response) -> {
			String password = request.session().attribute("password");
			if (password == null || !password.equals(blog.getPassword())) {
				response.redirect("/log-in");
				halt();
			}
		});



		//HOMEPAGE
		get("/", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("flashm", captureFlashMessage(request));
			model.put("entries", blog.getEntries());
			return new ModelAndView(model, "index");
		}, new ThymeleafTemplateEngine());

		get("/index.html", (request, response) -> {
			response.redirect("/");
			return null;
		});



		//VIEW ENTRY AND ADD COMMENT
		get("entries/:slug", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			String message = captureFlashMessage(request);
			model.put("flashm", message);
			model.put("entry", blog.findBySlug(request.params("slug")));
			return new ModelAndView(model, "detail");
		}, new ThymeleafTemplateEngine());


		post("entries/:slug/new-comment", (request, response) -> {
			String name = request.queryParams("name");
			String body = request.queryParams("comment");
			Comment comment = new Comment(name, body);
			Entry entry = blog.findBySlug(request.params("slug"));
			boolean added = blog.addCommentToEntry(entry, comment);

			if (added) {
				setFlashMessage(request, "Your comment was published! :)");
			}

			response.redirect("/entries/" + request.params("slug"));
			return null;
		});



		//NEW ENTRY
		get("/new", (request, response) -> {
			Map<String, Object> model = new HashMap<>(); //Todo: how can I avoid creating a map?
			return new ModelAndView(model, "new");
		}, new ThymeleafTemplateEngine());

		post("/new", (request, response) -> {
			String title = request.queryParams("title");
			String text = request.queryParams("entry");
			String tagspre = request.queryParams("tags");
			List<String> tags = Arrays.asList(tagspre.split(","));
			Entry entry = new Entry(title, Calendar.getInstance().getTime(), text, tags);
			blog.add(entry);
			setFlashMessage(request, "Your entry was published! :)");
			response.redirect("/entries/" + entry.getSlug());
			return null;
		});



		//DELETE
		get("/entries/:slug/delete", (request, response) -> {
			Entry entry = blog.findBySlug(request.params("slug"));
			blog.remove(entry);
			setFlashMessage(request, "Entry deleted!");
			response.redirect("/");
			return null;
		});



		//EDIT ENTRY
		get("/entries/:slug/edit", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("entry", blog.findBySlug(request.params("slug")));
			return new ModelAndView(model, "edit");
		}, new ThymeleafTemplateEngine());

		post("/entries/:slug/edit", (request, response) -> {
			String oldSlug = request.params("slug");
			String title = request.queryParams("title");
			String text = request.queryParams("entry");
			List<String> tags = EntryUtils.getTAgs(request.queryParams("newTags"));
			Entry entry = new Entry(title, Calendar.getInstance().getTime(), text, tags);
			blog.findBySlug(oldSlug).update(entry);
			setFlashMessage(request, "Your entry was edited!");
			response.redirect("/entries/" + entry.getSlug());
			return null;
		});



		//LOG-IN (PASSWORD FILTER)
		get("/log-in", (request, response) -> {
			Map<String, String> model = new HashMap<>();
			String destiny = request.session().attribute("destinyUrl");
			model.put("destinyUrl", destiny);
			return new ModelAndView(model, "password");
		}, new ThymeleafTemplateEngine());

		post("/log-in", (request, response) -> {
			response.cookie("password", request.queryParams("password"));
			String destiny = request.session().attribute("destinyUrl");
			response.redirect(destiny);
			return null;
		});


		//EXCEPTIONs
		exception(NotFoundException.class, (exception, request, response) -> {
			response.status(404);

			ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
			Map<String, String> model = new HashMap<>();
			String html = engine.render(new ModelAndView(model, "not-found"));
			response.body(html);
		});

	}


	//FLASH MESSAGE
	private static String getFlashMessageKey(Request request) {
		if (request.session(false) == null) {
			return null;
		}
		if (!request.session().attributes().contains("flash-message")) {
			return null;
		}
		return (String) request.session().attribute("flash-message");
	}

	private static String captureFlashMessage(Request request) {
		String message = getFlashMessageKey(request);
		if (message != null) {
			request.session().removeAttribute("flash-message");
		}
		return message;
	}

	private static void setFlashMessage(Request request, String message) {
		request.session().attribute("flash-message", message);
	}
}

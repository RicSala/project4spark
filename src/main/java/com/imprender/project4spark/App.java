package com.imprender.project4spark;


import com.imprender.project4spark.model.Blog;
import com.imprender.project4spark.model.Comment;
import com.imprender.project4spark.model.Entry;
import com.imprender.project4spark.model.NotFoundException;
import spark.ModelAndView;
import spark.Request;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

	public static void main(String[] args) {

		staticFileLocation("/public");
		Blog blog = new Blog();

		before((request, response) -> {
			if (request.cookie("password") != null) {
				request.attribute("password", request.cookie("password"));
			}
		});


		before("/entries/:slug/edit", (request, response) -> {
			String password = request.attribute("password");
			if (password == null || !password.equals(blog.getPassword())) {
				String slug = request.params("slug");
				response.redirect("/entries/" + slug + "/log-in");
				halt();
//				Todo: Ask what halt does
//				Todo: It asks again for the password...seems that request.attribute is not working!
			}
		});

		get("/", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("entries", blog.getEntries());
			return new ModelAndView(model, "index");
		}, new ThymeleafTemplateEngine());

		get("/index.html", (request, response) -> {
			response.redirect("/");
			return null;
		});


		get("/new.html", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("nothing", "nothing");
			return new ModelAndView(model, "new");
		}, new ThymeleafTemplateEngine());

		get("entries/:slug", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("entry", blog.findBySlug(request.params("slug")));
			return new ModelAndView(model, "detail");
		}, new ThymeleafTemplateEngine());

		get("/entries/:slug/edit", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("entry", blog.findBySlug(request.params("slug")));
			return new ModelAndView(model, "edit");
		}, new ThymeleafTemplateEngine());

		post("/entries/:slug/edit", (request, response) -> {
			String oldSlug = request.params("slug");
			String title = request.queryParams("title");
			String text = request.queryParams("entry");
			Entry entry = new Entry(title, Calendar.getInstance().getTime(), text, new ArrayList<>());
			entry = blog.findBySlug(oldSlug).update(entry);
			Map<String, Object> model = new HashMap<>();
			model.put("entry", entry);
			return new ModelAndView(model, "detail");
		}, new ThymeleafTemplateEngine());


		post("entries/:slug/new-comment", (request, response) -> {
			String name = request.queryParams("name");
			String body = request.queryParams("comment");
			Comment comment = new Comment(name, body);
			Entry entry = blog.findBySlug(request.params("slug"));
			boolean added = blog.addCommentToEntry(entry, comment);

//			if (added) {
//				setFlashMessage(request, "Your comment was published! :)");
//			}
//
			response.redirect("/entries/" + request.params("slug"));
			return null;
		});

		get("/entries/:slug/log-in", (request, response) -> {
//			Todo: como evito crear tantas urls iguales? esto para SEO es malo!
			String slug = request.params("slug");
			Map<String, Object> model = new HashMap<>();
			model.put("entry", blog.findBySlug(slug));
			return new ModelAndView(model, "password");
		}, new ThymeleafTemplateEngine());

		post("/entries/:slug/log-in", (request, response) -> {
			String slug = request.params("slug");
//			Todo: como evito crear tantas urls iguales? esto para SEO es malo!
			response.cookie("password", request.queryParams("password"));
			Map<String, Object> model = new HashMap<>();
			model.put("entry", blog.findBySlug(slug));
			response.redirect("/entries/" + request.params("slug") + "/edit");
			return null;
		});




		exception(NotFoundException.class, (exception, request, response) -> {
			response.status(404);

			ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
			Map<String, String> model = new HashMap<>();
			model.put("nothing", "nothing");
//			Todo: why can't I pass null like in hanblebars?
			String html = engine.render(new ModelAndView(model, "not-found"));
			response.body(html);
			//If you want more specific messages, we need more specific exceptions!
			//Todo: Aren't cookies visible for all the urls? why params better than cookies? If the password is now in the param, why do I lost it if I clean the cookie??

		});

	}

	private static void setFlashMessage(Request request, String message) {
		request.session().attribute("flash_message", message);
	}
}

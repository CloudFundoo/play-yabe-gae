package controllers;

import models.Post;
import models.Tag;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import java.util.*;

@With(Secure.class)
public class Admin extends Controller {
	@Before
	static void setConnecteduser() {
		if(Security.isConnected()) {
			User user = User.all().filter("email", Security.connected()).get();
			renderArgs.put("user", user.fullname);
		}
	}	
	
	public static void index() {
		String user = Security.connected();
		User loginuser = User.all().filter("email", user).get();
		List <Post> posts = Post.all().filter("author", loginuser).fetch();		
		render(posts);
	}
	
	public static void form(Long id) {
		if(id != null) {
	        Post post = Post.findById(id);
	        render(post);
	    }
	    render();		
	}
	
	public static void save(Long id, String title, String content, String tags) {
		Post post;
		
		if(id == null) {
			User author = User.all().filter("email", Security.connected()).get();
			post = new Post(title, author, content);
		}
		else {
			post = Post.findById(id);
			post.title = title;
			post.content = content;
			post.tags.asList().clear();
		}
		
		for(String tag : tags.split("\\s+")) {
	        if(tag.trim().length() > 0) {
	            post.tags.asList().add(Tag.findOrCreateByName(tag));
	        }
	    }
	    // Validate
	    validation.valid(post);
	    if(validation.hasErrors()) {
	        render("@form", post);
	    }
	    // Save
	    post.save();
	    index();		
	}

}

package controllers;

import java.util.List;

import models.Post;
import models.User;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.modules.siena.SienaFixtures;
import play.mvc.Before;
import play.mvc.Controller;
import lib.jobs.BootStrap;
import siena.Query;

public class Application extends Controller {

    public static void index() {
    	//Post frontPost = Post.find("order by postedAt desc").first();
    	Query<Post> postquery = Post.all();
    	Post frontPost = postquery.order("-postedAt").get();
    	//List<Post> olderPosts = Post.find("order By postedAt desc").from(1).fetch(10);
    	List<Post> olderPosts = postquery.order("-postedAt").offset(1).fetch(10);
    	
        render(frontPost, olderPosts);
    }
    @Before
	public static void loadYaml() {	
    	if(User.all().count() <= 0) {  
    		new BootStrap().doJob();			
    	}
	}
    
    @Before
    public static void addDefaults() {
    	renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
    	renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    
    public static void Show(Long id) {    	
    	Post post = Post.findById(id);
    	/*
    	Post post = Post.all().filter("id", id).get();*/
    	String randomID = Codec.UUID(); 
    	render(post, randomID);
    }
    
    public static void postComment (Long postId, 
    		@Required(message="Author is required") String author,
    		@Required(message="A message is required") String content,
    		@Required(message="Please type the code") String code,
    		String randomID) {
		Post post = Post.findById(postId);
		validation.equals(code, Cache.get(randomID)).message("Invalid code; Please type again");
		if(validation.hasErrors()) 
		{
			render("Application/Show.html", post, randomID);
		}
		post.addComment(author, content);
		flash.success("Thanks for posting %s", author);
		Cache.delete(randomID);
		Show(postId);
	}
    
    public static void captcha(String id) {
    	Images.Captcha captcha = Images.captcha();
    	String code = captcha.getText("#E4EAFD");    	
    	Cache.set(id, code, "10mn");
    	renderBinary(captcha);
    }
    public static void listTagged(String tag) {
    	List<Post> posts = Post.findTaggedWith(tag);
    	render(tag, posts);
    }
}
package models;

import java.util.*;

import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;
import play.*;


public class Comment extends Model {
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Required
	public String author;
	
	
	@DateTime
	public Date postedAt;
	
	@Text
	@Required		
	public String content;
	
	
	@Required
	public Post post;
	
	public static Query<Comment> all() {
		return Model.all(Comment.class);
	}
	 
	public Comment(Post post, String author, String content) {
		this.post = post;
		this.author = author;
		this.content = content;
		this.postedAt = new Date();
	}
	
	public String toString() {
		return "Comment_on_" + this.post.title;
	}
	public static int count() {
		return all().fetch().size();
	}
	public Long getId() {
		return id;
	}
}

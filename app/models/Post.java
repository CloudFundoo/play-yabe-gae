package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.*;

import play.*;
import siena.*;

import play.data.validation.Required;
import siena.DateTime;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.Query;
import siena.Table;
import siena.core.Aggregated;
import siena.core.Many;
import siena.core.Owned;
import siena.Text;

public class Post extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Required
	public String title;
 	
	@Required
	@DateTime
	public Date postedAt;
	
	@Text
	@Required
	public String content;
	
	@Required
	@Index("author_index")
	public User author;
	
	@Owned(mappedBy="post")
	public Many<Comment> comments;                                

	@Aggregated
	public Many<Tag> tags;
	
 	public Post(String title, User author, String content) {
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();		
	}
 	
	public Post() {}
	
	public Post addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content);
		this.comments.asList().add(newComment);		
		this.save();
		this.update();		
		return this;
	}
	
	public static Query<Post> all() {
		return Model.all(Post.class);
	}
	public Post previous () {
		return all().order("-postedAt").filter("postedAt<", postedAt).get();
	}
	
	public Post next() {
		
		return all().order("postedAt").filter("postedAt>", postedAt).get();
	}	
	
	public Post tagItWith(String name) {
		tags.asList().add(Tag.findOrCreateByName(name));
		this.save();	
		this.update();
		return this;
	}
	
	public static List<Post> findTaggedWith(String tag) {
		List <Post> posts = all().fetch();
		List <Post> result = new ArrayList();
		
	    Iterator<Post> postIterator = posts.iterator();
	    
	    while(postIterator.hasNext())
	    {
	    	if(postIterator.next().tags.asQuery().fetch().size()>= 1){
	    	if((postIterator.next().tags.asQuery().fetch().size()) >= 1)
	    		result.add(postIterator.next());	    	
	    	}
	    }
	    return result;
	}
	
	public String toString() {
		return this.title;
	}
	
	public static Post findById(Long id) {
		return all().filter("id", id).get();		
	}
	
	public static int count() {
		return all().fetch().size();
	}
	
	public List<Comment> findComments(){
		return this.comments.asList();
	}
	public List<Tag> findTags() {		
		return this.tags.asList();
	}
	public Long getId() {
		return id;
	}
}
	

import java.util.List;

import models.Comment;
import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.modules.siena.SienaFixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {
	@Before
	public void setup() {
		SienaFixtures.deleteAllModels();
	}	
	
    @Test
    public void createAndRetrieveUser() {
    	new User("nobin.mathew@gmail.com", "asdfgh", "Nobin" ).save();
    	
    	User bob = User.all().filter("email", "nobin.mathew@gmail.com").get();
    	
    	assertNotNull(bob);
    	assertEquals("Nobin", bob.fullname);
    }
    
    @Test
    public void tryConnectAsUser (){
    	new User("nobin.mathew@gmail.com", "asdfgh", "Nobin").save();
    	assertNotNull(User.connect("nobin.mathew@gmail.com", "asdfgh"));
    	assertNull(User.connect("nobin.mathew@gmail.com", "abc"));
    	assertNull(User.connect("abc@gmail.com", "zxcvbn"));
    }
    
    
    @Test
    public void testPost () {
    	User bob = new User("nobin.mathew@gmail.com", "asdfgh", "Nobin");
    	bob.insert();
    	
    	new Post("First Post", bob, "Hello World!").save();
    	assertEquals(1, Post.count());
    	List<Post> bobsPosts = Post.all().filter("author", bob).fetch();
    	
    	assertEquals(1, bobsPosts.size());
    	Post firstPost = bobsPosts.get(0);
    	assertNotNull(firstPost);
    	assertEquals(bob, firstPost.author);
    	assertEquals("First Post", firstPost.title);
    	assertEquals("Hello World!", firstPost.content);
    	assertNotNull(firstPost.postedAt);
    
    }
    
    
    @Test
    public void postComments() {
    	User bob = new User("nobin.mathew@gmail.com", "asdfgh", "Nobin");
    	bob.insert();
    	
    	Post bobPost = new Post("First Post", bob, "Hello World!");
    	bobPost.insert();
    	
    	new Comment(bobPost, "mary", "Nice Post").save();
    	new Comment(bobPost, "Nobin", "I knew that !").save();
    	List<Comment> bobpostComments = Comment.all().filter("post", bobPost).fetch();
    	
    	assertEquals(2, bobpostComments.size());
    }
    
    
    @Test
    public void useTheCommentsRelation() {
    	User bob = new User("nobin.mathew@gmail.com", "asdfgh", "Nobin");
    	bob.insert();
    	
    	Post bobPost = new Post("First Post", bob, "Hello World!");
    	bobPost.insert();
    	
    	bobPost.addComment("mary", "Nice Post!");
    	bobPost.addComment("rachel", "You copied Dad!");
    	
    	assertEquals(1, User.count());
    	assertEquals(1, Post.count());
    	assertEquals(2, bobPost.comments.asList().size());
    	Post bobPost1 = Post.all().filter("author", bob).get();
    	assertNotNull(bobPost1);
    	//bobPost1 fails; because bobPost is the not the first post
    	//previous entries are not deleted from table.
    	assertEquals(2, bobPost.comments.asList().size());
    	assertEquals("mary", bobPost.comments.asList().get(0).author);
    	bobPost.delete();
    	
    	assertEquals(1, User.count());
    	assertEquals(0, Post.count());
    	assertEquals(0, Comment.count());    	   	
    }
    
    
    @Test
    public void fullTest() {
    	SienaFixtures.loadModels("data.yml");
    	
    	assertEquals(2, User.count());
    }
    
    
    @Test
    public void testTags() {
    	User bob = new User("nobin.mathew@gmail.com", "asdfgh", "Nobin");
    	bob.insert();
    	
    	Post bobPost = new Post("My First Post", bob, "Hello World! First Time");
    	//bobPost.insert();
    	Post AnotherbobPost = new Post("My Second Post", bob, "Hello World! Second Time");
    	AnotherbobPost.insert();
    	
    	assertEquals(0, Post.findTaggedWith("Red").size());
    	//bobPost.tagItWith("Red").tagItWith("Blue");  
    	bobPost.tagItWith("Red");
    	bobPost.insert();
    	bobPost.tagItWith("Blue");
    	bobPost.insert();
    	AnotherbobPost.tagItWith("Red").tagItWith("Green");
    	AnotherbobPost.save();
    	AnotherbobPost.update();
    	
    	assertEquals(2, Post.findTaggedWith("Red").size());
    	assertEquals(1, Post.findTaggedWith("Green").size());
    	assertEquals(1, Post.findTaggedWith("Blue").size());    	
    }    
}

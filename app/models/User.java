package models;

import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;


public class User extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Email
	@Required
	public String email;
	
	@Required
	public String password;
	
	public String fullname;
	
	public boolean isAdmin;
	
	public User(String email, String password, String fullname) {
		this.email = email;
		this.fullname = fullname;
		this.password = password;
	}
	
	public static Query<User> all() {
		return Model.all(User.class);
	}
	
	/*
	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}*/
	
	public static User connect(String email, String password) {
		return all().filter("email", email).filter("password", password).get();
	}
	
	public String toString() {		
		return this.fullname;
	}
	
	public static int count() {
		return all().fetch().size();
	}
	
	public static User findById(Long id) {
		return all().filter("id", id).get();
	}	
	public Long getId() {
		return id;
	}
}

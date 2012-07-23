package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.data.validation.Required;
import siena.*;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;


public class Tag extends Model implements Comparable<Tag> {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Required
	public String name;
	
	private Tag(String name) {
		this.name = name;
	}
	
	public String toString() {		
		return name;
	}
	
	public int compareTo(Tag otherTag) {
		return name.compareTo(otherTag.name);		
	}
	
	public static Query<Tag> all() {
		return Model.all(Tag.class);
	}
	
	public static Tag findOrCreateByName(String name) {
		Tag tag = all().filter("name", name).get();
		if(tag == null) {
			tag = new Tag(name);
		}
		return tag;
	}
	
	public static List<Map> getCloud() {
		List<Map> result = new ArrayList(all().fetch());
		return result;
	}
	public static int count() {
		return all().fetch().size();
	}
	public Long getId() {
		return id;
	}
	
	public static List<Tag> findAll() {
		return all().fetch();
	}
}

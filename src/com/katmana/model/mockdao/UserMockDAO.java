package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.User;

public class UserMockDAO extends BaseMockDAO<User> implements User.DAO {

	public UserMockDAO() {
		/* set the data */
		ArrayList<User> data = new ArrayList<User>();
		data.add(new User(1L,"Some name1","someone1@someplace.com","idoneknowwhatthisis1"));
		data.add(new User(2L,"Some name2","someone2@someplace.com","idoneknowwhatthisis2"));
		data.add(new User(3L,"Some name3","someone3@someplace.com","idoneknowwhatthisis3"));
		data.add(new User(4L,"Some name4","someone4@someplace.com","idoneknowwhatthisis4"));
		data.add(new User(5L,"Some name5","someone5@someplace.com","idoneknowwhatthisis5"));
		this.data = data;
	}

	@Override
	public User getByEmail(String email) {
		for(User cur:data){
			if(cur.getEmail().equals(email)){
				return cur;
			}
		}
		return null;
	}

	@Override
	public User getByName(String name) {
		for(User cur:data){
			if(cur.getName().equals(name)){
				return cur;
			}
		}
		return null;
	}

}

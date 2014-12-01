package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.User;

public class UserMockDAO extends BaseMockDAO<User> implements User.DAO {

	public UserMockDAO() {
		/* set the data */
		ArrayList<User> data = new ArrayList<User>();
		data.add(new User(1L,"Some name1","someone1@someplace.com","1f3a386d5aca7415d4b925bb2bb4af56fda488536c275808ff2b6cc93fbe10b5"));
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

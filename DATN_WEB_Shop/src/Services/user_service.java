package Services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import CRUD.user_CRUD;
import Entity.user;
import Entity.user_full;
import exception.user_erro;

@Component
public class user_service implements user_Inter {
	private List<String> s = Arrays.asList("admin","phamhuungan");
	@Autowired
	private user_CRUD c;
	@Override
	public boolean login(user u)
	{
		return c.login(u);
	}
	@Override
	public boolean isAdmin(user u)
	{
		return s.contains(u.getUsername());
	}
	public boolean res(user_full u)
	{
		if(c.register(u))
			return true;
		return false;
	}
	public user_full get_full_info_user(user u)
	{
		user_full z= c.get_full_info_user(u);
		if(z==null)
		{
			throw new user_erro("ban chua dang nhap , hoac dang nhap het han!!!");
		}
			else
				return z;
	}
	public user_full get_full_info_user(int id)
	{
		
		user_full z= c.get_full_info_user(id);
		if(z==null)
		{
			throw new user_erro("!!!");
		}
			else
				return z;
	}
	public boolean update_user(user_full u)
	{
		return c.update_info(u);
	}
}

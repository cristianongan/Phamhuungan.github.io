package com.phamhuungan.thymeleafPractices.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Entities.UserInfo;
import com.phamhuungan.thymeleafPractices.Entities.Repository.PasUserRepository;
import com.phamhuungan.thymeleafPractices.Entities.Repository.UserRepository;




@Service
public class UserServices {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasUserRepository pasUserRepository;
	public boolean authentication(User user)
	{
		User userDB = userRepository.findByUserName(user.getUserName());
		if(userDB!=null)
		if(BCrypt.checkpw(user.getPassword(), userDB.getPassword()) )
			{
				user.setGrantAdmin(userDB.isGrantAdmin());
				user.setGrantEmployee(userDB.isGrantEmployee());
				user.setGrantStandar(userDB.isGrantStandar());
				return true;
			}
		return false;
	}
	public boolean register(User user)
	{
		user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(12)));
		if(userRepository.save(user)!=null)
			return true;
		return false;
	}
	public Page<User> findPaginated(Optional<Integer> page)
	{
		int currentPage=page.orElse(1);
		Page<User> pageContent = pasUserRepository.findAll(PageRequest.of(currentPage-1, 10));
		return pageContent;
	}
	public Page<User> findbyAll(Optional<Integer> page,String s)
	{
		if(s==null)
			s="";
		int currentPage=page.orElse(1);
		Page<User> pageContent= pasUserRepository.findByUserNameContainingOrUserInfo_FirstNameContainingOrUserInfo_LastNameContainingOrUserInfo_EmailContaining(PageRequest.of(currentPage-1, 10),s,s,s,s);
		return pageContent;
	}

}

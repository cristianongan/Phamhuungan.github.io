package com.phamhuungan.thymeleafPractices.Services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.services.drive.Drive;
import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Entities.UserInfo;
import com.phamhuungan.thymeleafPractices.Entities.Repository.PasUserRepository;
import com.phamhuungan.thymeleafPractices.Entities.Repository.UserRepository;




@Service
public class UserServices {
	@Autowired
	private Drive GGdrive;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasUserRepository pasUserRepository;
	public boolean authentication(User user)
	{
		User userDB = userRepository.findByUserName(user.getUserName());
		if(userDB!=null)
		if(BCrypt.checkpw(user.getPassword(), userDB.getPassword()))
			{
			user.setPassword(userDB.getPassword());
				user.setId(userDB.getId());
				user.setGrantAdmin(userDB.isGrantAdmin());
				user.setGrantEmployee(userDB.isGrantEmployee());
				user.setGrantStandar(userDB.isGrantStandar());
				return true;
			}
		return false;
	}
	public boolean register(User user,CommonsMultipartFile fileImg)
	{
		if(fileImg!=null)
		{
			String imgUrl = addImg(fileImg);
				if(imgUrl!=null)
					user.getUserInfo().setImgUrl(imgUrl);
		}
		user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(12)));
		if(userRepository.save(user)!=null)
			return true;
		return false;
	}
	public Page<User> findPaginated(Optional<Integer> page)
	{
		int currentPage=page.orElse(1);
		List<User> listUser = pasUserRepository.findPage(10, 10*(currentPage-1));
		long totalRows = pasUserRepository.count();
		long totalPage = totalRows/10+totalRows%10;
		Page<User> pageContent = new PageImpl<User>(listUser, PageRequest.of(currentPage-1, 10), totalPage);
		return pageContent;
	}
	public Page<User> findbyAll(Optional<Integer> page,String s)
	{
		if(s==null)
			s="";
		String pattern = "^\".*\"$";
		System.out.println("s:"+ pattern);
		
		int currentPage=page.orElse(1);

		Page<User> pageContent; 
		if(Pattern.compile(pattern).matcher(s).matches())
		{
			String a =s.replace("\"", "");
			System.out.println(a);
			long totalRows = pasUserRepository.rowsSearch(s); //?
			long totalPage = totalRows/10+totalRows%10;
			List<User> listUser = pasUserRepository.searchFulltext(a);//?
			pageContent =new PageImpl<User>(listUser,PageRequest.of(currentPage-1, 10),totalPage);//?
		}else
		{
			s="%"+s+"%";
			long totalRows = pasUserRepository.rowsSearch(s); 
			long totalPage = totalRows/10+totalRows%10;
			List<User> listUser = pasUserRepository.searchByAll(10,10*(currentPage-1),s);
			pageContent =new PageImpl<User>(listUser,PageRequest.of(currentPage-1, 10),totalPage);
		}
		return pageContent;
	}
	public User findUser(int id)
	{
		if(id>0)
		{
			return userRepository.findById(id).get();
		}
		return null;
	}
	public boolean updateUser(User user,CommonsMultipartFile fileImg)
	{
		if(fileImg!=null)
		{
			String imgUrl = addImg(fileImg);
				if(imgUrl!=null)
					user.getUserInfo().setImgUrl(imgUrl);
		}
			
		if(user.getId()!=0)
		{
			if(userRepository.save(user)!=null)
				return true;
		}
			
		return false;
	}
	@Transactional
	public boolean delete(int id)
	{
		if(id>0)
		{
			return userRepository.deleteByIdAndGrantAdmin(id, false)>0?true:false;
		}
		return true;
	}
	public String JWTBuild(User user)
	{
		String token = JWT.create()
				.withSubject(user.getUserName())
				.withExpiresAt(new Date(System.currentTimeMillis()+900000))
				.sign(Algorithm.HMAC512("nganpham".getBytes()));		
		return token;
	}
	public User reAu(String token)
	{
		System.out.println("token:"+token);
		try {
			String userName = JWT
					.require(Algorithm.HMAC512("nganpham".getBytes()))
					.build()
					.verify(token.replace("Bearer ", ""))
					.getSubject();
			User userDB = userRepository.findByUserName(userName);
			if(userDB.getUserName()!=null)
				return userDB;
			}catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	private String addImg(CommonsMultipartFile fileImg)
	{
//		String root ="/home/phngan/eclipse-workspace/DemoThymaleaf/src/main/resources/static/img_ngan_img";
//		String fileName = fileImg.getOriginalFilename();
//		File file =new File(root+File.separator+"_"+new Date().getTime()+"_"+fileName);
//		try {
//			fileImg.transferTo(file);
//			return file.getAbsolutePath();
//		} catch (IllegalStateException | IOException e) {
//			e.printStackTrace();
//		}
		com.google.api.services.drive.model.File GGFile = new com.google.api.services.drive.model.File();
		
		return null;
	}

}

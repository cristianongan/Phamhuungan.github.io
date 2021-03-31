package com.phamhuungan.thymeleafPractices.Entities.Repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.phamhuungan.thymeleafPractices.Entities.User;

public interface PasUserRepository extends PagingAndSortingRepository<User, Integer>{
	public Page<User> findByUserNameContainingOrUserInfo_FirstNameContainingOrUserInfo_LastNameContainingOrUserInfo_EmailContaining(Pageable pageable,String userName,String UserInfo_FirstName,String UserInfo_LastName,String UserInfo_Email);
	@Query(nativeQuery = true,value = "select * from user u left join userinfo ui on u.user_id = ui.user_id limit ?1 offset ?2")
	public List<User> findPage(int size,int start);
//	@Query("select count(*) from user u")
//	public int totalRows();
	@Query(value="select * from user u left join userinfo ui on u.user_id=ui.user_id where "
			+ "u.username like ?3 or ui.fistname like ?3 or ui.last_name like ?3 or ui.email like ?3 limit  ?1 offset ?2 ",
			nativeQuery = true)
	public List<User> searchByAll(int size,int start,String s);
	@Query(value="select count(*) from user u left join userinfo ui on u.user_id=ui.user_id where "
			+ "u.username like ?1 or ui.fistname like ?1 or ui.last_name like ?1 or ui.email like ?1",
			nativeQuery = true)
	public long rowsSearch(String s);
	@Query(value = "select * from user u left join userinfo ui on u.user_id=ui.user_id  where concat(concat(ui.fistname,' '),ui.last_name) = ?1", nativeQuery = true)
	public List<User> searchFulltext(String s);
}

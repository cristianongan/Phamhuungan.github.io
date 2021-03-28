package com.phamhuungan.thymeleafPractices.Entities.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Entities.UserInfo;

public interface PasUserRepository extends PagingAndSortingRepository<User, Integer>{
	public Page<User> findByUserNameContainingOrUserInfo_FirstNameContainingOrUserInfo_LastNameContainingOrUserInfo_EmailContaining(Pageable pageable,String userName,String UserInfo_FirstName,String UserInfo_LastName,String UserInfo_Email);

}

package com.phamhuungan.thymeleafPractices.Entities.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import com.phamhuungan.thymeleafPractices.Entities.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>{
	
	User findByUserName(String userName);
	


}

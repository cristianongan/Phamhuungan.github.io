package com.viettel.automl.repository;

import com.viettel.automl.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

	UsersEntity findByUsername(String username);

}

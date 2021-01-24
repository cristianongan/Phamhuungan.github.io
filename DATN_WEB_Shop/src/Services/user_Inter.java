package Services;

import org.springframework.stereotype.Service;

import Entity.user;

@Service
public interface user_Inter {

	boolean login(user u);

	boolean isAdmin(user u);

}

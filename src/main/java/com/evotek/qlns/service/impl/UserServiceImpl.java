/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

import com.evotek.qlns.dao.RightDAO;
import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.dao.UserDAO;
import com.evotek.qlns.dao.UsersLoginDAO;
import com.evotek.qlns.model.Language;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.UserLogin;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.EncryptUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.MailUtil;
import com.evotek.qlns.util.RandomStringGeneratorUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.PermissionConstants;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger _log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	private RightDAO rightDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UsersLoginDAO usersLoginDAO;

	@Override
	public void activateUser(List<User> users) {
		try {
			for (User user : users) {
				user.setStatus(Values.STATUS_ACTIVE);
			}

			this.userDAO.saveOrUpdateAll(users);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void addRole(Long userId, String roleName) {
		try {
			User user = this.userDAO.getUserById(userId);

			if (user != null) {
				addRole(user, roleName);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void addRole(User user, String roleName) {
		try {
			List<Role> roleUsers = this.roleDAO.getRoleByRN(roleName, null);

			if (Validator.isNotNull(roleUsers)) {
				user.getRoles().addAll(roleUsers);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void addVerifyResetPwd(User user) {
		if (Values.STATUS_DEACTIVE.equals(user.getStatus())) {
			MailUtil.sendVerifyResetPwd(user.getEmail(), user.getUserName(), user.getFullName());
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append(user.getUserId());
			sb.append(StringPool.COMMA);
			sb.append(Values.VERIFY_RESET_PWD);
			sb.append(StringPool.COMMA);
			sb.append(DateUtil.formatLongDate(DateUtil.getDateAfter(StaticUtil.VERIFY_RESET_PASSWORD_AVAIABLE_TIME)));

			// create verify
			String verifyCode = new EncryptUtil().encrypt(sb.toString());

			user.setVerificationCode(verifyCode);

			this.userDAO.saveOrUpdate(user);
			//

			MailUtil.sendVerifyResetPwd(user.getEmail(), user.getUserName(), user.getFullName(), verifyCode);
		}
	}

	@Override
	public void assignRoleToUser(User user, List<Role> roles, boolean isAdmin) throws Exception {
		for (Role role : roles) {
			if (isAdmin || role.getShareable()) {
				user.getRoles().add(role);
			}
		}

		this.userDAO.saveOrUpdate(user);
	}

	@Override
	public void createPassword(User user) {
		String randomPass = RandomStringGeneratorUtil.generate(16);

		if (randomPass.length() > 0) {
			user.setPassword(this.passwordEncoder.encode(randomPass));
		}

		this.saveOrUpdate(user);

		// send email to user create
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();

		String hostAddress = GetterUtil.getServerUrl(req);

		MailUtil.sendUserCreateEmail(user.getEmail(), user.getUserName(), randomPass, user.getFullName(), hostAddress);
	}

	@Override
	public void delete(List<Role> roles, User user) {
		try {
			user.getRoles().removeAll(roles);

			this.userDAO.saveOrUpdate(user);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public List<String> delete(List<User> users) throws Exception {
		List<String> userNotDel = new ArrayList<String>();

		for (User user : users) {
			if (Values.STATUS_NOT_READY.equals(user.getStatus())) {

				this.delete(user);
			} else {
				userNotDel.add(user.getUserName());
			}
		}

		return userNotDel;
	}

	@Override
	public void delete(User user) throws Exception {
		this.userDAO.delete(user);
	}

//    public void saveOrUpdate(Users user) {
//        usersDAO.saveOrUpdate(user);
//    }

	// LinhLH fix
	@Override
	public List<Language> getAllLanguages() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<User> getAllUsers() throws Exception {
		return this.userDAO.getAllUsers();
	}

	@Override
	public int getCountAllUsers() throws Exception {
		return this.userDAO.getCountAllUsers();
	}

//    public List<Role> getRolesByUser(User user) throws Exception {
//        return roleDAO.getRolesByUser(user);
//    }

	@Override
	public List<SimpleModel> getGenderType() {
		List<SimpleModel> genders = new ArrayList<SimpleModel>();

//        genders.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT,
//                Labels.getLabel(LanguageKeys.OPTION)));

		genders.add(new SimpleModel(Values.MALE, Labels.getLabel(LanguageKeys.MALE)));

		genders.add(new SimpleModel(Values.FEMALE, Labels.getLabel(LanguageKeys.FEMALE)));

		return genders;
	}

	@Override
	public Language getLanguageByLocale(String lan_locale) throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	// get set method
	@Override
	public User getNewUser() {
		return this.userDAO.getNewUser();
	}

	@Override
	public List<Right> getRightsByUser(User user) throws Exception {
		return this.rightDAO.getRightsByUser(user);
	}

	@Override
	public List<RightView> getRightViewByUserId(Long userId) throws Exception {
		return this.rightDAO.getRightViewByUserId(userId);
	}

	@Override
	public Role getRoleByName(String roleName) throws Exception {
		return this.roleDAO.getRoleByName(roleName);
	}

	// New
	@Override
	public List<Role> getRoles(boolean isAdmin) {
		return this.roleDAO.getRoles(isAdmin);
	}

	@Override
	public List<String> getRolesNameByUser(User user) throws Exception {
		List<String> roleNames = new ArrayList<String>();

		for (Role role : user.getRoles()) {
			if (Validator.isNotNull(role.getRoleName())) {
				roleNames.add(role.getRoleName());
			}
		}

		return roleNames;
	}

	@Override
	public User getUserByEmail(Long userId, String email) {
		User user = null;

		try {
			List<User> results = this.userDAO.getUsersByI_E(userId, email);

			if (Validator.isNotNull(results)) {
				user = results.get(0);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return user;
	}

	@Override
	public User getUserById(Long userId) throws Exception {
		return this.userDAO.getUserById(userId);
	}

	@Override
	public User getUserByUserName(String userName) throws Exception {
		return this.userDAO.getUserByUserName(userName);
	}

	@Override
	public List<User> getUsers(String keyword, int itemStartNumber, int pageSize, String orderByColumn,
			String orderByType) {
		return this.userDAO.getUsers(keyword, itemStartNumber, pageSize, orderByColumn, orderByType);
	}

	@Override
	public List<User> getUsers(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status, int itemStartNumber,
			int pageSize, String orderByColumn, String orderByType) {
		return this.userDAO.getUsers(userName, email, gender, birthPlace, birthdayFrom, birthdayTo, phone, mobile,
				account, status, itemStartNumber, pageSize, orderByColumn, orderByType);
	}

	@Override
	public int getUsersCount(String keyword) {
		return this.userDAO.getUsersCount(keyword);
	}

	@Override
	public int getUsersCount(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status) {
		return this.userDAO.getUsersCount(userName, email, gender, birthPlace, birthdayFrom, birthdayTo, phone, mobile,
				account, status);
	}

	@Override
	public List<User> getUsersLikeEmail(String value) throws Exception {
		return this.userDAO.getUsersLikeEmail(value);
	}

	@Override
	public List<User> getUsersLikeLastname(String value) throws Exception {
		return this.userDAO.getUsersLikeLastname(value);
	}

	@Override
	public List<User> getUsersLikeUserName(String value) throws Exception {
		return this.userDAO.getUsersLikeUserName(value);
	}

	@Override
	public boolean isEmailExits(Long userId, String email) {
		List<User> results = new ArrayList<User>();

		try {
			results = this.userDAO.getUsersByI_E(userId, email);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return !results.isEmpty();
	}

	@Override
	public boolean isIpAdrRequireCaptcha(String ip) {
		int invalidCount = this.usersLoginDAO.countByIp(ip);

		return invalidCount >= StaticUtil.LOGIN_POLICY_LIMIT_FAILURE_TIME;
	}

	@Override
	public boolean isUserNameExits(Long userId, String userName) {
		List<User> results = new ArrayList<User>();

		try {
			results = this.userDAO.getUsersByI_UN(userId, userName);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return !results.isEmpty();
	}

	@Override
	public void lockUser(List<User> users) {
		try {
			for (User user : users) {
				user.setStatus(Values.STATUS_DEACTIVE);
			}

			this.userDAO.saveOrUpdateAll(users);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void lockUser(User user) {
		try {
			user.setStatus(Values.STATUS_DEACTIVE);

			this.userDAO.saveOrUpdate(user);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void remove(String ip) {
		List<UserLogin> userLogins = this.usersLoginDAO.getUsersLogin(ip);

		this.usersLoginDAO.delete(userLogins);
	}

	@Override
	public void resetPassword(List<User> users) {
		try {
			String randomPwd = null;

			for (User user : users) {
				randomPwd = RandomStringGeneratorUtil.generate(16);

				user.setPassword(this.passwordEncoder.encode(randomPwd));
				user.setModifiedDate(new Date());

				// send email to user create

				MailUtil.sendPwdResetEmail(user.getEmail(), user.getUserName(), randomPwd, user.getFullName());
			}

			this.userDAO.saveOrUpdateAll(users);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveOrUpdate(List<User> users) throws Exception {
		for (User user : users) {
			saveOrUpdate(user);
		}
	}

	@Override
	public void saveOrUpdate(User user) {
		Long userId = user.getUserId();

		if (Validator.isNull(userId)) {
			// get role users
			addRole(user, PermissionConstants.ROLE_USERS);
		}

		this.userDAO.saveOrUpdate(user);
	}

	@Override
	public void saveOrUpdate(UserLogin loginLog) {
		this.usersLoginDAO.saveOrUpdate(loginLog);
	}

	@Override
	public List<Role> searchRoles(String roleName) {
		return this.roleDAO.searchRole(roleName);
	}

	@Override
	public void unlockUser(List<User> users) {
		try {
			for (User user : users) {
				user.setStatus(Values.STATUS_ACTIVE);
			}

			this.userDAO.saveOrUpdateAll(users);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void unlockUser(User user) {
		try {
			user.setStatus(Values.STATUS_ACTIVE);

			this.userDAO.saveOrUpdate(user);
		} catch (DataAccessException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}

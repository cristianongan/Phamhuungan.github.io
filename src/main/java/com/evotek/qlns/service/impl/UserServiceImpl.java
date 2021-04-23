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
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.dao.RightDAO;
import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.dao.UserDAO;
import com.evotek.qlns.dao.UsersLoginDAO;
import com.evotek.qlns.model.Group;
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
import com.evotek.qlns.util.PermissionUtil;
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
public class UserServiceImpl implements UserService {

    private transient UserDAO userDAO;
    private transient UsersLoginDAO usersLoginDAO;
    private transient RoleDAO roleDAO;
    private transient RightDAO rightDAO;
    private transient GroupDAO groupDAO;
    private transient CategoryDAO categoryDAO;

    //get set method

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO usersDAO) {
        this.userDAO = usersDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public RightDAO getRightDAO() {
        return rightDAO;
    }

    public void setRightDAO(RightDAO rightDAO) {
        this.rightDAO = rightDAO;
    }

    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public UsersLoginDAO getUsersLoginDAO() {
        return usersLoginDAO;
    }

    public void setUsersLoginDAO(UsersLoginDAO usersLoginDAO) {
        this.usersLoginDAO = usersLoginDAO;
    }

    //get set method
    public User getNewUser() {
        return userDAO.getNewUser();
    }

    public int getCountAllUsers() throws Exception {
        return userDAO.getCountAllUsers();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.getAllUsers();
    }

    public User getUserById(Long userId) throws Exception {
        return userDAO.getUserById(userId);
    }

    public User getUserByUserName(String userName) throws Exception {
        return userDAO.getUserByUserName(userName);
    }

    public List<User> getUsersLikeUserName(String value) throws Exception {
        return userDAO.getUsersLikeUserName(value);
    }

    public List<User> getUsersLikeLastname(String value) throws Exception {
        return userDAO.getUsersLikeLastname(value);
    }

    public List<User> getUsersLikeEmail(String value) throws Exception {
        return userDAO.getUsersLikeEmail(value);
    }

    //LinhLH fix
    public List<Language> getAllLanguages() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Language getLanguageByLocale(String lan_locale) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    public void saveOrUpdate(Users user) {
//        usersDAO.saveOrUpdate(user);
//    }

    public void saveOrUpdate(User user) {
        Long userId = user.getUserId();

        if(Validator.isNull(userId)){
            //get role users
            addRole(user, PermissionConstants.ROLE_USERS);
        }

        userDAO.saveOrUpdate(user);
    }

    public void addRole(User user, String roleName) {
        try {
            List<Role> roleUsers = roleDAO.getRoleByRN(
                    roleName, null);

            if (Validator.isNotNull(roleUsers)) {
                user.getRoles().addAll(roleUsers);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void delete(User user) throws Exception {
        userDAO.delete(user);
    }

//    public List<Role> getRolesByUser(User user) throws Exception {
//        return roleDAO.getRolesByUser(user);
//    }

    public List<String> getRolesNameByUser(User user) throws Exception {
        List<String> roleNames = new ArrayList<String>();

        for(Role role: user.getRoles()){
            if(Validator.isNotNull(role.getRoleName())){
                roleNames.add(role.getRoleName());
            }
        }

        return roleNames;
    }

    public List<Right> getRightsByUser(User user) throws Exception {
        return rightDAO.getRightsByUser(user);
    }

    public List<RightView> getRightViewByUserId(Long userId) throws Exception {
        return rightDAO.getRightViewByUserId(userId);
    }

    public List<Group> getGroupsByUser(User user) throws Exception {
        return groupDAO.getGroupByUser(user);
    }

    public Role getRoleByName(String roleName) throws Exception {
        return roleDAO.getRoleByName(roleName);
    }

    public boolean isIpAdrRequireCaptcha(String ip){
        int invalidCount = usersLoginDAO.countByIp(ip);

        return invalidCount>= StaticUtil.LOGIN_POLICY_LIMIT_FAILURE_TIME;
    }

    public void remove(String ip){
        List<UserLogin> userLogins = usersLoginDAO.getUsersLogin(ip);

        usersLoginDAO.delete(userLogins);
    }

    public void saveOrUpdate(UserLogin loginLog){
        usersLoginDAO.saveOrUpdate(loginLog);
    }

    public List<User> getUsers(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status, int itemStartNumber,
            int pageSize, String orderByColumn, String orderByType) {
        return userDAO.getUsers(userName, email, gender, birthPlace, birthdayFrom,
                birthdayTo, phone, mobile, account, status, itemStartNumber,
                pageSize, orderByColumn, orderByType);
    }

    public int getUsersCount(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status) {
        return userDAO.getUsersCount(userName, email, gender, birthPlace,
                birthdayFrom, birthdayTo, phone, mobile, account, status);
    }

    public List<User> getUsers(String keyword, int itemStartNumber, int pageSize,
            String orderByColumn, String orderByType) {
        return userDAO.getUsers(keyword, itemStartNumber, pageSize, orderByColumn,
                orderByType);
    }

    public int getUsersCount(String keyword) {
        return userDAO.getUsersCount(keyword);
    }

    public List<SimpleModel> getGenderType() {
        List<SimpleModel> genders = new ArrayList<SimpleModel>();

//        genders.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT,
//                Labels.getLabel(LanguageKeys.OPTION)));

        genders.add(new SimpleModel(Values.MALE,
                Labels.getLabel(LanguageKeys.MALE)));

        genders.add(new SimpleModel(Values.FEMALE,
                Labels.getLabel(LanguageKeys.FEMALE)));

        return genders;
    }

    public void lockUser(User user) {
        try {
            user.setStatus(Values.STATUS_DEACTIVE);

            userDAO.saveOrUpdate(user);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void lockUser(List<User> users) {
        try {
            for (User user : users) {
                user.setStatus(Values.STATUS_DEACTIVE);
            }

            userDAO.saveOrUpdateAll(users);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockUser(User user) {
        try {
            user.setStatus(Values.STATUS_ACTIVE);

            userDAO.saveOrUpdate(user);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockUser(List<User> users) {
        try {
            for (User user : users) {
                user.setStatus(Values.STATUS_ACTIVE);
            }

            userDAO.saveOrUpdateAll(users);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void resetPassword(List<User> users) {
        try {
            String randomPwd = null;
            
            for (User user : users) {
                randomPwd = RandomStringGeneratorUtil.generate(16);
                
                user.setPassword(PermissionUtil.encodePassword(
                        randomPwd, user.getUserId()));
                user.setModifiedDate(new Date());
                
                //send email to user create
                
                MailUtil.sendPwdResetEmail(user.getEmail(), user.getUserName(), 
                        randomPwd, user.getFullName());
            }

            userDAO.saveOrUpdateAll(users);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void activateUser(List<User> users) {
        try {
            for (User user : users) {
                user.setStatus(Values.STATUS_ACTIVE);
            }

            userDAO.saveOrUpdateAll(users);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void createPassword(User user) {
        String randomPass = RandomStringGeneratorUtil.generate(16);

        if (randomPass.length() > 0) {
            user.setPassword(PermissionUtil.encodePassword(
                    randomPass, user.getUserId()));
        }

        this.saveOrUpdate(user);

        //send email to user create
        HttpServletRequest req = (HttpServletRequest) 
                Executions.getCurrent().getNativeRequest();

        String hostAddress = GetterUtil.getServerUrl(req);

        MailUtil.sendUserCreateEmail(user.getEmail(), user.getUserName(),
                randomPass, user.getFullName(), hostAddress);
    }
    
    public void delete(List<Role> roles, User user) {
        try {
            user.getRoles().removeAll(roles);

            userDAO.saveOrUpdate(user);
        } catch (DataAccessException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public List<String> delete(List<User> users) throws Exception{
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

    public boolean isUserNameExits(Long userId, String userName) {
        List<User> results = new ArrayList<User>();

        try {
            results = userDAO.getUsersByI_UN(userId, userName);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return !results.isEmpty();
    }

    public boolean isEmailExits(Long userId, String email) {
        List<User> results = new ArrayList<User>();

        try {
            results = userDAO.getUsersByI_E(userId, email);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return !results.isEmpty();
    }

    public User getUserByEmail(Long userId, String email){
        User user = null;
        
        try {
            List<User> results = userDAO.getUsersByI_E(userId, email);
            
            if(Validator.isNotNull(results)){
                user = results.get(0);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
        
        return user;
    }
    
    public void addRole(Long userId, String roleName) {
        try {
            User user = userDAO.getUserById(userId);

            if (user != null) {
                addRole(user, roleName);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    //New
    public List<Role> getRoles(boolean isAdmin) {
        return roleDAO.getRoles(isAdmin);
    }

    public List<Role> searchRoles(String roleName) {
        return roleDAO.searchRole(roleName);
    }

    private static final Logger _log =
            LogManager.getLogger(UserServiceImpl.class);

    public void saveOrUpdate(List<User> users) throws Exception {
        for(User user: users){
            saveOrUpdate(user);
        }
    }

    public void assignRoleToUser(User user, List<Role> roles, boolean isAdmin)
            throws Exception{
        for(Role role: roles){
            if(isAdmin || role.getShareable()){
                user.getRoles().add(role);
            }
        }

        userDAO.saveOrUpdate(user);
    }
    
    public void addVerifyResetPwd(User user) {
        if (Values.STATUS_DEACTIVE.equals(user.getStatus())) {
            MailUtil.sendVerifyResetPwd(user.getEmail(), user.getUserName(), 
                    user.getFullName());
        } else {
            StringBuilder sb = new StringBuilder();
            
            sb.append(user.getUserId());
            sb.append(StringPool.COMMA);
            sb.append(Values.VERIFY_RESET_PWD);
            sb.append(StringPool.COMMA);
            sb.append(DateUtil.formatLongDate(
                    DateUtil.getDateAfter(StaticUtil.VERIFY_RESET_PASSWORD_AVAIABLE_TIME)));

            //create verify
            String verifyCode = new EncryptUtil().encrypt(sb.toString());
            
            user.setVerificationCode(verifyCode);
            
            userDAO.saveOrUpdate(user);
            //
            
            MailUtil.sendVerifyResetPwd(user.getEmail(), user.getUserName(), 
                    user.getFullName(), verifyCode);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.Date;
import java.util.List;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Language;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.UserLogin;

/**
 *
 * @author linhlh2
 */
public interface UserService {

    /**
     * EN: Get a new User object.<br>
     *
     * @return Users
     */
    public User getNewUser();

    /**
     * EN: Get the count of all Users.<br>
     *
     * @return int
     * @throws java.lang.Exception
     */
    public int getCountAllUsers() throws Exception;

    /**
     * EN: Get a list of all Users.<br>
     *
     * @return List of Users / Liste aus Usern
     * @throws java.lang.Exception
     */
    public List<User> getAllUsers() throws Exception;

    /**
     * EN: Get an User by its ID.<br>
     * @param userId
     * @return User/ User
     * @throws java.lang.Exception
     */
    public User getUserById(Long userId) throws Exception;

    /**
     * EN: Get an User by its LoginName.<br>
     * @param userName
     *            UserName / User Name
     * @return User/ User
     * @throws java.lang.Exception
     */
    public User getUserByUserName(final String userName) throws Exception;

    /**
     * EN: Gets a list of Users where the LoginName contains the %string% .<br>
     *
     * @param value
     * @return List of Users / Liste of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeUserName(String value) throws Exception;

    /**
     * EN: Gets a list of Users where the LastName name contains the %string% .<br>
     *
     * @param value
     * @return List of Users / Liste of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeLastname(String value) throws Exception;

    /**
     * EN: Get a list of Users by its emailaddress with the like SQL operator.<br>
     *
     * @param value
     * @return List of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeEmail(String value) throws Exception;

//    public List<Role> getRolesByUser(User user) throws Exception;

    public List<String> getRolesNameByUser(User user) throws Exception;

    public List<Right> getRightsByUser(User user) throws Exception;

    public List<RightView> getRightViewByUserId(Long userId) throws Exception;

    public List<Group> getGroupsByUser(User user) throws Exception;

    public List<Language> getAllLanguages() throws Exception;

    public Language getLanguageByLocale(String lan_locale) throws Exception;

    /**
     * EN: Saves new or updates an User.<br>
     * @param user
     */
    public void saveOrUpdate(User user);

    /**
     * EN: Deletes an User.<br>
     * @param user
     * @throws java.lang.Exception
     */
    public void delete(User user) throws Exception;

    public Role getRoleByName(String roleName) throws Exception;

//    public void saveRoles(List<UserRole> userRoles) throws Exception;

    public boolean isIpAdrRequireCaptcha(String ip);

    public void remove(String ip);

    public void saveOrUpdate(UserLogin loginLog);

    public List<User> getUsers(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile,String account, Long status, int itemStartNumber,
            int pageSize, String orderByColumn, String orderByType);

    public int getUsersCount(String userName, String email,Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status);

    public List<User> getUsers(String keyword, int itemStartNumber,
            int pageSize, String orderByColumn, String orderByType);

    public int getUsersCount(String keyword);

    public void lockUser(User user);

    public void lockUser(List<User> users);

    public void unlockUser(User user);

    public void unlockUser(List<User> users);

    public void resetPassword(List<User> users);

    public void activateUser(List<User> users);
    
    public void createPassword(User user);

    public List<SimpleModel> getGenderType();

    public List<String> delete(List<User> users) throws Exception;

    public void delete(List<Role> roles, User user);

    public boolean isUserNameExits(Long userId,String userName);

    public boolean isEmailExits(Long userId, String email);
    
    public User getUserByEmail(Long userId, String email);

    public void addRole(User user, String roleName);

    public void addRole(Long userId, String roleName);

    public List<Role> getRoles(boolean isAdmin);

    public List<Role> searchRoles(String roleName);

    public void saveOrUpdate(List<User> users) throws Exception;

    public void assignRoleToUser(User user, List<Role> roles, boolean isAdmin)
            throws Exception;
    
    public void addVerifyResetPwd(User user);
}

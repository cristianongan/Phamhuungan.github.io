/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.Date;
import java.util.List;

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

    public void activateUser(List<User> users);

    public void addRole(Long userId, String roleName);

    public void addRole(User user, String roleName);

    public void addVerifyResetPwd(User user);

    public void assignRoleToUser(User user, List<Role> roles, boolean isAdmin)
            throws Exception;

    public void createPassword(User user);

    public void delete(List<Role> roles, User user);

    public List<String> delete(List<User> users) throws Exception;

//    public List<Role> getRolesByUser(User user) throws Exception;

    /**
     * EN: Deletes an User.<br>
     * @param user
     * @throws java.lang.Exception
     */
    public void delete(User user) throws Exception;

    public List<Language> getAllLanguages() throws Exception;

    /**
     * EN: Get a list of all Users.<br>
     *
     * @return List of Users / Liste aus Usern
     * @throws java.lang.Exception
     */
    public List<User> getAllUsers() throws Exception;

    /**
     * EN: Get the count of all Users.<br>
     *
     * @return int
     * @throws java.lang.Exception
     */
    public int getCountAllUsers() throws Exception;

    public List<SimpleModel> getGenderType();

    public Language getLanguageByLocale(String lan_locale) throws Exception;

    /**
     * EN: Get a new User object.<br>
     *
     * @return Users
     */
    public User getNewUser();

    public List<Right> getRightsByUser(User user) throws Exception;

//    public void saveRoles(List<UserRole> userRoles) throws Exception;

    public List<RightView> getRightViewByUserId(Long userId) throws Exception;

    public Role getRoleByName(String roleName) throws Exception;

    public List<Role> getRoles(boolean isAdmin);

    public List<String> getRolesNameByUser(User user) throws Exception;

    public User getUserByEmail(Long userId, String email);

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

    public List<User> getUsers(String keyword, int itemStartNumber,
            int pageSize, String orderByColumn, String orderByType);

    public List<User> getUsers(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile,String account, Long status, int itemStartNumber,
            int pageSize, String orderByColumn, String orderByType);

    public int getUsersCount(String keyword);

    public int getUsersCount(String userName, String email,Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status);

    /**
     * EN: Get a list of Users by its emailaddress with the like SQL operator.<br>
     *
     * @param value
     * @return List of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeEmail(String value) throws Exception;

    /**
     * EN: Gets a list of Users where the LastName name contains the %string% .<br>
     *
     * @param value
     * @return List of Users / Liste of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeLastname(String value) throws Exception;
    
    /**
     * EN: Gets a list of Users where the LoginName contains the %string% .<br>
     *
     * @param value
     * @return List of Users / Liste of Users
     * @throws java.lang.Exception
     */
    public List<User> getUsersLikeUserName(String value) throws Exception;

    public boolean isEmailExits(Long userId, String email);

    public boolean isIpAdrRequireCaptcha(String ip);

    public boolean isUserNameExits(Long userId,String userName);

    public void lockUser(List<User> users);

    public void lockUser(User user);
    
    public void remove(String ip);

    public void resetPassword(List<User> users);

    public void saveOrUpdate(List<User> users) throws Exception;

    /**
     * EN: Saves new or updates an User.<br>
     * @param user
     */
    public void saveOrUpdate(User user);

    public void saveOrUpdate(UserLogin loginLog);

    public List<Role> searchRoles(String roleName);

    public void unlockUser(List<User> users);
    
    public void unlockUser(User user);
}

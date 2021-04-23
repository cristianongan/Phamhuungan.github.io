package com.evotek.qlns.policy.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.UserLogin;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.Validator;

/**
 * This class implements the spring-security UserDetailService Interface.<br>
 * It's been configured in the 'springSecurityContext.xml'.<br>
 *
 * @author linhlh2
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

    private static final long serialVersionUID = 1368775416931L;

    private static final Logger _log = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        User user = null;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        List<String> roles = new ArrayList<String>();

        try {
            user = getUserByUserName(userName);

            if (user == null) {
                saveLoginFailure(userName);
                
                return null;
            }

            roles = getRolesByUser(user);

            grantedAuthorities = getGrantedAuthority(user.getUserId());
        } catch (final NumberFormatException e) {
            throw new DataRetrievalFailureException("Cannot loadUserByUsername userName:" +
                    userName + " Exception:" + e.getMessage(), e);
        } catch (Exception ex){
            _log.error(ex.getMessage(), ex);
        }

        // Create the UserDetails object for a specified user with
        // their grantedAuthorities List.
        final UserDetails userDetails = new UserPrincipalImpl(user, roles,
                grantedAuthorities);

        if (_log.isDebugEnabled()) {
            _log.debug("Rights for '" + user.getUserName() + "' (ID: " +
                    user.getUserId() + ") evaluated. [" + this + "]");
        }

        return userDetails;
    }

    /**
     * Gets the User object by his stored userName.<br>
     *
     * @param userName
     * @return
     */
    public User getUserByUserName(final String userName) throws Exception{
        return this.userService.getUserByUserName(userName);
    }

    /**
     * Gets the roles has assign to user
     * @param User
     * @return
     */
     public List<String> getRolesByUser(final User user) throws Exception{
         return this.userService.getRolesNameByUser(user);
     }
    /**
     * Fills the GrantedAuthorities List for a specified user.<br>
     * 1. Gets a unique list of rights that a user have.<br>
     * 2. Creates GrantedAuthority objects from all rights. <br>
     * 3. Creates a GrantedAuthorities list from all GrantedAuthority objects.<br>
     *
     * @param user
     * @return
     */
    private List<GrantedAuthority> getGrantedAuthority(Long userId) throws Exception {

        final ArrayList<GrantedAuthority> rightsGrantedAuthorities =
                new ArrayList<GrantedAuthority>();

        try {
            // get the list of rights for a specified user from db.
            final List<RightView> rightViews = this.userService.
                    getRightViewByUserId(userId);

            // now create for all rights a GrantedAuthority entry
            // and fill the GrantedAuthority List with these authorities.

            for (final RightView rightView : rightViews) {
                rightsGrantedAuthorities.add(
                        new SimpleGrantedAuthority(rightView.getRightName()));
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return rightsGrantedAuthorities;
    }
    
    private void saveLoginFailure(String userName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();

        if (Validator.isIPAddress(ip)) {
            UserLogin loginLog = new UserLogin(userName, ip);

            loginLog.setLoginTime(new Date());
            loginLog.setSuccess(false);

            this.userService.saveOrUpdate(loginLog);
        }
    }
}

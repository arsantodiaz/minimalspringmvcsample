package com.kopetto.sample.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.security.SocialUserDetailsService;

import com.kopetto.sample.domain.entity.profile.User;

/**
 * Domain Service interface for user administration. It also extends SocialUserDetailsService,
 * UserDetailsService and UserIdSource.
 * 
 */
public interface UserAdminService extends SocialUserDetailsService, UserDetailsService, UserIdSource{
    
    /**
     * @param data
     * @return
     */
    User createUser(ConnectionData data);

    /* (non-Javadoc)
     * @see org.springframework.social.security.SocialUserDetailsService#loadUserByUserId(java.lang.String)
     */
    User loadUserByUserId(String userId) throws UsernameNotFoundException;
    
    /**
     * Gets current logged in user. Reload UserAccount object from userId in SecurityContextHolder. 
     * 
     * @return UserAccount object from database for current user
     */
    User getCurrentUser();

	/**Find user by FB Id
	 * @param id
	 * @return
	 */
	User getUserByFBId(String id);

}

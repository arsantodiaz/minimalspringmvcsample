package com.kopetto.sample.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.social.connect.ConnectionData;

import com.kopetto.sample.domain.entity.profile.User;
import com.kopetto.sample.domain.entity.profile.UserSocialConnection;


/**
 *
 */
public interface UserService {
    
    /**
     * @param userId
     * @return
     */
    User findByUserId (String userId);

    /**
     * @return
     */
    List<User> getAllUsers();
    
    /**
     * @param pageable
     * @return
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * @param pageable
     * @param filter
     * @return
     */
    Page<User> getAllUsers(Pageable pageable, String filter);
    
    /**
     * @param userId
     * @return
     */
    List<UserSocialConnection> getConnectionsByUserId(String userId);
    
    /**
     * @param data
     * @return
     */
    User createUserAccount(ConnectionData data);

	
}
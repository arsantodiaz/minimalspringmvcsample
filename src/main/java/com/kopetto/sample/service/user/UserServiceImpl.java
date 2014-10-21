package com.kopetto.sample.service.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.connect.ConnectionData;
import org.springframework.stereotype.Service;

import com.kopetto.sample.domain.dao.profile.UserAccountRepository;
import com.kopetto.sample.domain.dao.profile.UserSocialConnectionRepository;
import com.kopetto.sample.domain.entity.profile.User;
import com.kopetto.sample.domain.entity.profile.UserSocialConnection;


/**
* Implementation for UserService.
*
*/
@Service
public class UserServiceImpl implements UserService {
    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserAccountRepository accountRepository;
    private final UserSocialConnectionRepository userSocialConnectionRepository;
    private final UserAdminService userAdminService;

    @Autowired
    public UserServiceImpl(UserAccountRepository accountRepository, UserSocialConnectionRepository
            userSocialConnectionRepository, UserAdminService userAdminService) {
        this.accountRepository = accountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userAdminService = userAdminService;
    }

    @Override
    public User findByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return accountRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getAllUsers(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public Page<User> getAllUsers(Pageable pageable, String filter) {
        return accountRepository.findByFilter(pageable, filter);
    }
    
    @Override
    public List<UserSocialConnection> getConnectionsByUserId(String userId){
        return this.userSocialConnectionRepository.findByUserId(userId);
    }
    
    @Override
    public User createUserAccount(ConnectionData data) {
    	
    	User ua = this.userAdminService.getUserByFBId (data.getProviderUserId()); 
		if (ua == null){
			ua = userAdminService.createUser(data);
		}
        return ua;
    }

}

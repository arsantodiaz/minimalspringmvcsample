package com.kopetto.sample.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionData;
import org.springframework.stereotype.Service;

import com.kopetto.sample.domain.dao.profile.UserAccountRepository;
import com.kopetto.sample.domain.entity.profile.User;
import com.kopetto.sample.domain.entity.profile.UserRoleType;
import com.kopetto.sample.service.CounterService;
import com.kopetto.sample.util.AccountUtils;

/**
 * Implementation for UserAdminService.
 * 
 */

@Service
public class UserAdminServiceImpl implements UserAdminService {
    public static final String USER_ID_PREFIX = "user";

    @Autowired
    private UserAccountRepository accountRepository;

    @Autowired
    private CounterService counterService;

    @Override
    public User createUser(ConnectionData data) {
        long userIdSequence = this.counterService.getNextUserIdSequence();
        User account = new User();
        account.setUserId(USER_ID_PREFIX + userIdSequence);
        account.setDisplayName(data.getDisplayName());
        account.setImageUrl(data.getImageUrl());
        
        account.setFbUserName(data.getDisplayName());
        account.setFbUserId(data.getProviderUserId());
        
        account.setRoles(new UserRoleType[] { UserRoleType.ROLE_USER});
        
        this.accountRepository.save(account);

        return account;
    }


    @Override
    public User loadUserByUserId(String userId) throws UsernameNotFoundException {
        User account = accountRepository.findByUserId(userId);
        if (account == null) {
        	
        	//could be email from login form
            account = accountRepository.findByEmail (userId);
            if (account == null) {
            	throw new UsernameNotFoundException("Cannot find user by userId " + userId);
            }
        }
        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUserId(username);
    }

    @Override
    public String getUserId() {
        return AccountUtils.getLoginUserId();
    }

    @Override
    public User getCurrentUser() {
        return accountRepository.findByUserId(getUserId());
    }

	@Override
	public User getUserByFBId(String fbUserId) {
		return accountRepository.findByFbUserId(fbUserId);
	}

}

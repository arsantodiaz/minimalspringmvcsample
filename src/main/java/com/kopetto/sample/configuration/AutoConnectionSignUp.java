package com.kopetto.sample.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;

import com.kopetto.sample.domain.entity.profile.User;
import com.kopetto.sample.service.user.UserService;

/**
 * Automatically sign up user who is already signin through other social network account (google or twitter).
 * Create a new UserAccount in database, populate user's profile data from provider.
 * 
 */
public class AutoConnectionSignUp implements ConnectionSignUp{
    private final UserService accountService;
    
    @Autowired
    public AutoConnectionSignUp(UserService accountService){
        this.accountService = accountService;
    }
    
    public String execute(Connection<?> connection) {
        ConnectionData data = connection.createData();
        
        User account = this.accountService.createUserAccount(data);
        
        return account.getUserId();
    }
}

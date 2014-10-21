package com.kopetto.sample.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.provider.OAuth2AuthenticationService;
import org.springframework.social.security.provider.SocialAuthenticationService;

import com.kopetto.sample.domain.dao.profile.UserSocialConnectionRepository;
import com.kopetto.sample.domain.dao.social.MongoUsersConnectionRepositoryImpl;
import com.kopetto.sample.domain.entity.profile.User;
import com.kopetto.sample.service.user.UserService;
import com.kopetto.sample.util.AccountUtils;

@Configuration
public class SocialContext {

	@Autowired
    private Environment environment;

    @Autowired
    private UserService accountService;

    @Autowired
    private UserSocialConnectionRepository userSocialConnectionRepository;

    /**
* When a new provider is added to the app, register its {@link SocialAuthenticationService} here.
*
*/
    @Bean
    public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
        SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
        
        //add facebook
        OAuth2ConnectionFactory<Facebook> facebookConnectionFactory = new FacebookConnectionFactory(environment.getProperty("facebook.clientId"),
                environment.getProperty("facebook.clientSecret"));
        OAuth2AuthenticationService<Facebook> facebookAuthenticationService = new OAuth2AuthenticationService<Facebook>(facebookConnectionFactory);
        //facebookAuthenticationService.setScope(environment.getProperty("facebook.scope")); 
        registry.addAuthenticationService(facebookAuthenticationService);

        return registry;
    }

    /**
* Singleton data access object providing access to connections across all users.
*/
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        MongoUsersConnectionRepositoryImpl repository = new MongoUsersConnectionRepositoryImpl(userSocialConnectionRepository,
                socialAuthenticationServiceLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(autoConnectionSignUp());
        return repository;
    }

    /**
* Request-scoped data access object providing access to the current user's connections.
*/
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        User user = AccountUtils.getLoginUserAccount();
        return usersConnectionRepository().createConnectionRepository(user.getUsername());
    }

    /**
* A proxy to a request-scoped object representing the current user's primary Google account.
*
* @throws NotConnectedException
* if the user is not connected to FB.
*/
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Facebook facebook() {
        Connection<Facebook> facebook = connectionRepository().findPrimaryConnection(Facebook.class);
        return facebook != null ? facebook.getApi() : new FacebookTemplate();
    }

    @Bean
    public ConnectionSignUp autoConnectionSignUp() {
        return new AutoConnectionSignUp(accountService);
    }

}
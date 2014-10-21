package com.kopetto.sample.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import com.kopetto.sample.domain.dao.MongoPersistentTokenRepositoryImpl;
import com.kopetto.sample.domain.dao.RememberMeTokenRepository;
import com.kopetto.sample.service.user.UserAdminService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityContext extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment environment;

    @Autowired
    private UserAdminService userAdminService;
    
    @Autowired
    private RememberMeTokenRepository rememberMeTokenRepository;
    
    @Autowired
    private UsersConnectionRepository usersConnectionRepository;
    
    @Autowired
    private SocialAuthenticationServiceLocator socialAuthenticationServiceLocator;
    
    @Bean
    public SocialAuthenticationFilter socialAuthenticationFilter() throws Exception{
        SocialAuthenticationFilter filter = new SocialAuthenticationFilter(authenticationManager(), userAdminService,
                usersConnectionRepository, socialAuthenticationServiceLocator);
        filter.setFilterProcessesUrl("/signin");
        filter.setSignupUrl(null);
        filter.setConnectionAddedRedirectUrl("/account");
        filter.setPostLoginUrl("/account"); 
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SocialAuthenticationProvider socialAuthenticationProvider(){
        return new SocialAuthenticationProvider(usersConnectionRepository, userAdminService);
    }
    
    @Bean
    public LoginUrlAuthenticationEntryPoint socialAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/signin");
    }

    @Bean
    public RememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                        environment.getProperty("application.key"), userAdminService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }
    
    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider =
                        new RememberMeAuthenticationProvider(environment.getProperty("application.key"));
        return rememberMeAuthenticationProvider;
    }

    /**Provide mongo repo for storing remember me tokens
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoPersistentTokenRepositoryImpl(rememberMeTokenRepository);
    }

    @Override
    public void configure(WebSecurity builder) throws Exception {
        builder
            .ignoring()
                .antMatchers("/resources/**");
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
        http
        	.formLogin().
        		loginPage("/signin")
	            .permitAll()         
        	.and()
            .authorizeRequests()
                .antMatchers("/dologin").permitAll()//form login
                .antMatchers("/favicon.ico","robots.txt","/resources/**","/site/**").permitAll()
                .antMatchers("/account").hasAuthority("ROLE_USER")
                .and()
         .addFilterBefore(socialAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
         .logout()
         .deleteCookies("JSESSIONID")
         .logoutUrl("/signout")
         .logoutSuccessUrl("/")
         .permitAll()
         .and()
         .rememberMe()
         .rememberMeServices(rememberMeServices())
         ;
        }
    
    @Override
    protected void configure (AuthenticationManagerBuilder builder) throws Exception{
        builder
            .authenticationProvider(socialAuthenticationProvider())
            .authenticationProvider(rememberMeAuthenticationProvider())
            .userDetailsService(userAdminService);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }
}
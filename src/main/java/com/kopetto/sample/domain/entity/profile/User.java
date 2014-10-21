package com.kopetto.sample.domain.entity.profile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.util.StringUtils;

import com.kopetto.sample.domain.entity.BaseEntity;

/**
 * Domain Entity for user account.
 * 
 */
@SuppressWarnings("serial")
@Document(collection = "User")
public class User extends BaseEntity implements SocialUserDetails {
    @Indexed
    private String userId;
    private String password;
    
    private UserRoleType[] roles;
    
    private List<String> followConversationIds;
    
    private String email;
    
    private String displayName;
    
    private String imageUrl;
    
    private String webSite;
    
    private String fbUserName;

	private String fbUserId;

	private Date createdDt = new Date ();
	
    @Transient
    private List<UserSocialConnection> connections;

    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }

    public UserRoleType[] getRoles() {
        return roles;
    }

    public void setRoles(UserRoleType[] roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public List<UserSocialConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<UserSocialConnection> connections) {
        this.connections = connections;
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return getUserId();
    }

    public String getWebSiteLink(){
        if (StringUtils.hasText(getWebSite())){
            if (getWebSite().startsWith("http://") || getWebSite().startsWith("https://")){
                return getWebSite();
            }
            return "http://"+getWebSite();
            // add http:// to fix URL 
        }
        return "#";
    }

    public String getNameLink(){
        //TODO link to profile page
        return getWebSiteLink();
    }
    
    public boolean isAdmin(){
        for (UserRoleType role : getRoles()) {
            if (role == UserRoleType.ROLE_ADMIN){
                return true;
            }
        }        
        return false;
    }

    public boolean isHasImageUrl(){
        return StringUtils.hasLength(getImageUrl());
    }
    
    // used for account social connection
    private UserSocialConnection getConnection(String providerId) {
        if (this.connections != null){
            for (UserSocialConnection connection : this.connections){
                if (connection.getProviderId().equals(providerId)){
                    return connection;
                }
            }
        }
        return null;
    }

    public UserSocialConnection getFacebookConnection() {
        return getConnection("facebook");
    }
    
    public boolean isHasFacebookConnection(){
        return getFacebookConnection()  != null;
    }
    
    public void updateProfile(String displayName, String email, String webSite, String password){
        setDisplayName(displayName);
        setEmail(email);
        setWebSite(webSite);
        setPassword(password);
    }
    
    public String getFbUserName() {
		return fbUserName;
	}

	public void setFbUserName(String fbUserName) {
		this.fbUserName = fbUserName;
	}

	public String getFbUserId() {
		return fbUserId;
	}

	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public List<String> getFollowConversationIds() {
		return followConversationIds;
	}

	public void setFollowConversationIds(List<String> followConversationIds) {
		this.followConversationIds = followConversationIds;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
    @Override
    public String toString() {
        String str = String.format("UserAccount{userId:'%s'; displayName:'%s';roles:[", getUserId(), getDisplayName());
        for (UserRoleType role : getRoles()) {
            str += role.toString() + ",";
        }
        return str + "]}";
    }



}

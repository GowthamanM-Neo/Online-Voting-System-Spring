package com.example.demo.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

 

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dao.UserDao;

 

public class MyUserDetails implements UserDetails {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    
    public MyUserDetails(String username) {
        this.email = username;
    }
    
    public MyUserDetails() {
        // TODO Auto-generated constructor stub
    }
    
    public MyUserDetails(UserDao user) {
        
        
        
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = Arrays.stream(user.getRoles().split(","))
                        .map(SimpleGrantedAuthority :: new)
                        .collect(Collectors.toList());
    }

 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }
    
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }
    
    public String getEmail() {
        // TODO Auto-generated method stub
        return this.email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return this.active;
    }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
    
}

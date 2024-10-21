package io.github.venkat1701.rolebasedaccesscontrolpoc.service;

import io.github.venkat1701.rolebasedaccesscontrolpoc.dto.UserRegistrationDTO;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface UserService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Collection<? extends GrantedAuthority> getAuthorities();

    User findUserProfileByJwt(String jwt) throws Exception;

    public User registerUser(UserRegistrationDTO userRegistrationDTO);

    public User createAdministrator(UserRegistrationDTO input);
}

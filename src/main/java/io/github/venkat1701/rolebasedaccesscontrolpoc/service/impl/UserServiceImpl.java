package io.github.venkat1701.rolebasedaccesscontrolpoc.service.impl;

import io.github.venkat1701.rolebasedaccesscontrolpoc.dto.UserRegistrationDTO;
import io.github.venkat1701.rolebasedaccesscontrolpoc.exceptions.UserNotFoundException;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.Role;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.RoleEnum;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.User;
import io.github.venkat1701.rolebasedaccesscontrolpoc.repositories.RoleRepository;
import io.github.venkat1701.rolebasedaccesscontrolpoc.repositories.UserRepository;
import io.github.venkat1701.rolebasedaccesscontrolpoc.security.jwt.JwtProvider;
import io.github.venkat1701.rolebasedaccesscontrolpoc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(username).get();
            if(user == null) {
                throw new UserNotFoundException("User not found");
            }
            Role role = user.getRole();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(authority));
        } catch(UserNotFoundException unfe) {
            unfe.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public User findUserById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("User with corresponding email not found");
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email).get();
        if(user == null)
            throw new UserNotFoundException("User with profile not found");

        return user;
    }

    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        Optional<Role> role = roleRepository.findByName(RoleEnum.USER);
        if(role.isEmpty()) {
            return null;
        }
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setGender(registrationDTO.getGender());
        user.setMobile(registrationDTO.getPhone());
        user.setRole(role.get());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return userRepository.save(user);
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User createAdministrator(UserRegistrationDTO userDto) {
        Optional<Role> role = roleRepository.findByName(RoleEnum.ADMIN);
        if(role.isEmpty()) {
            return null;
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        user.setMobile(userDto.getPhone());
        user.setRole(role.get());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }
}

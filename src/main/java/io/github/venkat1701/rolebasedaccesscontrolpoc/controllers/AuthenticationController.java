package io.github.venkat1701.rolebasedaccesscontrolpoc.controllers;

import io.github.venkat1701.rolebasedaccesscontrolpoc.dto.AuthRequest;
import io.github.venkat1701.rolebasedaccesscontrolpoc.dto.AuthResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthenticationController(UserService userService, UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserRegistrationDTO registrationDTO) throws UserNotFoundException {
        String email = registrationDTO.getEmail();
        String password = registrationDTO.getPassword();
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if(optionalRole.isEmpty()) {
            return null;
        }
        registrationDTO.setRole(optionalRole.get());
        User createdUser = userService.registerUser(registrationDTO);

        List<GrantedAuthority> authorities = new ArrayList<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication, createdUser.getId());

        return ResponseEntity.ok().body(new AuthResponse(jwt, "User Registered Successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody AuthRequest loginRequestDTO) throws UserNotFoundException {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();
        Authentication authentication = this.authenticate(email, password);
        User user = userRepository.findByEmail(email).get();
        if(user == null) {
            throw new BadCredentialsException("Invalid Email or Password");
        }

        String jwt = jwtProvider.generateToken(authentication, user.getId());
        return ResponseEntity.ok().body(new AuthResponse(jwt, "User Successfully Login"));

    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userService.loadUserByUsername(email);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid Email or Password");
        } if(!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}

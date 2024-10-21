package io.github.venkat1701.rolebasedaccesscontrolpoc.dto;

import io.github.venkat1701.rolebasedaccesscontrolpoc.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private Role role;
}

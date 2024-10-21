package io.github.venkat1701.rolebasedaccesscontrolpoc.components;

import io.github.venkat1701.rolebasedaccesscontrolpoc.model.Role;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.RoleEnum;
import io.github.venkat1701.rolebasedaccesscontrolpoc.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.initializeRoles();
    }

    private void initializeRoles() {
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN};
        Map<RoleEnum, String> roleDescriptions = Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.SUPER_ADMIN, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(roleDescriptions.get(roleName));
                roleRepository.save(role);
            });
        });
    }
}

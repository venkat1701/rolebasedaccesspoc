package io.github.venkat1701.rolebasedaccesscontrolpoc.repositories;

import io.github.venkat1701.rolebasedaccesscontrolpoc.model.Role;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}

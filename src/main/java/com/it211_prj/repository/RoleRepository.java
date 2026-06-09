package com.it211_prj.repository;

import com.it211_prj.entity.Role;
import com.it211_prj.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository lay role mac dinh khi register va phan quyen.
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

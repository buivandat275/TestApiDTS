package vn.buivandat.TestApiDTS.repository;

import org.springframework.stereotype.Repository;

import vn.buivandat.TestApiDTS.domain.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    Role findById(long id);

    Role findByName(String name);
}

    

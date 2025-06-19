package vn.buivandat.TestApiDTS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.buivandat.TestApiDTS.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
    boolean existsByModuleAndApiPathAndMethod(String m, String A, String M);

    Permission findPermissionById(long id);

    List<Permission> findByIdIn(List<Long> id);
}

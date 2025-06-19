package vn.buivandat.TestApiDTS.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.buivandat.TestApiDTS.domain.Permission;
import vn.buivandat.TestApiDTS.domain.Role;
import vn.buivandat.TestApiDTS.repository.PermissionRepository;
import vn.buivandat.TestApiDTS.repository.RoleRepository;

@Service
public class RoleService {
     private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

  public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  public boolean existByName(String name) {
    return this.roleRepository.existsByName(name);
  }

  public boolean existById(Long id) {
    return this.roleRepository.existsById(id);
  }

  public Role fetchById(long id){
    return this.roleRepository.findById(id);
  }

  public Role create(Role role) {
    // check permission
    if (role.getPermissions() != null) {
      List<Long> reqPermission = role.getPermissions()
          .stream().map(x -> x.getId())
          .collect(Collectors.toList());
      List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
      role.setPermissions(dbPermissions);
    }
    return this.roleRepository.save(role);
  }

   public Role Update(Role r) {
    Role roleDB = this.roleRepository.findById(r.getId());

    roleDB.setName(r.getName());
    roleDB.setDescription(r.getDescription());
    roleDB.setActive(r.isActive());

    // check permission
    if (r.getPermissions() != null) {
      List<Long> reqPermission = r.getPermissions()
          .stream().map(x -> x.getId())
          .collect(Collectors.toList());
      List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
      r.setPermissions(dbPermissions);
    }
    roleDB.setPermissions(r.getPermissions());

    roleDB = this.roleRepository.save(roleDB);
    return roleDB;

  }
   public void delete(long id){
    this.roleRepository.deleteById(id);
   }


}

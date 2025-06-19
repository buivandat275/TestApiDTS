package vn.buivandat.TestApiDTS.config;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.buivandat.TestApiDTS.domain.Permission;
import vn.buivandat.TestApiDTS.domain.Role;
import vn.buivandat.TestApiDTS.domain.User;
import vn.buivandat.TestApiDTS.repository.PermissionRepository;
import vn.buivandat.TestApiDTS.repository.RoleRepository;
import vn.buivandat.TestApiDTS.repository.UserRepository;
import vn.buivandat.TestApiDTS.util.constant.GenderEnum;
@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository,RoleRepository roleRepository,
    PasswordEncoder passwordEncoder,PermissionRepository permissionRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
       System.out.println("Start Init Database");
       long countPermissions = this.permissionRepository.count();
       long countRoles = this.roleRepository.count();
       long countUsers = this.userRepository.count();

       if(countPermissions == 0 ){
        ArrayList<Permission> arr = new ArrayList<>();
      
            arr.add(new Permission("Create a permission", "/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/permissions/{id}" , "DELETE", "PERMISSIONS"));

            arr.add(new Permission("Create a role", "/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/roles", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/roles/{id}", "DELETE", "ROLES"));

            arr.add(new Permission("Create a user", "/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/users", "GET", "USERS"));

            arr.add(new Permission("Upload a avatar", "/avatar/{userId}", "POST", "FILES"));

            this.permissionRepository.saveAll(arr);
       }

       if(countRoles == 0){
        List<Permission> allPermissions = this.permissionRepository.findAll();
        
        Role adminRole = new Role();
        adminRole.setName("SUPER_ADMIN");
        adminRole.setDescription("Admin full permission");
        adminRole.setActive(true);
        adminRole.setPermissions(allPermissions);
        this.roleRepository.save(adminRole);
       }

       if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

          Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }
    
   


    
}

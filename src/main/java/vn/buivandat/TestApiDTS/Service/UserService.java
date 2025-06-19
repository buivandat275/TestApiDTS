package vn.buivandat.TestApiDTS.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.buivandat.TestApiDTS.domain.Role;
import vn.buivandat.TestApiDTS.domain.User;
import vn.buivandat.TestApiDTS.domain.respone.ResCreateUserDTO;
import vn.buivandat.TestApiDTS.domain.respone.ResUpdateUserDTO;
import vn.buivandat.TestApiDTS.domain.respone.ResUserDTO;
import vn.buivandat.TestApiDTS.repository.UserRepository;
import vn.buivandat.TestApiDTS.util.error.IdInvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User create(User user) {
        // check role
        if (user.getRole() != null) {
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        }

        return this.userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmailAndDeletedFalse(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public boolean existById(long id) {
        return this.userRepository.existsById(id);
    }

    public boolean deleteUserById(Long id) {
        Optional<User> userOpt = this.userRepository.findByIdAndDeletedFalse(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeleted(true);
            this.userRepository.save(user);
            return true;
        }
        return false;
    }

    public User handleUpdateUser(User requestUser) throws IdInvalidException {
        Optional<User> userOpt = this.userRepository.findByIdAndDeletedFalse(requestUser.getId());
        if (userOpt.isEmpty()) {
            throw new IdInvalidException("User không tồn tại hoặc đã xóa.");
        }

        User currentUser = userOpt.get();
        currentUser.setName(requestUser.getName());
        currentUser.setAvatar(requestUser.getAvatar());
        currentUser.setPhone(requestUser.getPhone());
        currentUser.setGender(requestUser.getGender());

        // Check role
        if (requestUser.getRole() != null) {
            Role r = this.roleService.fetchById(requestUser.getRole().getId());
            currentUser.setRole(r != null ? r : currentUser.getRole());
        }

        return this.userRepository.save(currentUser);
    }

    public List<User> findAllActiveUsers() {
        return this.userRepository.findByDeletedFalse();
    }

    public Optional<User> findUserById(Long id) {
        return this.userRepository.findByIdAndDeletedFalse(id);
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmailAndDeletedFalse(token, email);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAvatar(user.getAvatar());
        res.setPhone(user.getPhone());
        res.setCreateAt(user.getCreatedAt());
        res.setUpdateAt(user.getUpdatedAt());
        return res;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAvatar(user.getAvatar());
        res.setPhone(user.getPhone());
        res.setCreateAt(user.getCreatedAt());
        res.setCreateBy(user.getCreatedBy());
        return res;
    }

    public ResUpdateUserDTO convertToUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAvatar(user.getAvatar());
        res.setPhone(user.getPhone());
        res.setUpdateBy(user.getUpdatedBy());
        res.setUpdateAt(user.getUpdatedAt());
        return res;
    }

}

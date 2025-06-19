package vn.buivandat.TestApiDTS.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import jakarta.validation.Valid;
import vn.buivandat.TestApiDTS.Service.UserService;
import vn.buivandat.TestApiDTS.domain.User;
import vn.buivandat.TestApiDTS.domain.respone.ResCreateUserDTO;
import vn.buivandat.TestApiDTS.domain.respone.ResUpdateUserDTO;
import vn.buivandat.TestApiDTS.domain.respone.ResUserDTO;
import vn.buivandat.TestApiDTS.util.annotation.ApiMessage;
import vn.buivandat.TestApiDTS.util.error.IdInvalidException;

@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        boolean existEmail = this.userService.existsByEmail(user.getEmail());
        if (existEmail == true) {
            throw new IdInvalidException("Email " + user.getEmail() + " đã tồn tại, Vui lòng dùng email khác!");
        }
        User newUser = this.userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete user success")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        boolean deleted = this.userService.deleteUserById(id); // gọi soft delete

        if (!deleted) {
            throw new IdInvalidException("Id " + id + " không tồn tại hoặc đã bị xóa");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/users")
    @ApiMessage("update user success")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {
        User user = this.userService.handleUpdateUser(postManUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToUpdateUserDTO(user));
    }

    @GetMapping("/users")
public ResponseEntity<List<ResUserDTO>> findAllUsers() {
    List<User> users = userService.findAllActiveUsers();
    List<ResUserDTO> result = users.stream()
                                   .map(userService::convertToResUserDTO)
                                   .collect(Collectors.toList());
    return ResponseEntity.ok(result);
}

    @GetMapping("/users/{id}")
public ResponseEntity<ResUserDTO> findUserById(@PathVariable("id") long id) throws IdInvalidException {
    User user = this.userService.findUserById(id)
        .orElseThrow(() -> new IdInvalidException("User với id = " + id + " không tồn tại hoặc đã xóa"));

    return ResponseEntity.status(HttpStatus.OK)
            .body(this.userService.convertToResUserDTO(user));
}



}

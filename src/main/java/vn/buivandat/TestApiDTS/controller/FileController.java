package vn.buivandat.TestApiDTS.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.buivandat.TestApiDTS.Service.UserService;
import vn.buivandat.TestApiDTS.domain.respone.ResResponse;

@RestController
public class FileController {
    private final UserService userService;

    public FileController(UserService userService) {
        this.userService = userService;
    }

   @PostMapping("/avatar/{userId}")
public ResponseEntity<ResResponse<String>> uploadUserAvatar(
        @PathVariable Long userId,
        @RequestParam("file") MultipartFile file) throws IOException {

    String url = userService.uploadAvatar(userId, file);
    ResResponse<String> resp = new ResResponse<>();
    resp.setStatusCode(200);
    resp.setError(null);
    resp.setMessage("Upload thành công");
    resp.setData(url);
    return ResponseEntity.ok(resp);
}

}

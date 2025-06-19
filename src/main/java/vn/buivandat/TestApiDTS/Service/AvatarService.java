package vn.buivandat.TestApiDTS.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AvatarService {
    private final String UPLOAD_DIR = "uploads/";

  public String storeAvatar(Long userId, MultipartFile file) throws IOException {
    Files.createDirectories(Paths.get(UPLOAD_DIR));
    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
    String fileName = "avatar_" + userId + "_" + UUID.randomUUID() + "." + extension;
    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    return "/uploads/" + fileName;
  }

  public void deleteAvatarFile(String filePath) {
    if (filePath == null) return;
    try {
      Path path = Paths.get(filePath.replaceFirst("^/", ""));
      Files.deleteIfExists(path);
    } catch (IOException e) {
      // log error
    }
  }
}

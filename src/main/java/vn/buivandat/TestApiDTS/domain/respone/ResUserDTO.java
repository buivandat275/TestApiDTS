package vn.buivandat.TestApiDTS.domain.respone;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.buivandat.TestApiDTS.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
     private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String phone;
    private String avatar;
    private Instant createAt;
    private Instant updateAt;

     private RoleUser role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  RoleUser {
        private long id;
        private String name;
        
    }
}

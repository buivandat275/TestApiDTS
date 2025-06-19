package vn.buivandat.TestApiDTS.domain.respone;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.buivandat.TestApiDTS.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String phone;
    private String avatar;
   
    private Instant createAt;
    private String createBy;
}

package vn.buivandat.TestApiDTS.domain.respone;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.buivandat.TestApiDTS.domain.Role;

@Getter
@Setter
public class ResLoginDTO {
      @JsonProperty("access_token")//đôi tên accessToken ->access_token để phù hợp với forntend
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
   public static class  UserLogin {
        private long id;
        private String email;
        private String name;
        private Role role;
    
   }

   @Getter
   @Setter
   @AllArgsConstructor
   @NoArgsConstructor
   public static class  UserGetAccount {
     private UserLogin user;
     
   }

   @Getter
   @Setter
   @AllArgsConstructor
   @NoArgsConstructor
   public static class  UserInsideToken {
     private long id;
     private String email;
     private String name;
     
   }
}

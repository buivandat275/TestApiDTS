package vn.buivandat.TestApiDTS.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.buivandat.TestApiDTS.Service.UserService;
import vn.buivandat.TestApiDTS.domain.User;
import vn.buivandat.TestApiDTS.domain.request.ReqLoginDTO;
import vn.buivandat.TestApiDTS.domain.respone.ResLoginDTO;
import vn.buivandat.TestApiDTS.util.SecurityUtil;
import vn.buivandat.TestApiDTS.util.annotation.ApiMessage;
import vn.buivandat.TestApiDTS.util.error.IdInvalidException;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,  SecurityUtil securityUtil,
            UserService userService,
            PasswordEncoder passwordEncoder
            ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${buivandat.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpriration;

     @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) throws IdInvalidException {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getName(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }
        //create access token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);
        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpriration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);

    }

     @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? 
        SecurityUtil.getCurrentUserLogin().get() : "" ;

        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUserDB != null) {
                userLogin.setId( currentUserDB.getId());
                userLogin.setEmail(currentUserDB.getEmail());
                userLogin.setName(currentUserDB.getName());
                userLogin.setRole(currentUserDB.getRole());
                userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
        @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token
        ) throws IdInvalidException{
            if(refresh_token.equals("abc")){
                throw new IdInvalidException("Bạn không có refresh token ở cookie");
            }
        //check valid
         Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

            //check user by token + email
            User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
            if(currentUser == null){
                throw new IdInvalidException("Refresh Token không hợp lệ");
            }

            //issue new token/set refresh token as cookies

        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getName(),currentUserDB.getRole());
            res.setUser(userLogin);
        }
        //create access token
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);
        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpriration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User ")
    public ResponseEntity<Void> logoutToken() throws IdInvalidException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if(email.equals("")){
            throw new IdInvalidException("Access Token không hợp lệ");
        }

        this.userService.updateUserToken(null, email);

        // set cookies
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);


    }
    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<User> register(@Valid @RequestBody User postManUser)
            throws IdInvalidException {

        boolean isEmailExist = this.userService.existsByEmail(postManUser.getEmail());
        if(isEmailExist){
            throw new IdInvalidException("Email "+ postManUser.getEmail() +" đã tồn tại, vui lòng dùng email kháckhác");
        }
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User newUser = this.userService.create(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    
}

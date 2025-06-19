package vn.buivandat.TestApiDTS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.buivandat.TestApiDTS.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmailAndDeletedFalse(String token, String email);

    User findByEmailAndDeletedFalse(String email);

    List<User> findByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Long id);
    
    Optional<User> findByUserNameAndDeletedFalse(String username); 

}

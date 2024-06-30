package org.senju.eshopeule.repository;

import jakarta.transaction.Transactional;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.projection.LoginInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE username = :un OR email = :em)", nativeQuery = true)
    boolean checkUserExistsWithUsernameOrEmail(@Param("un") String username, @Param("em") String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE username = :un OR email = :em AND id != :id)", nativeQuery = true)
    boolean checkUserExistsWithUsernameOrEmailExceptId(@Param("un") String username, @Param("em") String email, @Param("id") String id);

    @Query(value = "SELECT email FROM users WHERE username = :ide OR email = :ide OR phone_number = :ide", nativeQuery = true)
    Optional<String> getEmailByIde(@Param("ide") String ide);

    @Query(value = "SELECT username FROM users WHERE email = :email", nativeQuery = true)
    Optional<String> getUsernameByEmail(@Param("email") String email);

    @Query(value = "SELECT password FROM users WHERE username = :un", nativeQuery = true)
    Optional<String> getEncodedPasswordByUsername(@Param("un") String username);

    @Query(value = "SELECT username, email FROM users WHERE username = :ide OR email = :ide", nativeQuery = true)
    Optional<LoginInfoView> getLoginInfoViewByIdentifier(@Param("ide") String identifier);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password = :newPassword WHERE username = :un", nativeQuery = true)
    void updatePasswordWithUsername(@Param("un") String username, @Param("newPassword") String newPassword);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password = :newPassword WHERE email = :em", nativeQuery = true)
    void updatePasswordWithEmail(@Param("em") String email, @Param("newPassword") String newPassword);
}
package org.senju.eshopeule.repository;

import org.senju.eshopeule.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT id FROM users WHERE username = :un OR email = :em OR phone_number = :pn", nativeQuery = true)
    List<String> findAllByUsernameOrEmailOrPhoneNumber(@Param("un") String username, @Param("em") String email, @Param("pn") String phoneNumber);

    @Query(value = "SELECT username FROM users WHERE username = :ide OR email = :ide OR phone_number = :ide", nativeQuery = true)
    Optional<String> findUsernameByIde(@Param("ide") String ide);

    @Query(value = "SELECT username FROM users WHERE email = :email", nativeQuery = true)
    Optional<String> findUsernameByEmail(@Param("email") String email);
}

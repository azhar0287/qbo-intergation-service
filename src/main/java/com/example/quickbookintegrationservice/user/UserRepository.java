package com.example.quickbookintegrationservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.uuid =:uuid")
    User getUserByUuid(@Param("uuid") String uuid);
}

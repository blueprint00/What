package com.example.what.Domain;

import com.example.what.Dto.UserDTO;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface User_info_repository extends JpaRepository<User_info, String> {
    @Query(value = "SELECT * FROM user WHERE user_id = ?1", nativeQuery = true)
    Optional<User_info> findOneWithAuthoritiesByUser_id(@Param("user_id") String user_id);

    @Query(value = "SELECT count(*) FROM user WHERE user_id = ?1", nativeQuery = true)
    Long countByUserId(@Param("user_id") String user_id);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO user VALUES(?1, ?2)", nativeQuery = true)
//    void save

}

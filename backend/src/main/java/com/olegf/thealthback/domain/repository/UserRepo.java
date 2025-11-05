package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.AppUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<AppUser, Long> {

    @Query("""
    select app_user from app_users app_user
        where CAST(app_user.phone as text) = :phone
    """)
    Optional<AppUser> findByPhone(String phone);
}

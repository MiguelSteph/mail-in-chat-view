package com.mailchatview.authserver.repo;

import com.mailchatview.authserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserId(String userId);
}

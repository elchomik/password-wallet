package com.example.wallet.repositories;

import com.example.wallet.readmodel.readonly.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Integer> {
        User findUserByLogin(String login);
}

package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.UserInfo;
import com.briamcarrasco.arriendomaquinaria.model.User;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByUser(User user);

    UserInfo findByUser(User user);
}
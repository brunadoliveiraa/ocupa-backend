package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    void delete(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

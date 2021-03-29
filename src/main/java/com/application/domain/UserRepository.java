package com.application.domain;

import com.application.domain.entity.User;
import com.application.domain.utils.PaginatedResponse;

import java.util.List;

public interface UserRepository {
    void createUser(User user);
    void createUsers(List<User> users);
    User getUserByID(Long id);
    PaginatedResponse<User> listUsers(Long lastFetchedId, Integer limit);
}

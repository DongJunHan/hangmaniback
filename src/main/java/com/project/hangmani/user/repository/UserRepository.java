package com.project.hangmani.user.repository;

import com.project.hangmani.user.model.dto.RequestInsertDTO;
import com.project.hangmani.user.model.dto.UserDTO;
import com.project.hangmani.user.model.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<UserDTO> getByoAuthId(String oAuthId);
    Optional<UserDTO> getById(String userID);
    String add(User user);
    int delete(String userID);
}

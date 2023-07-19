package com.project.hangmani.service;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final AES aes;
    private ConvertData convertData;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, AES aes) {
        this.aes = aes;
        this.userRepository = userRepository;
        this.convertData = new ConvertData();
    }

    @Transactional
    public ResponseUserDTO InsertUser(RequestInsertUserDTO requestInsertUserDTO) {
        //encrypt, encoding id data
        byte[] encryptOAuthID = this.aes.encryptData(requestInsertUserDTO.getOAuthID(), StandardCharsets.UTF_8);
        String base64OAuthID = convertData.byteToBase64(encryptOAuthID);
        //confirm exist user @Valid
        Optional<User> users = userRepository.findByoAuthId(base64OAuthID);
        if (!users.isEmpty()){
            User user = users.get();
            return ResponseUserDTO.builder()
                    .id(user.getId())
                    .refreshToken(requestInsertUserDTO.getRefreshToken())
                    .oAuthType(user.getOAuthType())
                    .build();
        }

        //insert user
        String userID = userRepository.insertUser(requestInsertUserDTO);
        if (userID.isBlank())
            throw new FailInsertData();
        //check user data
        Optional<User> findUser = userRepository.findById(userID);
        if (findUser.isEmpty())
            throw new FailInsertData();

        User user = findUser.get();
        log.info("login user: {}", user);
        return ResponseUserDTO.builder()
                .id(user.getId())
                .refreshToken(requestInsertUserDTO.getRefreshToken())
                .oAuthType(user.getOAuthType())
                .build();
    }

    @Transactional
    public void deleteUser(String userID) {
        //check id valid
        Optional<User> usersByID = userRepository.findById(userID);
        if (usersByID.isEmpty()){
            throw new NotFoundUser();
        }
        User user = usersByID.get();
        //delete db
        int ret = userRepository.deleteUser(user.getId());
        log.error("ret={}",ret);
        if (ret != 1) {
            throw new FailInsertData();
        }
    }
}

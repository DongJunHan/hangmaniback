package com.project.hangmani.user.service;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.user.model.dto.RequestInsertDTO;
import com.project.hangmani.user.model.dto.ResponseDTO;
import com.project.hangmani.user.model.dto.UserDTO;
import com.project.hangmani.user.model.entity.User;
import com.project.hangmani.user.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
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
    private Util util;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, AES aes, PropertiesValues propertiesValues) {
        this.aes = aes;
        this.util = new Util(propertiesValues);
        this.userRepository = userRepository;
        this.convertData = new ConvertData(propertiesValues);
    }

    @Transactional
    public ResponseDTO InsertUser(RequestInsertDTO requestDTO) {
        ResponseDTO result = checkUserByOAuthID(requestDTO.getOAuthID());
        if (result != null)
            return result;
        //dto to entity
        User user = requestDTO.convertToEntity();
        //encrypt
        String base64OAuthID = encryptData(user.getOAuthID());
        String base64RefreshToken = encryptData(user.getRefreshToken());
        user.setOAuthID(base64OAuthID);
        user.setRefreshToken(base64RefreshToken);
        //insert user
        String userID = userRepository.add(user);
        if (userID.isBlank())
            throw new FailInsertData();

        //check user data
        UserDTO ret = checkUserByID(userID);
        log.info("login user: {}", ret);

        return ResponseDTO.builder()
                .id(ret.getId())
                .refreshToken(requestDTO.getRefreshToken())
                .oAuthType(ret.getOAuthType())
                .build();
    }

    @Transactional
    public void deleteUser(String userID) {
        //check user exist
        UserDTO user = checkUserByID(userID);
        //delete db
        int ret = userRepository.delete(user.getId());
        log.error("ret={}",ret);
        if (ret != 2) {
            throw new FailInsertData();
        }
    }

    private String encryptData(String plain) {
        String base64Data = plain;
        log.info("{}", this.util.isBase64(plain));
        if (!this.util.isBase64(plain)) {
            //encrypt,base64 encoding plain data
            byte[] encryptData = this.aes.encryptData(plain, StandardCharsets.UTF_8);
            base64Data = this.convertData.byteToBase64(encryptData);
        }
        return base64Data;
    }

    private UserDTO checkUserByID(String userID) {
        Optional<UserDTO> findUser = userRepository.getById(userID);
        if (findUser.isEmpty())
            throw new NotFoundUser();

        return findUser.get();
    }
    private ResponseDTO checkUserByOAuthID(String oAuthID) {
        String base64OAuthID = encryptData(oAuthID);
        //confirm exist user @Valid
        Optional<UserDTO> users = userRepository.getByoAuthId(base64OAuthID);
        if (!users.isEmpty()){
            UserDTO user = users.get();
            return ResponseDTO.builder()
                    .id(user.getId())
                    .refreshToken(user.getRefreshToken())
                    .oAuthType(user.getOAuthType())
                    .build();
        }else{
            return null;
        }
    }
}

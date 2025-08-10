package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    String register(String username,String password) throws Exception;

    String login(String username,String password) throws Exception;

    String validateEmail(String username, String email,String otp) throws Exception;

    void checkPassword(String rawPassword, String hashedPassword);

    User getUserByUsername(String username) throws UsernameNotFoundException;

}

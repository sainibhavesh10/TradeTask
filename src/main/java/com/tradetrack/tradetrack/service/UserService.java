package com.tradetrack.tradetrack.service;

public interface UserService {

    String register(String username,String password) throws Exception;

    String login(String username,String password) throws Exception;

    String validateEmail(String email,String otp) throws Exception;

    String getCurrentUsername();

    boolean checkPassword(String rawPassword, String hashedPassword);

}

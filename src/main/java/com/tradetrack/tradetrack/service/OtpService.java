package com.tradetrack.tradetrack.service;

public interface OtpService {

    String generateOtp();

    void sendOtp(String email);

    boolean validateOtp(String email,String otp);
}

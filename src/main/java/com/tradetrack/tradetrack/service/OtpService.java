package com.tradetrack.tradetrack.service;

public interface OtpService {

    void sendOtp(String email);

    void validateOtp(String email,String otp);
}

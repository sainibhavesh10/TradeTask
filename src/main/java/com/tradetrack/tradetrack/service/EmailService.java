package com.tradetrack.tradetrack.service;

public interface EmailService {

    void sendMail(String to,String subject,String body);
}

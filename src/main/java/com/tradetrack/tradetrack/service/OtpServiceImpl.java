package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.entity.Otp;
import com.tradetrack.tradetrack.entity.User;
import com.tradetrack.tradetrack.repo.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService{

    private final OtpRepository otpRepository;

    private final EmailService emailService;

    @Autowired
    public OtpServiceImpl(OtpRepository otpRepository,EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void sendOtp(String email) {
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(generateOtp());

        otpRepository.save(otp);

        String subject = "Email verification for TradeTask";
        String body = "Please enter the following code on the page where you requested for an OTP: " +
                otp.getCode() +
                " This verification code will only be valid for the next 10 minutes.";

        emailService.sendMail(email,subject,body);
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        Optional<Otp> optionalOtp = otpRepository.findTopByEmailOrderByExpiryTimeDesc(email);
        if(optionalOtp.isEmpty()) return false;

        Otp topOtp = optionalOtp.get();
        if(topOtp.getExpiryTime().isBefore(LocalDateTime.now())) return false;
        if(topOtp.isUsed()) return false;
        if(topOtp.getAttempts()>=3) return false;
        if(!topOtp.getCode().equals(otp)) {
            topOtp.increaseAttempt();
            return false;
        };

        topOtp.setUsed(true);
        otpRepository.save(topOtp);

        return true;
    }


}

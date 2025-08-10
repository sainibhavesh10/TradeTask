package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Exceptions.Types.OtpException;
import com.tradetrack.tradetrack.entity.Otp;
import com.tradetrack.tradetrack.repo.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;

    private final EmailService emailService;

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Autowired
    public OtpServiceImpl(OtpRepository otpRepository,EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
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

    @Transactional
    @Override
    public void validateOtp(String email, String otp) {
        Optional<Otp> optionalOtp = otpRepository.findTopByEmailOrderByExpiryTimeDesc(email);
        if(optionalOtp.isEmpty()){
            throw new OtpException("No OTP found for this email.",OtpException.ErrorType.NOT_FOUND);
        }

        Otp topOtp = optionalOtp.get();
        if(topOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new OtpException("OTP has expired.",OtpException.ErrorType.EXPIRED);
        }
        if(topOtp.isUsed()) {
            throw new OtpException("OTP has already been used.",OtpException.ErrorType.USED);
        }
        if(topOtp.getAttempts()>=3) {
            throw new OtpException("Maximum OTP attempts exceeded.",OtpException.ErrorType.ATTEMPTS_EXCEEDED);
        }
        if(!topOtp.getCode().equals(otp)) {
            topOtp.increaseAttempt();
            throw new OtpException("Invalid OTP code.",OtpException.ErrorType.INCORRECT);
        };

        topOtp.setUsed(true);
        otpRepository.save(topOtp);
    }


}

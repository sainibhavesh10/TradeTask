package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.UserRole;
import com.tradetrack.tradetrack.entity.User;
import com.tradetrack.tradetrack.repo.UserRepository;
import com.tradetrack.tradetrack.security.CustomUserDetails;
import com.tradetrack.tradetrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private OtpService otpService;

    private JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,OtpService otpService,JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public String register(String username,String password) throws Exception{
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) throw new Exception("User Already Exists");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        //creating jwt
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtUtil.generateToken(userDetails);
        return jwtToken;
    }

    @Override
    public String login(String username,String password) throws Exception{
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) throw new Exception("User Don't Exists");

        User user = optionalUser.get();

        if(!checkPassword(password,user.getPassword())) throw new Exception("Invalid credentials");

        //creating jwt
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String jwtToken = jwtUtil.generateToken(userDetails);

        return jwtToken;
    }

    @Override
    public String validateEmail(String email,String otp) throws Exception{
        if(!otpService.validateOtp(email, otp)) throw new Exception("Invalid otp");

        User user = userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new Exception("User not found"));

        user.setEmail(email);
        user.setVerified(true);
        user.setRole(UserRole.USER);

        userRepository.save(user);

        //creating jwt
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String jwtToken = jwtUtil.generateToken(userDetails);

        return jwtToken;
    }
}

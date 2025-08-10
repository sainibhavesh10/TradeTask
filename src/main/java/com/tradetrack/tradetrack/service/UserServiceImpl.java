package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.UserRole;
import com.tradetrack.tradetrack.Exceptions.Types.UserException;
import com.tradetrack.tradetrack.entity.User;
import com.tradetrack.tradetrack.repo.UserRepository;
import com.tradetrack.tradetrack.security.CustomUserDetails;
import com.tradetrack.tradetrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void checkPassword(String rawPassword, String hashedPassword) {
        if (hashedPassword == null || !passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new UserException("Invalid credentials", UserException.ErrorType.INVALID_CREDENTIALS);
        }
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserException(
                "User don't exist " + username,
                UserException.ErrorType.USER_NOT_FOUND
        ));
    }

    @Override
    public String register(String username,String password) throws Exception{
        boolean exists = userRepository.findByUsername(username).isPresent();
        if (exists) {
            throw new UserException("User already exists", UserException.ErrorType.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        //creating jwt
        return jwtUtil.generateToken(new CustomUserDetails(user));
    }

    @Override
    public String login(String username,String password){
        User user = getUserByUsername(username);

        checkPassword(password,user.getPassword());

        //creating jwt
        return jwtUtil.generateToken(new CustomUserDetails(user));
    }

    @Override
    public String validateEmail(String username, String email,String otp){
        otpService.validateOtp(email, otp);

        User user = getUserByUsername(username);

        user.setEmail(email);
        user.setVerified(true);
        user.setRole(UserRole.USER);

        userRepository.save(user);

        //creating jwt
        return jwtUtil.generateToken(new CustomUserDetails(user));
    }
}

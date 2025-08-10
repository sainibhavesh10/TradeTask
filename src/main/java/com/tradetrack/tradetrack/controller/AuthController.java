package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.request.LoginRequest;
import com.tradetrack.tradetrack.request.RegisterRequest;
import com.tradetrack.tradetrack.request.ValidationRequest;
import com.tradetrack.tradetrack.response.AuthResponse;
import com.tradetrack.tradetrack.security.CustomUserDetails;
import com.tradetrack.tradetrack.security.JwtUtil;
import com.tradetrack.tradetrack.service.OtpService;
import com.tradetrack.tradetrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try {
            String jwtToken = userService.register(
                    registerRequest.getUsername(),
                    registerRequest.getPassword()
            );

            return ResponseEntity.ok().body(
                   new AuthResponse(jwtToken,"Registration Successful")
            );
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Registration failed: " + ex.getMessage()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            String jwtToken = userService.login(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            return ResponseEntity.ok().body(
                    new AuthResponse(jwtToken, "Login Successful")
            );
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Login failed: " + ex.getMessage()
            );
        }
    }

    @GetMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestParam String email){
        otpService.sendOtp(email);
        return ResponseEntity.ok().body("Otp Sent Successfully");
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<?> validateEmail(
            @RequestBody ValidationRequest validationRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        try {
            String jwtToken = userService.validateEmail(
                    userDetails.getUsername(),
                    validationRequest.getEmail(),
                    validationRequest.getOtp()
            );

            return ResponseEntity.ok().body(
                    new AuthResponse(jwtToken, "Validation Successful")
            );
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Validation failed: " + ex.getMessage()
            );
        }
    }

}

package com.sdm.Authentication.auth.controller;

import com.sdm.Authentication.auth.dto.*;
import com.sdm.Authentication.auth.repository.UserRepository;
import com.sdm.Authentication.auth.service.AuthService;
import com.sdm.Authentication.auth.service.ForgotPasswordService;
import com.sdm.Authentication.auth.util.JwtUtil;
import com.sdm.Authentication.entity.User;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ForgotPasswordService forgotPasswordService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository, JwtUtil jwtUtil, ForgotPasswordService forgotPasswordService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {

            if (optionalUser.get().isActivated()) {
                JSONObject response = new JSONObject();
                try {
                    response.put("userId", optionalUser.get().getId());
                    response.put("token", jwt);
                    response.put("tokenType", "Bearer");
                } catch (JSONException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating response.");
                }

                return ResponseEntity.ok(response.toString());
            }else {
                return ResponseEntity.badRequest().body("Account is not activated. Check your email.");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignupRequest signupRequest){
        if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam("code") String code) {
        User user = userRepository.findByActivationCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid activation code!"));

        user.setActivated(true);
        user.setActivationCode(null);
        userRepository.save(user);

        return ResponseEntity.ok("Account activated successfully! You can now log in.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String response = forgotPasswordService.generateResetToken(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {

        if (forgotPasswordService.validateResetToken(token)) {
            if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and Confirm Password Not Match.");
            } else {
                forgotPasswordService.updatePassword(token, request.getNewPassword());
                return ResponseEntity.ok("Password has been successfully reset.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

}


package com.sdm.Authentication.auth.controller;

import com.sdm.Authentication.auth.dto.AuthenticationRequest;
import com.sdm.Authentication.auth.dto.SignupRequest;
import com.sdm.Authentication.auth.dto.UserDto;
import com.sdm.Authentication.auth.repository.UserRepository;
import com.sdm.Authentication.auth.service.AuthService;
import com.sdm.Authentication.auth.util.JwtUtil;
import com.sdm.Authentication.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository, JwtUtil jwtUtil, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization ";

    private final AuthService authService;

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
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            JSONObject response = new JSONObject();
            try {
                response.put("userId", optionalUser.get().getId());
                response.put("token", jwt);
                response.put("tokenType", "Bearer");
            } catch (JSONException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating response.");
            }

            return ResponseEntity.ok(response.toString());
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
}


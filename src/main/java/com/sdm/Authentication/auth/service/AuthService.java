package com.sdm.Authentication.auth.service;

import com.sdm.Authentication.auth.dto.SignupRequest;
import com.sdm.Authentication.auth.dto.UserDto;
import com.sdm.Authentication.auth.repository.UserRepository;
import com.sdm.Authentication.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ActivationService activationService;

    private final EmailService emailService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ActivationService activationService, EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.activationService = activationService;
        this.emailService = emailService;
    }

    public UserDto createUser(SignupRequest signupRequest){
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        String activationCode = activationService.generateActivationCode();
        user.setActivationCode(activationCode);
        User createUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());
        userDto.setEmail(createUser.getEmail());
        userDto.setName(createUser.getName());
        userDto.setActivationCode(createUser.getActivationCode());
        // Send activation email

        emailService.sendActivationEmail(userDto.getEmail(), activationCode);


        return userDto;
    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }
}

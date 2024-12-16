package com.sdm.Authentication.auth.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivationService {

    public String generateActivationCode() {
        return UUID.randomUUID().toString();
    }
}

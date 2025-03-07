package com.shorter.service;

import org.springframework.stereotype.Service;

@Service
public class PremiumService {

    private final UserService userService;

    public PremiumService(UserService userService) {
        this.userService = userService;
    }

    public boolean isUserPremium(String userId) {
        return userService.findByUsername(userId).isPremium();
    }
}

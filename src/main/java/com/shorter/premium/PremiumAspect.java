package com.shorter.premium;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.shorter.models.User;
import com.shorter.service.UserService;

@Aspect
@Component
public class PremiumAspect {

    @Autowired
    private UserService userService;

    @Before("@annotation(PremiumRequired)")
    public void checkPremiumUser(JoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByEmail(username);

        if (!user.isPremium()) {
            throw new AccessDeniedException("Access is denied. Premium membership is required.");
        }
    }
}

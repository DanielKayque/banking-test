package com.api.banking_study.features.user;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.UserRepository;
import com.api.banking_study.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    public String execute(LoginUserDto data) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var manager = this.manager.authenticate(auth);

        UserEntity user = (UserEntity) manager.getPrincipal();

        return this.tokenService.generateToken(user);
    }
}
